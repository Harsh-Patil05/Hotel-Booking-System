package com.hoteldev.HarshHotel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
public class AppConfig {
    @Bean
    public CachingUserDetailsService cachingUserDetailsService(UserDetailsService delegate) {
        return new CachingUserDetailsService(delegate);
    }

}
