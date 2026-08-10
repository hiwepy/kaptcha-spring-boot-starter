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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link KaptchaProperties }}.
 *
 * <p>Verifies default values, getters/setters and POJO contract.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("KaptchaProperties Tests")
class KaptchaPropertiesTest {

    @Test
    @DisplayName("Default constructor creates instance with expected defaults")
    void testDefaults() {
        KaptchaProperties props = new KaptchaProperties();

        assertThat(props.getPattern()).isEqualTo("/kaptcha");
        assertThat(props.getCaptchaTimeout()).isEqualTo(KaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT);
        assertThat(props.getParameters()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Getters and setters round-trip all fields")
    void testGettersAndSetters() {
        KaptchaProperties props = new KaptchaProperties();

        Map<String, String> params = new HashMap<>();
        params.put("kaptcha.textproducer.char.length", "6");
        props.setParameters(params);
        assertThat(props.getParameters()).isSameAs(params);

        props.setPattern("/custom-kaptcha");
        assertThat(props.getPattern()).isEqualTo("/custom-kaptcha");

        props.setCaptchaStoreKey("store-key");
        assertThat(props.getCaptchaStoreKey()).isEqualTo("store-key");

        props.setCaptchaDateStoreKey("date-key");
        assertThat(props.getCaptchaDateStoreKey()).isEqualTo("date-key");

        props.setCaptchaTimeout(30_000L);
        assertThat(props.getCaptchaTimeout()).isEqualTo(30_000L);
    }

    @Test
    @DisplayName("Public constants have expected values")
    void testConstants() {
        assertThat(KaptchaProperties.PREFIX).isEqualTo("kaptcha");
        assertThat(KaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT).isEqualTo(60_000L);
    }

}
