package com.google.code.kaptcha.spring.boot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = KaptchaProperties.PREFIX)
public class KaptchaProperties {

	public static final String PREFIX = "kaptcha";
	public static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;
	
	/**
	 * 详细参数参见：com.google.code.kaptcha.Constants
	 */
	private Map<String,String> parameters = new HashMap<String,String>();

	/** 验证码插件访问路径 **/
	private String pattern = "/kaptcha";
	/**
     * 验证码缓存的key
     */
	private String captchaStoreKey;
	/**
     * 验证码创建时间缓存的key
     */
	private String captchaDateStoreKey;
	/**
     * 验证码有效期；单位（毫秒），默认 60000
     */
	private long captchaTimeout = DEFAULT_CAPTCHA_TIMEOUT;
	
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

	public String getCaptchaStoreKey() {
		return captchaStoreKey;
	}

	public void setCaptchaStoreKey(String captchaStoreKey) {
		this.captchaStoreKey = captchaStoreKey;
	}

	public String getCaptchaDateStoreKey() {
		return captchaDateStoreKey;
	}

	public void setCaptchaDateStoreKey(String captchaDateStoreKey) {
		this.captchaDateStoreKey = captchaDateStoreKey;
	}

	public long getCaptchaTimeout() {
		return captchaTimeout;
	}

	public void setCaptchaTimeout(long captchaTimeout) {
		this.captchaTimeout = captchaTimeout;
	}
	
}
