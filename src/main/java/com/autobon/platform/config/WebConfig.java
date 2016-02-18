package com.autobon.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Created by dave on 16/2/18.
 */
@Configuration
public class WebConfig {

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxUploadSize(12*1024*1024);
        resolver.setMaxUploadSizePerFile(2*1024*1024);
        resolver.setResolveLazily(true);
        return resolver;
    }
}
