package com.autobon.technician;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.autobon.technician"})
@EntityScan(basePackages = {"com.autobon.technician"})
@EnableJpaRepositories(basePackages = {"com.autobon.technician"})
@EnableTransactionManagement
public class Application {}
