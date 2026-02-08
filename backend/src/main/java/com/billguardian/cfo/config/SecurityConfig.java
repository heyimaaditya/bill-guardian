package com.billguardian.cfo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Required for stateless REST APIs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/statements/**").permitAll() // Allow our endpoints
                .requestMatchers("/ws-cfo/**").permitAll()        // Allow WebSockets
                .anyRequest().authenticated()
            );
            
        return http.build();
    }
}