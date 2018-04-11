/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yunjing.zuul.ratelimit.support;

/**
 * @author Marcos Barbero
 * @since 2018-04-05
 */
public final class RateLimitConstants {

    private RateLimitConstants() {
        // prevent instantiation
    }

    public static final String HEADER_QUOTA = "X-RateLimit-Quota-";

    public static final String HEADER_REMAINING_QUOTA = "X-RateLimit-Remaining-Quota-";

    public static final String HEADER_LIMIT = "X-RateLimit-Limit-";

    public static final String HEADER_REMAINING = "X-RateLimit-Remaining-";

    public static final String HEADER_RESET = "X-RateLimit-Reset-";

    public static final String REQUEST_START_TIME = "rateLimitRequestStartTime";

}
