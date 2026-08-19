package com.google.code.kaptcha.spring.boot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = KaptchaProperties.PREFIX)
/**
 * <p>Configuration properties for Kaptcha.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class KaptchaProperties {

	public static final String PREFIX = "kaptcha";
	public static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;
	
	/**
	 * 详细参数参见：com.google.code.kaptcha.Constants
	 */
	private Map<String,String> parameters = new HashMap<String,String>();

	/** Captcha插件访问路径 **/
	private String pattern = "/kaptcha";
	/**
     * Captcha缓存的key
     */
	private String captchaStoreKey;
	/**
     * Captcha创建时间缓存的key
     */
	private String captchaDateStoreKey;
	/**
     * Captcha有效期；单位（毫秒），默认 60000
     */
	private long captchaTimeout = DEFAULT_CAPTCHA_TIMEOUT;
	/** Gets the parameters. */
	
	public Map<String, String> getParameters() {
		return parameters;
	}
	/** Sets the parameters. */

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	/** Gets the pattern. */

	public String getPattern() {
		return pattern;
	}
	/** Sets the pattern. */

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/** Gets the captcha store key. */

	public String getCaptchaStoreKey() {
		return captchaStoreKey;
	}
	/** Sets the captcha store key. */

	public void setCaptchaStoreKey(String captchaStoreKey) {
		this.captchaStoreKey = captchaStoreKey;
	}
	/** Gets the captcha date store key. */

	public String getCaptchaDateStoreKey() {
		return captchaDateStoreKey;
	}
	/** Sets the captcha date store key. */

	public void setCaptchaDateStoreKey(String captchaDateStoreKey) {
		this.captchaDateStoreKey = captchaDateStoreKey;
	}
	/** Gets the captcha timeout. */

	public long getCaptchaTimeout() {
		return captchaTimeout;
	}
	/** Sets the captcha timeout. */

	public void setCaptchaTimeout(long captchaTimeout) {
		this.captchaTimeout = captchaTimeout;
	}
	
}
