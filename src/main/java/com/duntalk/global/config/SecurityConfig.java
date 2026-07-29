package com.duntalk.global.config;

import com.duntalk.global.security.OAuthLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OAuthLoginSuccessHandler oauthLoginSuccessHandler
    ) throws Exception {

        http
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers(
                                "/members/signup",
                                "/members/character/equipment",
                                "/members/character/equipment/confirm"
                        )
                )
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/members/character/equipment",
                                        "/members/character/equipment/confirm"
                                )
                                .authenticated()
                                .anyRequest()
                                .permitAll()
                )
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(oauthLoginSuccessHandler)
                );

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}

