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
package com.google.code.kaptcha.spring.boot.ext.util;

import java.util.Properties;

import com.google.code.kaptcha.spring.boot.ext.Constants;
import com.google.code.kaptcha.util.Config;

public class ExtConfig extends Config {

	public ExtConfig(Properties properties) {
		super(properties);
	}
	
	/**
	 * Allows one to override the key name which is stored in the users Cookie.
	 */
	public String getCookieKey(String def) {
		return this.getProperties().getProperty(Constants.KAPTCHA_COOKIE_CONFIG_KEY, def);
	}

	/**
	 * Allows one to override the date name which is stored in the users Cookie.
	 */
	public String getCookieDate(String def)
	{
		return this.getProperties().getProperty(Constants.KAPTCHA_COOKIE_CONFIG_DATE, def);
	}

}
