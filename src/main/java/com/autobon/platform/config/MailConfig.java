package com.autobon.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by yuh on 2016/3/3.
 */
@Configuration
public class MailConfig {

    @Value("${com.autobon.mail.host}")
    private String host;

    @Value("${com.autobon.mail.username}")
    private String username;

    @Value("${com.autobon.mail.password}")
    private String password;

    @Bean
    public JavaMailSenderImpl javaMailService() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }

}
