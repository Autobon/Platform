package com.autobon.platform.security;

import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.staff.entity.Staff;
import com.autobon.staff.service.StaffService;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
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
import java.util.Date;

/**
 * Created by dave on 16/2/16.
 */
public class TokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private CooperatorService cooperatorService;

    public TokenAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
        this.setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if (cookies != null) {
            cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("autoken")).findFirst().orElse(null);
        }
        String token = cookie != null ? cookie.getValue() : "";
        UserDetails user = null;
        if (token.startsWith("technician:")) {
            int id = Technician.decodeToken(token);
            if (id > 0) {
                user = technicianService.get(id);
                if (user != null) {
                    Technician technician = (Technician) user;
                    technician.setLastLoginAt(new Date());
                    technician.setLastLoginIp(request.getRemoteAddr());
                    technicianService.save(technician);
                }
            }
        }else if (token.startsWith("cooperator:")) {
            int id = Cooperator.decodeToken(token);
            if (id > 0) user = cooperatorService.get(id);
        } else if (token.startsWith("staff:")) {
            int id = Staff.decodeToken(token);
            if (id > 0) user = staffService.get(id);
        } else {
            GrantedAuthority authority = new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "ANONYMOUS";
                }
            };
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authority);
            user = new UserDetails() {
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
        }

        TokenAuthentication authentication = new TokenAuthentication(user);
        request.setAttribute("user", user);
        return this.getAuthenticationManager().authenticate(authentication);
    }
}
