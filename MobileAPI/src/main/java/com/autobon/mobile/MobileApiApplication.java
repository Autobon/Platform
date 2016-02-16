package com.autobon.mobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by dave on 16/2/12.
 */
@SpringBootApplication(scanBasePackageClasses = MobileApiApplication.class)
@EntityScan(basePackageClasses = MobileApiApplication.class)
@EnableJpaRepositories(basePackageClasses = MobileApiApplication.class)
@EnableWebMvc
@EnableTransactionManagement
public class MobileApiApplication {
    public static void main(String[] args){
        SpringApplication.run(MobileApiApplication.class, args);
    }
}
