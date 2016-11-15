package com.autobon.platform.config;

import com.autobon.platform.security.TokenAuthenticationProcessingFilter;
import com.autobon.platform.security.TokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
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
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
                new RegexRequestMatcher("/api/(mobile|web)/.+", null),
                new RegexRequestMatcher("/api/(mobile|web)/[^/]+/(login|register|resetPassword).*", null));
        filter.setAuthenticationManager(authenticationManagerBean());
        http.addFilterBefore(filter, BasicAuthenticationFilter.class);

        http.authorizeRequests().antMatchers(
                "/api/web/admin/login",
                "/api/web/admin/logout",
                "/api/mobile/*/login",
                "/api/mobile/*/register",
                "/api/mobile/*/merchant/login",
                "/api/mobile/*/merchant/register",
                "/api/mobile/*/resetPassword").permitAll()
            .and().authorizeRequests().antMatchers("/api/mobile/technician/**").hasAuthority("TECHNICIAN")
            .and().authorizeRequests().antMatchers("/api/web/**").hasAuthority("STAFF")
            .and().authorizeRequests().antMatchers("/api/mobile/coop/**").hasAuthority("COOPERATOR");

        RequestMatcher apiRequestMatcher = new AntPathRequestMatcher("/api/**");
        HeaderWriter headerWriter = new DelegatingRequestMatcherHeaderWriter(apiRequestMatcher,
                                                new CacheControlHeadersWriter());
        // 由于
        // 1. springMVC对已有缓存控制设置的请求响应,无法再次更改缓存时间.
        // 2. spring security对所有启用缓存控制的请求默认添加了no-cache设置,即缓存时间为0
        // 所以这里默认禁用cache control,只对api请求启用,而spring security自动将启用缓存的请求的缓存时间更改为no-cache.
        // 而静态文件没有启用缓存控制,spring security不会添加缓存设置.在webconfig中对静态资源添加的缓存控制设定就可以生效.
        http.headers().cacheControl().disable().addHeaderWriter(headerWriter)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new TokenAuthenticationProvider());
    }
}
