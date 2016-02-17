package com.autobon.mobile.security;

import com.autobon.mobile.entity.Technician;
import com.autobon.mobile.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by dave on 16/2/16.
 */
public class TokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    private TechnicianService technicianService;

    public TokenAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        Authentication authentication = null;
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if (cookies != null) {
            cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("autoken")).findFirst().orElse(null);
        }
        String token = cookie != null ? cookie.getValue() : "";
        if (token.startsWith("technician:")) {
            int id = Technician.decodeToken(token);
            Technician technician = null;
            if (id > 0) technician = technicianService.get(id);
            if (technician != null) authentication = new TokenAuthentication(technician);
        }
        if (authentication == null) {
            GrantedAuthority authority = new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "ANONYMOUS";
                }
            };
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authority);
            UserDetails anonymous = new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return authorities;
                }

                @Override
                public String getPassword() {
                    return "";
                }

                @Override
                public String getUsername() {
                    return "Anonymous";
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
            authentication = new TokenAuthentication(anonymous);
        }
        return authentication;
    }
}
