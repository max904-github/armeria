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

import java.time.Duration;

import javax.annotation.Nullable;

import com.linecorp.armeria.common.Request;
import com.linecorp.armeria.common.throttling.ThrottlingHeaders;

/**
 * Builds {@link TokenBucketThrottlingStrategy}.
 */
public class TokenBucketThrottlingStrategyBuilder<T extends Request> {

    private final TokenBucket tokenBucket;
    @Nullable
    private Duration minimumBackoff;
    @Nullable
    private ThrottlingHeaders headersScheme;
    private boolean sendQuota;
    @Nullable
    private String name;

    TokenBucketThrottlingStrategyBuilder(TokenBucket tokenBucket) {
        this.tokenBucket = tokenBucket;
    }

    /**
     * Optional name of the strategy.
     * By default, it will be assigned with a predefined name.
     */
    public TokenBucketThrottlingStrategyBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Optional {@link Duration} that defines a minimum backoff period for throttled requests.
     * By default, it will be set to 0 seconds.
     */
    public TokenBucketThrottlingStrategyBuilder<T> withMinimumBackoff(Duration minimumBackoff) {
        this.minimumBackoff = minimumBackoff;
        return this;
    }

    /**
     * Optional {@link ThrottlingHeaders} to define specific RateLimit Header Scheme for HTTP.
     * By default, no throttling headers will be used.
     * The strategy will only use standard HTTP Retry-After header.
     */
    public TokenBucketThrottlingStrategyBuilder<T> withHeadersScheme(ThrottlingHeaders headersScheme,
                                                                     boolean sendQuota) {
        this.headersScheme = headersScheme;
        this.sendQuota = sendQuota;
        return this;
    }

    /**
     * Returns a newly-created {@link TokenBucketThrottlingStrategy}.
     */
    public TokenBucketThrottlingStrategy<T> build() {
        return new TokenBucketThrottlingStrategy<>(tokenBucket, minimumBackoff, headersScheme, sendQuota, name);
    }
}
