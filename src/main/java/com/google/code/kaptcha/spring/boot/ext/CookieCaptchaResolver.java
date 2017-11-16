/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
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
package com.google.code.kaptcha.spring.boot.ext;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import com.google.code.kaptcha.spring.boot.ext.util.ExtConfig;


public class CookieCaptchaResolver extends CookieGenerator implements CaptchaResolver {


	/**
	 * Name of the request attribute that holds the kaptcha name.
	 */
	public static final String KAPTCHA_REQUEST_ATTRIBUTE_NAME = CookieCaptchaResolver.class.getName() + ".KAPTCHA";

	public static final String DEFAULT_COOKIE_NAME = CookieCaptchaResolver.class.getName() + ".KAPTCHA";
	
	public static final String KAPTCHA_DATE_REQUEST_ATTRIBUTE_NAME = CookieCaptchaResolver.class.getName() + ".KAPTCHA_DATE";

	public static final String DEFAULT_COOKIE_DATE_NAME = CookieCaptchaResolver.class.getName() + ".KAPTCHA_DATE";

	private String cookieKeyValue = DEFAULT_COOKIE_NAME;
	private String cookieKeyDateValue = DEFAULT_COOKIE_DATE_NAME;
	
	@Override
	public void init(ExtConfig config ) {
		this.cookieKeyValue = config.getCookieKey(DEFAULT_COOKIE_NAME);
		this.cookieKeyDateValue = config.getCookieKey(DEFAULT_COOKIE_DATE_NAME);
	}
	
	@Override
	public boolean validCaptcha(HttpServletRequest request, String capText) {
		
		if(StringUtils.isEmpty(capText)){
			return false;
		}
		// Check request for preparsed or preset kaptcha.
		String requestCapText = (String) request.getAttribute(KAPTCHA_REQUEST_ATTRIBUTE_NAME);
		if (requestCapText != null) {
			return StringUtils.equalsIgnoreCase(requestCapText, capText);
		}

		// Retrieve cookie value from request.
		Cookie cookie = WebUtils.getCookie(request, this.cookieKeyValue);
		if (cookie != null) {
			String value = cookie.getValue();
			if (StringUtils.isNotEmpty(value)) {
				requestCapText = value;
			}
		}
		
		return StringUtils.equalsIgnoreCase(requestCapText, capText);
	}
	
	@Override
	public void setCaptcha(HttpServletRequest request, HttpServletResponse response, String capText, Date capDate) {
		if (StringUtils.isNotEmpty(capText)) {
			
			// Set request attribute and add cookie.
			request.setAttribute(KAPTCHA_REQUEST_ATTRIBUTE_NAME, capText);
			request.setAttribute(KAPTCHA_DATE_REQUEST_ATTRIBUTE_NAME, capDate);
			
			this.setCookieName(this.cookieKeyValue);
			addCookie(response, capText);
			this.setCookieName(this.cookieKeyDateValue);
			addCookie(response, capDate.toString());
			
		}
		else {
			// remove cookie.
			removeCookie(response);
		}
	}

}
