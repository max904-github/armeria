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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Builds a {@link TokenBucket} instance using builder pattern.
 */
public class TokenBucketBuilder {
    private static final BandwidthLimit[] NO_BANDWIDTH_LIMITS = {};

    private List<BandwidthLimit> limits = Collections.emptyList();

    TokenBucketBuilder() {} // prevent public access

    /**
     * Adds a number of {@link BandwidthLimit}.
     */
    public TokenBucketBuilder limits(@Nonnull BandwidthLimit... limits) {
        requireNonNull(limits, "limits");
        if (this.limits.isEmpty()) {
            this.limits = new ArrayList<>(2);
        }
        this.limits.addAll(Arrays.asList(limits));
        return this;
    }

    /**
     * Adds new {@link BandwidthLimit}.
     */
    public TokenBucketBuilder limit(long limit, long overdraftLimit, long initialSize, Duration period) {
        return limits(BandwidthLimit.of(limit, overdraftLimit, initialSize, period));
    }

    /**
     * Adds new {@link BandwidthLimit}.
     */
    public TokenBucketBuilder limit(long limit, long overdraftLimit, Duration period) {
        return limits(BandwidthLimit.of(limit, overdraftLimit, period));
    }

    /**
     * Adds new {@link BandwidthLimit}.
     */
    public TokenBucketBuilder limit(long limit, Duration period) {
        return limits(BandwidthLimit.of(limit, period));
    }

    /**
     * Builds {@link TokenBucket}.
     */
    public TokenBucket build() {
        return new TokenBucket(limits.isEmpty() ? NO_BANDWIDTH_LIMITS
                                                : limits.toArray(NO_BANDWIDTH_LIMITS));
    }
}