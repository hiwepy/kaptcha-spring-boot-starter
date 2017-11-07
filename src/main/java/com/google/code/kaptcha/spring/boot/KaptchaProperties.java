package com.google.code.kaptcha.spring.boot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = KaptchaProperties.PREFIX)
public class KaptchaProperties {

	public static final String PREFIX = "kaptcha";

	/**
	 * 详细参数参见：com.google.code.kaptcha.Constants
	 */
	private Map<String,String> parameters = new HashMap<String,String>();

	/** 验证码插件访问路径 **/
	private String pattern = "/kaptcha";
	
	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
}
