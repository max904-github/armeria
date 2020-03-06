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

public class BandwidthLimitTest {

    @Test
    public void testSpecification() {
        final BandwidthLimit bl1 = BandwidthLimit.of("100;window=60;burst=1000");
        System.out.println(bl1);
        assertEquals(100L, bl1.limit());
        assertEquals(1000L, bl1.overdraftLimit());
        assertEquals(0L, bl1.initialSize());
        assertEquals(Duration.ofSeconds(60L), bl1.period());
    }
}
