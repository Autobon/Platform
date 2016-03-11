package com.autobon.platform.config;

import com.autobon.platform.security.TokenAuthenticationProcessingFilter;
import com.autobon.platform.security.TokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by dave on 16/2/16.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        TokenAuthenticationProcessingFilter filter = new TokenAuthenticationProcessingFilter(applicationContext,
                new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManagerBean());
        http.addFilterBefore(filter, BasicAuthenticationFilter.class);

        http.authorizeRequests().antMatchers(
                "/api/mobile/*/login",
                "/api/mobile/*/register",
                "/api/mobile/*/resetPassword").permitAll()
            .and().authorizeRequests().antMatchers("/api/mobile/technician/**")
                .hasAuthority("TECHNICIAN")
            .and().authorizeRequests().antMatchers("/api/web/**")
                .hasAuthority("STAFF")
            .and().authorizeRequests().antMatchers("/api/mobile/coop/**")
                .hasAuthority("COOPERATOR");

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new TokenAuthenticationProvider());
    }
}
