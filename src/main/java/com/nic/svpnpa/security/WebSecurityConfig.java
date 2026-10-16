package com.nic.svpnpa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import com.nic.svpnpa.security.jwt.AuthTokenFilter;
import com.nic.svpnpa.security.services.UserDetailsServiceImpl;
import com.nic.svpnpa.security.jwt.AuthEntryPointJwt;
import com.nic.svpnpa.util.CaptchaValidationFilter;
import com.nic.svpnpa.util.CustomAccessDeniedHandler;
import com.nic.svpnpa.util.CustomAuthenticationFailureHandler;
import com.nic.svpnpa.util.CustomAuthenticationSuccessHandler;

import com.nic.svpnpa.util.CustomLogoutSuccessHandler;
import com.nic.svpnpa.util.SameSiteCookieFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private CaptchaValidationFilter captchaValidationFilter;
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

	@Autowired
	CustomLogoutSuccessHandler customLogoutSuccessHandler;

	@Bean
	SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	ConcurrentSessionFilter concurrentSessionFilter() {
		return new ConcurrentSessionFilter(sessionRegistry(),
				new SimpleRedirectSessionInformationExpiredStrategy("/getsignin?expired"));
	}

	@Bean
	AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	HttpSessionEventPublisher httpSessionEventPublisher() {

		return new HttpSessionEventPublisher();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setSameSite("Strict"); // or "Lax"
		serializer.setUseSecureCookie(true);
		return serializer;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("https://fonts.googleapis.com");
		configuration.addAllowedOrigin("https://code.responsivevoice.org");
		configuration.addAllowedOrigin("https://api.mymemory.translated.net");
		configuration.addAllowedOrigin("https://panini.ai.nic.in");
		 
		
//		configuration.addAllowedOrigin("https://cke4.ckeditor.com/ckeditor4-secure-version/versions.json");
//		configuration.addAllowedOrigin("https://ckeditor.com/ckeditor-4-support");
//		configuration.addAllowedOrigin("https://pdf-converter.cke-cs.com/");
//		configuration.addAllowedOrigin("https://svc.webspellchecker.net/spellcheck31/wscbundle/wscbundle.js");
		
		
		
		
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	/*
	 * @Bean public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
	 * ConcurrentSessionControlAuthenticationStrategy strategy = new
	 * ConcurrentSessionControlAuthenticationStrategy( sessionRegistry());
	 * strategy.setMaximumSessions(1); // Set the maximum number of sessions to 1
	 * strategy.setExceptionIfMaximumExceeded(true); // Prevent new login when max
	 * sessions reached return strategy; }
	 */

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		final String[] staticResources = {"/", "/forgot-password", "/forgot-password", "/index", "/signup",
				"/logout", "/static/**", "/registration", "/registration/**", "/signup",
				"/reset-password/**", "/updateCookie", "/registration", "/home", "/index", "/Preview/**", "/getsignin", "/getPublicForm", "/insertPublicFormData", "/getForm", "/generateOtp","/validateOtp","/translateContent","/translation",
				"/getsignin", "/Preview/**", "/customerror", "/getCaptcha","/userReport",
				"/getCaptcha", "/captcha/**","/getDynamicForm/**","/getDynamicForm/**", "/captcha/**", "/universalSearch/","/universalSearch", "/static/assets/ckeditor_4.22.1/**",
				"/checkGalleryFolderUsage", "/deleteGalleryFolder", "/galleryFolderManager" , "/lastUpdated", "/getDbimResources",
				"/navtabs", "/getNavTabEligibleObjects", "/getChildObjectPreview"};

		


		httpSecurity
				.headers(headers -> headers.permissionsPolicy(permissions -> permissions.policy("geolocation=(self)")));

		httpSecurity.sessionManagement(session -> session.maximumSessions(1).maxSessionsPreventsLogin(true) // Prevent
// new login
// when max 
// sessions
// reached
				.expiredUrl("/getsignin?expired").sessionRegistry(sessionRegistry()) // Use the
// sessionRegistry bean
// here
				.and().sessionFixation().newSession());

// /*
// * httpSecurity .sessionManagement().invalidSessionUrl("/getsignin?expired")
// * .sessionFixation().migrateSession().sessionCreationPolicy(
// * SessionCreationPolicy.IF_REQUIRED)
// * .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
// * .maximumSessions(1) // Example configuration for maximum sessions
// * .maxSessionsPreventsLogin(true) // Example configuration for session handling
// * .expiredUrl("/getsignin?expired") .sessionRegistry(sessionRegistry()); //
// * Using the sessionRegistry bean here
// */
//    httpSecurity.requiresChannel(channel -> channel.anyRequest().requiresSecure());

		httpSecurity.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

		httpSecurity.headers(headers -> headers.cacheControl(cache -> cache.disable()));

		httpSecurity.headers(headers -> headers.contentTypeOptions(contentTypeOptions -> contentTypeOptions.disable()));

		httpSecurity.headers(headers -> headers.httpStrictTransportSecurity(
				hsts -> hsts.includeSubDomains(true).preload(true).maxAgeInSeconds(31536000)));

//    httpSecurity.headers(headers ->
//     headers
//     .xssProtection().and().contentSecurityPolicy("script-src 'self' 'unsafe-inline' https://code.responsivevoice.org;") // Specify your CSP directives here
//    );

		httpSecurity.headers(headers -> headers
				.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)) // Enable
