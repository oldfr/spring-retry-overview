package com.example.springretryoverview.config;

import com.example.springretryoverview.listener.DefaultListenerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MainConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RetryTemplate getRetryTemplate() {
        RetryTemplate template =  RetryTemplate.builder()
                .maxAttempts(2)
                .fixedBackoff(1000)
                .retryOn(ResourceAccessException.class)
//                .withListener(new DefaultListenerSupport())
                .build();
        template.registerListener(new DefaultListenerSupport());
        return template;
    }
}
