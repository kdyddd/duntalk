package com.duntalk.global.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public RateLimiter neopleRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(4)
                .limitRefreshPeriod(Duration.ofMillis(10))
                .timeoutDuration(Duration.ofSeconds(10))
                .build();

        return RateLimiter.of("neopleApi", config);
    }
}
