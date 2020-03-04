/*
 * Copyright 2020 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.armeria.server.throttling.tokenbucket;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

/**
 * Stores configuration of the Token-Bucket algorithm.
 */
public class TokenBucket {

    @Nonnull
    private final BandwidthLimit[] limits;

    /**
     * Defines throttling configuration comprised of one or more bandwidth limits in accordance to
     * token-bucket algorithm.
     *
     * <h3>Multiple bandwidths:</h3>
     * It is possible to specify more than one bandwidth per bucket,
     * and bucket will handle all bandwidth in strongly atomic way.
     * Strongly atomic means that token will be consumed from all bandwidth or from nothing,
     * in other words any token can not be partially consumed.
     * <br> Example of multiple bandwidth:
     * <pre>{@code
     * // Adds bandwidth that restricts to consume
     * // not often than 1000 tokens per 1 minute and
     * // not often than 100 tokens per second.
     * TokenBucketConfig config = TokenBucketConfig.builder()
     *      .limit(1000L, Duration.ofMinutes(1))
     *      .limit(100L, Duration.ofSeconds(1))
     *      .build()
     * }</pre>
     *
     * @param limits one or more bandwidth limits to be used by token-bucket algorithm
     */
    TokenBucket(@Nonnull BandwidthLimit... limits) {
        this.limits = requireNonNull(limits, "limits");
    }

    /**
     * Creates a new {@link TokenBucketBuilder}.
     */
    public static TokenBucketBuilder builder() {
        return new TokenBucketBuilder();
    }

    /**
     *  Multiple limits applied to the bucket. This may be empty.
     * @return An array of {@link BandwidthLimit}
     */
    @Nonnull
    public BandwidthLimit[] limits() {
        return limits;
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("limits", limits)
                .toString();
    }

    @Nullable
    private BandwidthLimit lowestLimit() {
        BandwidthLimit lowestLimit = null;
        for (BandwidthLimit limit : limits) {
            if (lowestLimit == null) {
                lowestLimit = limit;
            } else {
                if (Double.compare(limit.ratePerSecond(), lowestLimit.ratePerSecond()) < 0) {
                    lowestLimit = limit;
                }
            }
        }
        return lowestLimit;
    }

    /**
     * Returns a string representation of the multiple limits in the following format:
     * <pre>{@code
     * <lowest limit>, <first limit>;window=<first period(in seconds)>;burst=<first overdraftLimit>,
     *                 <second limit>;window=<second period(in seconds)>;burst=<second overdraftLimit>, etc.
     * }</pre>
     * For example: "100, 100;window=60;burst=1000, 5000;window=3600;burst=0".
     *
     * @return A {@link String} representation of the limits.
     */
    @Nullable
    String toHeaderString() {
        final BandwidthLimit lowestLimit = lowestLimit();
        if (limits.length == 0 || lowestLimit == null) {
            return null;
        }
        return lowestLimit.limit() + ", " + Arrays.stream(limits)
                                                  .map(BandwidthLimit::toHeaderString)
                                                  .collect(Collectors.joining(", "));
    }
}
