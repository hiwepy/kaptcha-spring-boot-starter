/*
 * Copyright (c) 2017, hiwepy (https://github.com/easy-4-java).
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

import com.google.code.kaptcha.spring.boot.exception.CaptchaIncorrectException;
import com.google.code.kaptcha.spring.boot.exception.CaptchaTimeoutException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import com.google.code.kaptcha.util.Config;

/**
 * <p>Resolver for session kaptcha resolver resolution.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class SessionKaptchaResolver implements KaptchaResolver {

	/**
	 * Name of the session attribute that holds the Kaptcha name. Only used
	 * internally by this implementation.
	 */
	public static final String CAPTCHA_SESSION_ATTRIBUTE_NAME = SessionKaptchaResolver.class.getName() + ".KAPTCHA";
	public static final String CAPTCHA_DATE_SESSION_ATTRIBUTE_NAME = SessionKaptchaResolver.class.getName() + ".KAPTCHA_DATE";
	
	/**
     * Captcha在Session中存储值的key
     */
	private String captchaStoreKey = CAPTCHA_SESSION_ATTRIBUTE_NAME;
	/**
     * Captcha创建时间在Session中存储值的key
     */
	private String captchaDateStoreKey = CAPTCHA_SESSION_ATTRIBUTE_NAME;
	/**
     * Captcha有效期；单位（毫秒），默认 60000
     */
	private long captchaTimeout = KaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT;
	/**
	 * <p>Init.</p>
	 * @param config the config
	 */
	
	@Override
	public void init(Config config ) {
		if(StringUtils.isNoneEmpty(captchaStoreKey)) {
			this.captchaStoreKey = config.getSessionKey();
		}
		if(StringUtils.isNoneEmpty(captchaDateStoreKey)) {
			this.captchaDateStoreKey = config.getSessionDate();
		}
	}
	/**
	 * <p>Init.</p>
	 * @param captchaStoreKey the captcha store key
	 * @param captchaDateStoreKey the captcha date store key
	 * @param captchaTimeout the captcha timeout
	 */
	
	@Override
	public void init(String captchaStoreKey, String captchaDateStoreKey, long captchaTimeout) {
		if(StringUtils.isNoneEmpty(captchaStoreKey)) {
			this.captchaStoreKey = captchaStoreKey;
		}
		if(StringUtils.isNoneEmpty(captchaDateStoreKey)) {
			this.captchaDateStoreKey = captchaDateStoreKey;
		}
		if(captchaTimeout > 0) {
			this.captchaTimeout = captchaTimeout;
		}
	}
	/**
	 * <p>Valid captcha.</p>
	 * @param request the request
	 * @param capText the cap text
	 * @return the boolean
	 */
	
	@Override
	public boolean validCaptcha(HttpServletRequest request, String capText)
			throws CaptchaIncorrectException, CaptchaTimeoutException {
		
		// Captcha无效
		if(StringUtils.isEmpty(capText)) {
			throw new CaptchaIncorrectException();
		}
		// 历史Captcha无效
		String sessionCapText = (String) WebUtils.getSessionAttribute(request, getCaptchaStoreKey());
		if(StringUtils.isEmpty(sessionCapText)) {
			throw new CaptchaIncorrectException();
		}
		// 检查Captcha是否过期
		Date sessionCapDate = (Date) WebUtils.getSessionAttribute(request, getCaptchaDateStoreKey());
		if(new Date().getTime() - sessionCapDate.getTime()  > getCaptchaTimeout()) {
			throw new CaptchaTimeoutException();
		}
		
		return StringUtils.equalsIgnoreCase(sessionCapText, capText);
	}
	/** Sets the captcha. */

	@Override
	public void setCaptcha(HttpServletRequest request, HttpServletResponse response, String capText, Date capDate) {
		
		// store the text in the session
		WebUtils.setSessionAttribute(request, getCaptchaStoreKey(), (StringUtils.isNotEmpty(capText) ? capText : null));

		// store the date in the session so that it can be compared
		// against to make sure someone hasn't taken too long to enter
		// their kaptcha
		WebUtils.setSessionAttribute(request, getCaptchaDateStoreKey(), (capDate != null ? capDate : new Date()) );

	}
	/** Gets the captcha store key. */

	public String getCaptchaStoreKey() {
		return captchaStoreKey;
	}
	/** Gets the captcha date store key. */
	
	public String getCaptchaDateStoreKey() {
		return captchaDateStoreKey;
	}
	/** Gets the captcha timeout. */
	
	public long getCaptchaTimeout() {
		return captchaTimeout;
	}

}
