package com.payhere.accountbook.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.payhere.accountbook.domain.member.repository.MemberRepository;
import com.payhere.accountbook.global.security.token.TokenService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final TokenService tokenService;
	private final MemberRepository memberRepository;

	@Value("${app.domain}")
	private String domain;

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.rememberMe(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(
				sessionManagement -> sessionManagement
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeRequests(
				authorizeRequests -> authorizeRequests
					.antMatchers("/**")
					.permitAll()
					.anyRequest().authenticated()
			)
			.addFilterBefore(
				new JwtAuthenticationFilter(tokenService, memberRepository),
				UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	SimpleUrlAuthenticationFailureHandler failureHandler() {
		return new SimpleUrlAuthenticationFailureHandler(domain);
	}

}