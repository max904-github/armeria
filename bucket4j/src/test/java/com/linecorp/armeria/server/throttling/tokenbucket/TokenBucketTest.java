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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;

public class TokenBucketTest {

    @Test
    public void testSpecification() {
        final TokenBucket tb1 = TokenBucket.of("100;window=60;burst=1000, 50000;window=3600");
        System.out.println(tb1);

        final BandwidthLimit[] limits1 = tb1.limits();
        assertEquals(2, limits1.length);

        assertEquals(100L, limits1[0].limit());
        assertEquals(1000L, limits1[0].overdraftLimit());
        assertEquals(0L, limits1[0].initialSize());
        assertEquals(Duration.ofSeconds(60L), limits1[0].period());

        assertEquals(50000L, limits1[1].limit());
        assertEquals(0L, limits1[1].overdraftLimit());
        assertEquals(0L, limits1[1].initialSize());
        assertEquals(Duration.ofSeconds(3600L), limits1[1].period());
    }
}
