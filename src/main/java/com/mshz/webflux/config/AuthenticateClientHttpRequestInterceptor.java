package com.mshz.webflux.config;

import java.io.IOException;

import com.mshz.security.SecurityUtils;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateClientHttpRequestInterceptor implements ClientHttpRequestInterceptor{

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException { 
        String token = SecurityUtils.getCurrentUserJWT().orElse("");
            request.getHeaders().add("Authorization","Bearer "+token);
        return execution.execute( request, body);
    }
    
}
