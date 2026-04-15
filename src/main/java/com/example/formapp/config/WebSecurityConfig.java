package com.example.formapp.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.formapp.exception.AuthorizationException;
import com.example.formapp.filter.JwtAuthenticationFilter;
import com.example.formapp.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final SecurityConfig securityConfig;

	private final HandlerExceptionResolver handlerExceptionResolver;

	private final AuthService authService;

	@Bean
	@Order(1)
	SecurityFilterChain loginAndHealthChain(HttpSecurity http) throws Exception {
		log.info("loginAndHealthChain");

		http.securityMatcher("/api/v1/auth/login", "/api/health/check").csrf(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().permitAll())
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain jwtChain(HttpSecurity http) throws AuthorizationException, Exception {
		log.info("jwtChain");
		http.securityMatcher("/api/**").cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated())
				.addFilterBefore(new JwtAuthenticationFilter(authService, handlerExceptionResolver),
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(3)
	public SecurityFilterChain guardChain(HttpSecurity http) throws AuthorizationException, Exception {
		log.info("guardChain");
		http.securityMatcher("/**").authorizeHttpRequests(auth -> auth.anyRequest().denyAll());

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		return request -> {
			CorsConfiguration config = new CorsConfiguration();

			config.setAllowedMethods(Arrays.asList(securityConfig.getAllowedMethod()));
			config.setAllowedOrigins(Arrays.asList(securityConfig.getAllowedOrigin()));
			config.setAllowedHeaders(List.of("*"));

			return config;
		};
	}
}
