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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import com.google.code.kaptcha.spring.boot.ext.util.ExtConfig;

public class SessionCaptchaResolver implements CaptchaResolver {

	/**
	 * Name of the session attribute that holds the Kaptcha name. Only used
	 * internally by this implementation.
	 */
	public static final String KAPTCHA_SESSION_ATTRIBUTE_NAME = SessionCaptchaResolver.class.getName() + ".KAPTCHA";
	public static final String KAPTCHA_DATE_SESSION_ATTRIBUTE_NAME = SessionCaptchaResolver.class.getName() + ".KAPTCHA_DATE";

	private String sessionKeyValue = KAPTCHA_SESSION_ATTRIBUTE_NAME;
	private String sessionKeyDateValue = KAPTCHA_DATE_SESSION_ATTRIBUTE_NAME;
	
	@Override
	public void init(ExtConfig config ) {
		this.sessionKeyValue = config.getSessionKey();
		this.sessionKeyDateValue = config.getSessionDate();
	}
	
	@Override
	public boolean validCaptcha(HttpServletRequest request, String capText) {
		if(StringUtils.isEmpty(capText)){
			return false;
		}
		String sessionCapText = (String) WebUtils.getSessionAttribute(request, this.sessionKeyValue);
		//String sessionCapDate = (String) WebUtils.getSessionAttribute(request, this.sessionKeyDateValue);
		if (sessionCapText != null) {
			return StringUtils.equalsIgnoreCase(sessionCapText, capText);
		}
		return false;
	}

	@Override
	public void setCaptcha(HttpServletRequest request, HttpServletResponse response, String capText, Date capDate) {
		
		// store the text in the session
		WebUtils.setSessionAttribute(request, sessionKeyValue, (StringUtils.isNotEmpty(capText) ? capText : null));

		// store the date in the session so that it can be compared
		// against to make sure someone hasn't taken too long to enter
		// their kaptcha
		WebUtils.setSessionAttribute(request, sessionKeyDateValue, (capDate != null ? capDate : new Date()) );

	}


}
