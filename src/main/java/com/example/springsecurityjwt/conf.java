package com.example.springsecurityjwt;

import com.example.UserService.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class conf {
    @Bean
    UserService setUserService(){
        return new UserService();
    }
    @Bean
    PasswordEncoder setPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
