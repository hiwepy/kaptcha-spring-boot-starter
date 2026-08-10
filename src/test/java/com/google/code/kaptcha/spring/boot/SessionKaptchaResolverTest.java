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
import java.util.Properties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.google.code.kaptcha.spring.boot.exception.CaptchaIncorrectException;
import com.google.code.kaptcha.spring.boot.exception.CaptchaTimeoutException;
import com.google.code.kaptcha.util.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {{ @link SessionKaptchaResolver }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("SessionKaptchaResolver Tests")
class SessionKaptchaResolverTest {

    /** Helper: build a resolver whose store key and date-store key are distinct. */
    private SessionKaptchaResolver newResolverWithDistinctKeys() {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();
        // The default ctor leaves captchaDateStoreKey equal to captchaStoreKey (a known
        // quirk of the production code); use init to set distinct keys so the captcha text
        // and the captcha date can be stored independently within a single session.
        resolver.init("captcha.text", "captcha.date", KaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT);
        return resolver;
    }

    @Test
    @DisplayName("Default constructor exposes expected defaults")
    void testInstantiation() {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();
        assertThat(resolver).isNotNull();
        // captchaStoreKey default
        assertThat(resolver.getCaptchaStoreKey())
                .isEqualTo(SessionKaptchaResolver.CAPTCHA_SESSION_ATTRIBUTE_NAME);
        // captchaDateStoreKey default is the *captcha* session attribute name (production quirk)
        assertThat(resolver.getCaptchaDateStoreKey())
                .isEqualTo(SessionKaptchaResolver.CAPTCHA_SESSION_ATTRIBUTE_NAME);
        assertThat(resolver.getCaptchaTimeout()).isEqualTo(KaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT);
    }

    @Test
    @DisplayName("init(Config) applies session keys from config")
    void testInitWithConfig() {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();

        Properties props = new Properties();
        props.setProperty("kaptcha.session.key", "my-session-key");
        props.setProperty("kaptcha.session.date", "my-session-date");
        Config config = new Config(props);

        resolver.init(config);

        assertThat(resolver.getCaptchaStoreKey()).isEqualTo("my-session-key");
        assertThat(resolver.getCaptchaDateStoreKey()).isEqualTo("my-session-date");
    }

    @Test
    @DisplayName("init(Config) applies the Config's built-in session key defaults when unset")
    void testInitWithConfigAppliesConfigDefaults() {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();

        Config emptyConfig = new Config(new Properties());
        // The kaptcha Config provides non-empty default session key names, so init(Config)
        // will always assign them regardless of whether the caller set any properties.
        resolver.init(emptyConfig);

        assertThat(resolver.getCaptchaStoreKey()).isEqualTo(emptyConfig.getSessionKey());
        assertThat(resolver.getCaptchaDateStoreKey()).isEqualTo(emptyConfig.getSessionDate());
    }

    @Test
    @DisplayName("init(String,String,long) overrides keys and timeout only when non-empty / positive")
    void testInitWithStringLong() {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();

        // override everything
        resolver.init("store-key", "date-key", 5000L);
        assertThat(resolver.getCaptchaStoreKey()).isEqualTo("store-key");
        assertThat(resolver.getCaptchaDateStoreKey()).isEqualTo("date-key");
        assertThat(resolver.getCaptchaTimeout()).isEqualTo(5000L);

        // empty / non-positive values are ignored -> values unchanged
        resolver.init("", "", 0L);
        assertThat(resolver.getCaptchaStoreKey()).isEqualTo("store-key");
        assertThat(resolver.getCaptchaDateStoreKey()).isEqualTo("date-key");
        assertThat(resolver.getCaptchaTimeout()).isEqualTo(5000L);

        // null values are ignored as well (StringUtils.isNoneEmpty handles null)
        resolver.init(null, null, -1L);
        assertThat(resolver.getCaptchaStoreKey()).isEqualTo("store-key");
        assertThat(resolver.getCaptchaDateStoreKey()).isEqualTo("date-key");
        assertThat(resolver.getCaptchaTimeout()).isEqualTo(5000L);
    }

    @Test
    @DisplayName("setCaptcha stores captcha text and date into the session")
    void testSetCaptcha() {
        SessionKaptchaResolver resolver = newResolverWithDistinctKeys();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());
        HttpServletResponse response = new MockHttpServletResponse();

        // store non-empty captcha with explicit date
        Date capDate = new Date();
        resolver.setCaptcha(request, response, "ABCD", capDate);
        assertThat(request.getSession().getAttribute(resolver.getCaptchaStoreKey())).isEqualTo("ABCD");
        assertThat(request.getSession().getAttribute(resolver.getCaptchaDateStoreKey())).isEqualTo(capDate);

        // store empty captcha -> stored value becomes null, date falls back to now
        resolver.setCaptcha(request, response, "", null);
        assertThat(request.getSession().getAttribute(resolver.getCaptchaStoreKey())).isNull();
        assertThat(request.getSession().getAttribute(resolver.getCaptchaDateStoreKey())).isNotNull();
    }

    @Test
    @DisplayName("validCaptcha throws CaptchaIncorrectException when input captcha is blank")
    void testValidCaptchaBlankInput() {
        SessionKaptchaResolver resolver = newResolverWithDistinctKeys();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());

        assertThatThrownBy(() -> resolver.validCaptcha(request, ""))
                .isInstanceOf(CaptchaIncorrectException.class);

        assertThatThrownBy(() -> resolver.validCaptcha(request, null))
                .isInstanceOf(CaptchaIncorrectException.class);
    }

    @Test
    @DisplayName("validCaptcha throws CaptchaIncorrectException when session captcha is missing")
    void testValidCaptchaMissingSession() {
        SessionKaptchaResolver resolver = newResolverWithDistinctKeys();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());

        assertThatThrownBy(() -> resolver.validCaptcha(request, "ABCD"))
                .isInstanceOf(CaptchaIncorrectException.class);
    }

    @Test
    @DisplayName("validCaptcha throws CaptchaTimeoutException when captcha is expired")
    void testValidCaptchaExpired() {
        SessionKaptchaResolver resolver = newResolverWithDistinctKeys();
        resolver.init("captcha.text", "captcha.date", 1L); // 1ms timeout

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        // store an old captcha date that is older than the timeout
        Date oldDate = new Date(System.currentTimeMillis() - 10_000L);
        session.setAttribute(resolver.getCaptchaStoreKey(), "ABCD");
        session.setAttribute(resolver.getCaptchaDateStoreKey(), oldDate);

        assertThatThrownBy(() -> resolver.validCaptcha(request, "ABCD"))
                .isInstanceOf(CaptchaTimeoutException.class);
    }

    @Test
    @DisplayName("validCaptcha returns true when captcha matches (case-insensitive)")
    void testValidCaptchaMatches() throws Exception {
        SessionKaptchaResolver resolver = newResolverWithDistinctKeys();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        session.setAttribute(resolver.getCaptchaStoreKey(), "ABCD");
        session.setAttribute(resolver.getCaptchaDateStoreKey(), new Date());

        assertThat(resolver.validCaptcha(request, "abcd")).isTrue();
        assertThat(resolver.validCaptcha(request, "ABCD")).isTrue();
    }

    @Test
    @DisplayName("validCaptcha returns false when captcha does not match")
    void testValidCaptchaMismatch() throws Exception {
        SessionKaptchaResolver resolver = newResolverWithDistinctKeys();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        session.setAttribute(resolver.getCaptchaStoreKey(), "ABCD");
        session.setAttribute(resolver.getCaptchaDateStoreKey(), new Date());

        assertThat(resolver.validCaptcha(request, "WXYZ")).isFalse();
    }

}
