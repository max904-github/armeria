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

final class ThrottlingHeadersImpl implements ThrottlingHeaders {
    private static final String LIMIT_SUFFIX = "-Limit";
    private static final String REMAINING_SUFFIX = "-Remaining";
    private static final String RESET_SUFFIX = "-Reset";

    private final String limitHeader;
    private final String remainingHeader;
    private final String resetHeader;

    ThrottlingHeadersImpl(final String scheme) {
        limitHeader = scheme + LIMIT_SUFFIX;
        remainingHeader = scheme + REMAINING_SUFFIX;
        resetHeader = scheme + RESET_SUFFIX;
    }

    /**
     * Returns the name of the "limit" throttling header for the given scheme, like "X-RateLimit-Limit".
     * This header specifies the requests quota for the given time window.
     */
    @Override
    public String limitHeader() {
        return limitHeader;
    }

    /**
     * Returns the name of the "remaining" throttling header for the given scheme, like "X-RateLimit-Remaining".
     * This header specifies the remaining requests quota for the current time window.
     */
    @Override
    public String remainingHeader() {
        return remainingHeader;
    }

    /**
     * Returns the name of the "reset" throttling header for the given scheme, like "X-RateLimit-Reset".
     * This header specifies the time remaining in the current window. Its value defined in seconds or
     * as a timestamp.
     */
    @Override
    public String resetHeader() {
        return resetHeader;
    }
}
