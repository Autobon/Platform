package com.autobon.platform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/2/18.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private ServletContext context;

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxUploadSize(5*1024*1024);
        resolver.setResolveLazily(true);
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("/")
                .setCachePeriod(365*24*3600)
                .resourceChain(false)
                .addResolver(new PathResourceResolver() { // 添加对Angularjs中的html5Mode支持
                    @Override
                    protected Resource getResource(String path, Resource location) throws IOException {
                        Resource resource;
                        if (path.startsWith("uploads/")) {
                            resource = new ServletContextResource(context, "/" + path);
                        } else {
                            resource = new ClassPathResource("/static/" + path);
                            if (!resource.exists() && !Pattern.matches(
                                    ".*\\.(html|js|css|ico|jpg|png|gif|zip|rar|ttf|otf|eot|svg|woff|woff2)$", path)) {
                                resource = new ClassPathResource("/static/index.html");
                            }
                        }

                        return resource.exists() ? resource : null;
                    }
                });
    }
}
