package com.google.code.kaptcha.spring.boot;


import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.spring.boot.servlet.KaptchaJakartaServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
@ConditionalOnClass({ Producer.class })
@EnableConfigurationProperties(KaptchaProperties.class)
/**
 * <p>Auto-configuration for Kaptcha integration.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class KaptchaAutoConfiguration {
	/**
	 * <p>Kaptcha resolver.</p>
	 * @param properties the properties
	 * @return the kaptcha resolver
	 */

	@Bean
	@ConditionalOnMissingBean(KaptchaResolver.class)
	public KaptchaResolver kaptchaResolver(KaptchaProperties properties) {
		
		KaptchaResolver kaptchaResolver = new SessionKaptchaResolver(); 
		// Initialization parameters
		kaptchaResolver.init(properties.getCaptchaStoreKey(), properties.getCaptchaDateStoreKey(), properties.getCaptchaTimeout());
		
		return kaptchaResolver;
	}
	
	// Captcha
	/**
	 * <p>Servlet registration bean.</p>
	 * @param properties the properties
	 * @param kaptchaResolver the kaptcha resolver
	 * @return the servlet registration bean< kaptcha jakarta servlet>
	 */
	@Bean
	@ConditionalOnMissingBean(name = "kaptchaServlet")
	public ServletRegistrationBean<KaptchaJakartaServlet> servletRegistrationBean(KaptchaProperties properties, KaptchaResolver kaptchaResolver) {

		ServletRegistrationBean<KaptchaJakartaServlet> registrationBean = new ServletRegistrationBean<KaptchaJakartaServlet>();
		
		KaptchaJakartaServlet kaptchaServlet = new KaptchaJakartaServlet(kaptchaResolver);

		registrationBean.setServlet(kaptchaServlet);
		
		// Default parameters
		registrationBean.addInitParameter(Constants.KAPTCHA_BORDER, "no");
		registrationBean.addInitParameter(Constants.KAPTCHA_BORDER_COLOR, "black");
		registrationBean.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
		registrationBean.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "5");
		registrationBean.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");

		if (!CollectionUtils.isEmpty(properties.getParameters())) {
			registrationBean.setInitParameters(properties.getParameters());
		}

		registrationBean.addUrlMappings(properties.getPattern());

		return registrationBean;
	}
	
}
