package com.mshz.webflux.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateBeanConfig {
    
    @Autowired
    AuthenticateClientHttpRequestInterceptor interceptor;
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        return restTemplate;
    }
}
