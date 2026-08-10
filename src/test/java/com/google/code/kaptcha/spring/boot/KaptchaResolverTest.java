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
package com.google.code.kaptcha.spring.boot;

import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.code.kaptcha.util.Config;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link KaptchaResolver }}.
 *
 * <p>{@code KaptchaResolver} is an interface, so it is exercised through an
 * anonymous implementation that invokes the {@code default} init methods to
 * ensure they are covered.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("KaptchaResolver Tests")
class KaptchaResolverTest {

    @Test
    @DisplayName("Default init methods are no-ops and can be invoked safely")
    void testDefaultInitMethods() {
        KaptchaResolver resolver = new KaptchaResolver() {
            @Override
            public boolean validCaptcha(HttpServletRequest request, String capText) {
                return false;
            }
            @Override
            public void setCaptcha(HttpServletRequest request, HttpServletResponse response, String capText, Date capDate) {
                // no-op
            }
        };

        // default init(Config) is a no-op
        resolver.init((Config) null);
        // default init(String, String, long) is a no-op
        resolver.init("storeKey", "dateKey", 1000L);

        assertThat(resolver).isNotNull();
    }

}
