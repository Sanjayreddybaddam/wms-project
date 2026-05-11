package com.wms.wms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	    http.csrf(csrf -> csrf.disable())
	    	.cors(cors -> {})
	        .authorizeHttpRequests(auth -> auth

	            // Public
	            .requestMatchers("/auth/**").permitAll()

	            // ADMIN ONLY
	            .requestMatchers("/api/products/**").hasRole("ADMIN")
	            .requestMatchers("/api/warehouses/**").hasRole("ADMIN")

	            // OPERATOR + ADMIN
	            .requestMatchers("/api/orders/**").hasAnyRole("ADMIN", "OPERATOR")

	            // everything else
	            .anyRequest().authenticated()
	            
	            
	        )
	        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
