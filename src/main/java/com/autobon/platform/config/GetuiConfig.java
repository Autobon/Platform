package com.autobon.platform.config;

import com.autobon.getui.PushService;
import com.autobon.getui.utils.GtConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by dave on 16/2/28.
 */
@Configuration
public class GetuiConfig {
    @Value("${com.autobon.getui.a.host}")
    private String hostA;
    @Value("${com.autobon.getui.a.appId}")
    private String appIdA;
    @Value("${com.autobon.getui.a.appKey}")
    private String appKeyA;
    @Value("${com.autobon.getui.a.appSecret}")
    private String appSecretA;
    @Value("${com.autobon.getui.a.masterSecret}")
    private String masterSecretA;

    @Bean(name = "PushServiceA")
    public PushService getPushServiceA() throws IOException {
        return new PushService(
                new GtConfig(hostA, appIdA, appKeyA, appSecretA, masterSecretA));
    }

}
