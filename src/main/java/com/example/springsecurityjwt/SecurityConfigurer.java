package com.example.springsecurityjwt;


import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
@EnableWebSecurity
@Configuration
public class SecurityConfigurer {

    @Bean
    public SecurityFilterChain configurer(HttpSecurity http) throws Exception{
        return http
                .csrf((csrf)-> csrf.disable())
                .authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.anyRequest().permitAll())
                .sessionManagement((sessionManagement)->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}