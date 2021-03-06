package com.bairock.iot.hamaCenter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigurerAdapter implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/devImg/**").addResourceLocations("file:C:/dafa/inteldev-img/");
		registry.addResourceHandler("/download/**").addResourceLocations("file:C:/dafa/download/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
}
