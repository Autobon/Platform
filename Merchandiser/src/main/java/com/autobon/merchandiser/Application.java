package com.autobon.merchandiser;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.autobon.merchandiser"})
@EntityScan(basePackages = {"com.autobon.merchandiser"})
@EnableJpaRepositories(basePackages = {"com.autobon.merchandiser"})
@EnableTransactionManagement
public class Application {}
