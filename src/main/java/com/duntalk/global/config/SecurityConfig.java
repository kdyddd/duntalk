package com.duntalk.global.config;

import com.duntalk.global.config.security.oauth.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomOidcUserService customOidcUserService,
            @Value("${app.frontend-url}") String frontendUrl
    ) throws Exception {

        http
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().permitAll()
                )
                .oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfo ->
                                userInfo.oidcUserService(customOidcUserService)
                        )
                                .defaultSuccessUrl(frontendUrl, true)
                );

        return http.build();
    }
}
