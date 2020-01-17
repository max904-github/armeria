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

package com.linecorp.armeria.server.throttling;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

/**
 * Decorates an {@link HttpService} to throttle incoming requests.
 */
public class RetryThrottlingService extends ThrottlingService {

    private final RetryThrottlingStrategy<HttpRequest> strategy;

    /**
     * Creates a new decorator using the specified {@link ThrottlingStrategy} instance.
     *
     * @param strategy The {@link ThrottlingStrategy} instance to be used
     */
    public static Function<? super HttpService, ThrottlingService>
    newDecorator(RetryThrottlingStrategy<HttpRequest> strategy) {
        requireNonNull(strategy, "strategy");
        return delegate -> new RetryThrottlingService(delegate, strategy);
    }

    /**
     * Creates a new instance that decorates the specified {@link HttpService}.
     */
    protected RetryThrottlingService(HttpService delegate,
                                     RetryThrottlingStrategy<HttpRequest> strategy) {
        super(delegate, strategy);
        this.strategy = strategy;
    }

    /**
     * Invoked when {@code req} is throttled. This method responds with the status and headers
     * provided by the supplied strategy.
     */
    @Override
    protected HttpResponse onFailure(ServiceRequestContext ctx, HttpRequest req, @Nullable Throwable cause)
            throws Exception {
        return HttpResponse.of(strategy.getFailureResponseHeaders());
    }
}
