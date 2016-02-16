package com.autobon.mobile.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by dave on 16/2/16.
 */
public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public AuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication oa = context.getAuthentication();
        if (oa == null || !oa.isAuthenticated()) {
            HttpSession session = request.getSession();
            String token = session.getAttribute("token").toString();
        }
        return null;
    }
}
