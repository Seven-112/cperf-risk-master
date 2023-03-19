package com.mshz.security.jwt;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class SamAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private final Long userId;
    
    public SamAuthenticationToken(Object principal, Object credentials, Long userId) {
        super(principal, credentials);
        this.userId = userId;
    }
    
    public SamAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(principal, credentials, authorities);
        this.userId = userId;
    }
    
    public Long getUserId() {
        return userId;
    }
}
