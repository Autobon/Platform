package com.autobon;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class Application {
    public static void main(String[] args){
        appCtx = SpringApplication.run(Application.class, args);
    }


    private static ConfigurableApplicationContext appCtx;
}
