package com.autobon.mobile.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by dave on 16/2/16.
 */
public class TokenAuthentication implements Authentication {
    private boolean authenticated;
    private UserDetails userDetails;

    public TokenAuthentication(UserDetails userDetails) {
        this.userDetails = userDetails;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userDetails != null ? userDetails.getPassword() : null;
    }

    @Override
    public Object getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return userDetails != null ? userDetails.getUsername() : null;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userDetails != null ? userDetails.getUsername() : null;
    }
}
