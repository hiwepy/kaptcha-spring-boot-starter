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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import com.google.code.kaptcha.spring.boot.servlet.KaptchaJakartaServlet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link KaptchaAutoConfiguration }}.
 *
 * <p>Verifies the auto-configuration activates under the expected conditions
 * and exposes its declared beans.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("KaptchaAutoConfiguration Tests")
class KaptchaAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner();

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        KaptchaAutoConfiguration configuration = new KaptchaAutoConfiguration();
        assertThat(configuration).isNotNull();
    }

    @Test
    @DisplayName("Loads configuration and registers kaptchaResolver + servlet registration bean")
    void testLoadsAndRegistersBeans() {
        runner.withUserConfiguration(KaptchaAutoConfiguration.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(KaptchaAutoConfiguration.class);
                    assertThat(context).hasSingleBean(KaptchaResolver.class);
                    assertThat(context).hasBean("servletRegistrationBean");

                    KaptchaResolver resolver = context.getBean(KaptchaResolver.class);
                    assertThat(resolver).isInstanceOf(SessionKaptchaResolver.class);

                    ServletRegistrationBean<?> registration = context.getBean(
                            "servletRegistrationBean", ServletRegistrationBean.class);
                    assertThat(registration.getServlet()).isInstanceOf(KaptchaJakartaServlet.class);

                    // default init parameters supplied by the auto-configuration
                    assertThat(registration.getInitParameters())
                            .containsEntry("kaptcha.border", "no")
                            .containsEntry("kaptcha.border.color", "black");
                });
    }

    @Test
    @DisplayName("Custom pattern and parameters are applied to the servlet registration bean")
    void testCustomParametersApplied() {
        runner.withUserConfiguration(KaptchaAutoConfiguration.class)
                .withPropertyValues(
                        "kaptcha.pattern=/my-captcha",
                        "kaptcha.parameters.kaptcha.textproducer.char.length=6")
                .run(context -> {
                    ServletRegistrationBean<?> registration = context.getBean(
                            "servletRegistrationBean", ServletRegistrationBean.class);
                    assertThat(registration.getUrlMappings()).contains("/my-captcha");
                    assertThat(registration.getInitParameters())
                            .containsEntry("kaptcha.textproducer.char.length", "6");
                });
    }

}