// XSS
// protection
// and
// mode
// block
				.contentSecurityPolicy("script-src 'self' 'unsafe-inline' https://code.responsivevoice.org;")); // Specify
// your
// CSP
// directives
// here

		httpSecurity.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())).cors(); // Enable
//		httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/save"));																													// CSRF
		httpSecurity.headers(headers -> headers.addHeaderWriter(createXFrameOptionsHeader())
				.addHeaderWriter(createContentSecurityPolicyHeader()).addHeaderWriter(createHSTSHeader())
				.addHeaderWriter(createXSSProtectionHeader()).addHeaderWriter(createReferrerPolicyHeader())
				.addHeaderWriter(createXContentOptionsHeader())
				.contentTypeOptions(contentTypeOptions -> contentTypeOptions.disable()));

		httpSecurity.authorizeHttpRequests().requestMatchers(HttpMethod.OPTIONS, "/**").denyAll();
		httpSecurity.authorizeHttpRequests().requestMatchers(HttpMethod.PUT, "/**").denyAll();
		httpSecurity.authorizeHttpRequests().requestMatchers(HttpMethod.DELETE, "/**").denyAll();
		httpSecurity.authorizeHttpRequests().requestMatchers(HttpMethod.PATCH, "/**").denyAll();
		httpSecurity.authorizeHttpRequests().requestMatchers(HttpMethod.HEAD, "/**").denyAll();
		httpSecurity.authorizeHttpRequests().requestMatchers(HttpMethod.TRACE, "/**").denyAll();

		httpSecurity.authorizeHttpRequests().requestMatchers(staticResources).permitAll()
				.requestMatchers(HttpMethod.GET, "/**").authenticated().requestMatchers(HttpMethod.POST, "/**")
				.authenticated().anyRequest().denyAll();

		httpSecurity
				.formLogin(login -> login.loginPage("/loginSubmit").successHandler(customAuthenticationSuccessHandler)
						.failureHandler(customAuthenticationFailureHandler).isCustomLoginPage());

		httpSecurity.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler)
				.accessDeniedHandler(new CustomAccessDeniedHandler()));

		httpSecurity.logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(customLogoutSuccessHandler)
				.invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll());
		/*
		 * httpSecurity .logout((logout) -> logout .addLogoutHandler(new
		 * HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES))) );
		 */
		httpSecurity.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		httpSecurity.addFilterBefore(captchaValidationFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	/*
	 * @Bean CsrfTokenRepository customCsrfTokenRepository() { // create a
	 * CookieCsrfTokenRepository instance CookieCsrfTokenRepository repository =
	 * CookieCsrfTokenRepository.withHttpOnlyFalse(); // set the cookie name,
	 * parameter name, and header name as you want
	 *
	 * repository.setCookieName("XSRF-TOKEN"); repository.setParameterName("_csrf");
	 * repository.setHeaderName("X-XSRF-TOKEN"); // register a customizer to set the
	 * cookie attributes as you want repository.setCookieHttpOnly(true);
	 * repository.setSecure(true);
	 *
	 * // return the repository instance return repository; }
	 */

	@Bean
	FilterRegistrationBean<SameSiteCookieFilter> sameSiteFilter() {
		FilterRegistrationBean<SameSiteCookieFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new SameSiteCookieFilter());
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	private HeaderWriter createXSSProtectionHeader() {
		return new StaticHeadersWriter("X-XSS-Protection", "1; mode=block");
	}

	private HeaderWriter createXFrameOptionsHeader() {
		return new StaticHeadersWriter("X-Frame-Options", "DENY", "ALLOW-FROM https://www.youtube.com/");
	}

	private HeaderWriter createReferrerPolicyHeader() {
		return new StaticHeadersWriter("Referrer-Policy", "strict-origin-when-cross-origin");
	}

	private HeaderWriter createXContentOptionsHeader() {
		return new StaticHeadersWriter("X-Content-Type-Options", "nosniff");
	}

	private HeaderWriter createContentSecurityPolicyHeader() {
		String policy = "default-src 'self'; "
				+ "frame-src 'self' https://www.youtube.com/ https://youtube.com/ https://platform.twitter.com/ https://syndication.twitter.com/ https://www.facebook.com/ https://web.facebook.com/ https://www.instagram.com/; connect-src 'self' https://api.mymemory.translated.net https://syndication.twitter.com https://cdn.syndication.twimg.com; "
				+ "script-src 'self' 'unsafe-inline' https://code.responsivevoice.org https://platform.twitter.com https://cdn.syndication.twimg.com; "
				+ "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com/; "
				+ "img-src 'self' data: https://www.svpnpa.gov.in https://www.ciuvo.com https://*.twimg.com https://*.cdninstagram.com https://*.facebook.com https://*.fbcdn.net; "
				+ "font-src 'self' https://fonts.gstatic.com/; "
				+ "frame-ancestors 'self' http://10.160.1.155:8080/; base-uri 'self';"
				+ "x-content-type-options nosniff"; // Adding the X-Content-Type header here
		return new StaticHeadersWriter("Content-Security-Policy", policy);
	}

	
	private HeaderWriter createHSTSHeader() {
		return new StaticHeadersWriter("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
	}

	/*
	 * @Bean public SessionManagementFilter sessionManagementFilter() throws
	 * Exception { return new SessionManagementFilter(securityContextRepository(),
	 * sessionAuthenticationStrategy()); }
	 */

	/*
	 * @Bean public SessionManagementFilter sessionManagementFilter() {
	 * SessionManagementFilter filter = new
	 * SessionManagementFilter(securityContextRepository());
	 * filter.setSessionAuthenticationStrategy(new
	 * ConcurrentSessionControlAuthenticationStrategy(sessionRegistry())); return
	 * filter; }
	 */

	/*
	 * @Bean public SessionManagementFilter sessionManagementFilter() {
	 * SessionManagementFilter filter = new
	 * SessionManagementFilter(securityContextRepository());
	 * filter.setInvalidSessionStrategy(new
	 * SimpleRedirectInvalidSessionStrategy("/login?expired")); return filter; }
	 */
	@Bean
	SecurityContextRepository securityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}

	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	GrantedAuthoritiesMapper authoritiesMapper() {
		SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
		authorityMapper.setConvertToUpperCase(true); // Optionally convert roles to uppercase
		return authorityMapper;
	}
}