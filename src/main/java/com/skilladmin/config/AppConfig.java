package com.skilladmin.config;


import com.phonepe.sdk.pg.common.PhonePeClient; // Assuming a factory class exists

import com.phonepe.sdk.pg.common.PhonePeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.skilladmin.serviceImpl.UserServiceImpl;
import org.springframework.web.client.RestTemplate;


@Configuration
public class  AppConfig {

	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private Jwtvalidator jwtvalidator;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(authorize -> authorize
	            .requestMatchers("/api/**").authenticated()
	            .requestMatchers("/admin/**").hasRole("ADMIN")
					.requestMatchers("/newskill/tutor/**").authenticated()
					.requestMatchers("/newskill/auth/**").hasAuthority("isTrainee")// Require authentication for /newskill/api/** paths
	            .anyRequest().permitAll()  // Permit all other requests
	        )
	        .authenticationProvider(authenticationProvider())
	        .addFilterBefore(jwtvalidator, UsernamePasswordAuthenticationFilter.class)
	        .csrf(csrf -> csrf.disable());

	    return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider =  new  DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userService);;
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();



	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


}
