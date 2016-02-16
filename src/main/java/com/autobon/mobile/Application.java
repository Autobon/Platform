package com.autobon.mobile;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({com.autobon.mobile.MobileApiApplication.class})
public class Application {
    public static void main(String[] args){
        appCtx = SpringApplication.run(MobileApiApplication.class, args);
    }


    private static ConfigurableApplicationContext appCtx;
}
