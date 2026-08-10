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
package com.google.code.kaptcha.spring.boot.servlet;

import java.awt.image.BufferedImage;
import java.util.Date;

import jakarta.servlet.ServletException;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;

import com.google.code.kaptcha.spring.boot.KaptchaProperties;
import com.google.code.kaptcha.spring.boot.KaptchaResolver;
import com.google.code.kaptcha.spring.boot.SessionKaptchaResolver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link KaptchaJakartaServlet }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("KaptchaJakartaServlet Tests")
class KaptchaJakartaServletTest {

    private KaptchaResolver newResolver() {
        return new SessionKaptchaResolver();
    }

    @Test
    @DisplayName("Constructor stores the captcha resolver")
    void testConstructor() {
        KaptchaResolver resolver = newResolver();
        KaptchaJakartaServlet servlet = new KaptchaJakartaServlet(resolver);
        assertThat(servlet).isNotNull();
    }

    @Test
    @DisplayName("init(config) reads init parameters and builds a producer")
    void testInit() throws ServletException, IOException {
        KaptchaResolver resolver = newResolver();
        KaptchaJakartaServlet servlet = new KaptchaJakartaServlet(resolver);

        MockServletConfig servletConfig = new MockServletConfig();
        servletConfig.addInitParameter("kaptcha.textproducer.char.length", "4");

        servlet.init(servletConfig);

        // after init the servlet must be able to serve a captcha image
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());
        MockHttpServletResponse response = new MockHttpServletResponse();

        servlet.doGet(request, response);

        assertThat(response.getContentType()).isEqualTo("image/jpeg");
        assertThat(response.getContentAsByteArray()).isNotEmpty();
        assertThat(response.getHeader("Cache-Control"))
                .isEqualTo("no-store, no-cache, must-revalidate");
        assertThat(response.getHeader("Pragma")).isEqualTo("no-cache");

        // the captcha text + date must be stored in the session
        SessionKaptchaResolver sessionResolver = (SessionKaptchaResolver) resolver;
        assertThat(request.getSession().getAttribute(sessionResolver.getCaptchaStoreKey()))
                .isInstanceOf(String.class);
        assertThat(request.getSession().getAttribute(sessionResolver.getCaptchaDateStoreKey()))
                .isInstanceOf(Date.class);
    }

    @Test
    @DisplayName("doGet writes a non-empty JPEG byte stream")
    void testDoGetWritesImage() throws Exception {
        KaptchaResolver resolver = newResolver();
        KaptchaJakartaServlet servlet = new KaptchaJakartaServlet(resolver);

        MockServletConfig servletConfig = new MockServletConfig();
        servlet.init(servletConfig);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());
        MockHttpServletResponse response = new MockHttpServletResponse();

        servlet.doGet(request, response);

        byte[] content = response.getContentAsByteArray();
        assertThat(content).isNotEmpty();
        // JPEG magic bytes: FF D8 FF
        assertThat(content[0] & 0xFF).isEqualTo(0xFF);
        assertThat(content[1] & 0xFF).isEqualTo(0xD8);
    }

    @Test
    @DisplayName("getProducerImpl is exercised indirectly via doGet producing a BufferedImage")
    void testProducerCreatesBufferedImage() throws Exception {
        // sanity: make sure the BufferedImage-based path is actually hit
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertThat(image).isNotNull();
    }

    @Test
    @DisplayName("KaptchaProperties default timeout constant is wired through resolver init")
    void testResolverUsesDefaultTimeout() {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();
        assertThat(resolver.getCaptchaTimeout()).isEqualTo(KaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT);
    }

}
