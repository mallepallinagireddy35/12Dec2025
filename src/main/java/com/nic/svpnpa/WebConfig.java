package com.nic.svpnpa;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${galleryAbsolutePath:}")
	private String galleryAbsolutePath;

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.ENGLISH);
		return resolver;
	}

	@Override
	public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
		// Custom language handling in controllers for 'E'/'en' and 'H'/'hi'
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		registry.viewResolver(resolver);
	}

	@Bean
	public ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		return engine;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration reg = 
			registry.addResourceHandler("/static/**", "/templates/**");
		if (galleryAbsolutePath != null && !galleryAbsolutePath.trim().isEmpty()) {
			String normalized = galleryAbsolutePath.replace('\\', '/');
			if (!normalized.endsWith("/")) {
				normalized += "/";
			}
			reg.addResourceLocations("file:" + normalized);
		}
		reg.addResourceLocations("file:D:/Kiranmai/SVPNPA/SVPNPA_Project/12Dec2025/src/main/resources/static/");
		reg.addResourceLocations("file:D:/Kiranmai/SVPNPA/25Nov2024/25November2024/src/main/resources/static/");
		reg.addResourceLocations("classpath:/static/");
		reg.addResourceLocations("classpath:/templates/");
	}
	
	/*
	 * @Bean public SessionDestroyedListener sessionDestroyedListener() { return new
	 * SessionDestroyedListener(); }
	 */

}
