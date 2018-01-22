# spring-boot-starter-kaptcha


### 说明

 > 基于 Kaptcha 验证码开源项目实现的验证码 Spring Boot Starter 实现

1. KaptchaServlet 自动注册
2. KaptchaServlet 逻辑扩展实现，增加 CaptchaResolver 接口定义，实现基于Session、Cookie的默认验证码存储和解析实现
3. 支持自定义CaptchaResolver实现，扩展基于外部存储，如 Database、Cache的验证码存储和解析

### Maven

``` xml
<dependency>
	<groupId>${project.groupId}</groupId>
	<artifactId>spring-boot-starter-kaptcha</artifactId>
	<version>${project.version}</version>
</dependency>
```

### Sample ：  CaptchaResolver Extends

[https://github.com/vindell/spring-boot-starter-samples/tree/master/spring-boot-sample-kaptcha](https://github.com/vindell/spring-boot-starter-samples/tree/master/spring-boot-sample-kaptcha "spring-boot-sample-kaptcha")
