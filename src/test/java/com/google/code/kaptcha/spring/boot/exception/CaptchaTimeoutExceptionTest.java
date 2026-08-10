/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.code.kaptcha.spring.boot.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link CaptchaTimeoutException }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("CaptchaTimeoutException Tests")
class CaptchaTimeoutExceptionTest {

    @Test
    @DisplayName("Default constructor uses the standard message")
    void testDefaultConstructor() {
        CaptchaTimeoutException ex = new CaptchaTimeoutException();
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).isEqualTo("captcha expired");
    }

    @Test
    @DisplayName("Message constructor uses the provided message")
    void testMessageConstructor() {
        CaptchaTimeoutException ex = new CaptchaTimeoutException("custom message");
        assertThat(ex.getMessage()).isEqualTo("custom message");
    }

}
