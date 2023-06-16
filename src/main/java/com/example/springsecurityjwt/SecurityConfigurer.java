package com.example.springsecurityjwt;


import com.example.UserService.CustomAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfigurer {
    @Autowired
     BCryptPasswordEncoder encoder;
    UserDetailsService userDetailsService;
    @Autowired
AuthenticationConfiguration authenticationConfiguration;
    public void configurer(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }
    @Bean
    public SecurityFilterChain configurer(HttpSecurity http) throws Exception{
        return http
                .csrf((csrf)-> csrf.disable())
                .authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.requestMatchers("/login").permitAll())
                .authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.anyRequest().authenticated())
                .addFilterAfter(new CustomAuthenticationFilter(setManager(authenticationConfiguration)), BasicAuthenticationFilter.class)
                .sessionManagement((sessionManagement)->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    AuthenticationManager setManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}