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

    //coop
    @Value("${com.autobon.getui.b.host}")
    private String hostB;
    @Value("${com.autobon.getui.b.appId}")
    private String appIdB;
    @Value("${com.autobon.getui.b.appKey}")
    private String appKeyB;
    @Value("${com.autobon.getui.b.appSecret}")
    private String appSecretB;
    @Value("${com.autobon.getui.b.masterSecret}")
    private String masterSecretB;

    @Bean(name = "PushServiceA")
    public PushService getPushServiceA() throws IOException {
        return new PushService(
                new GtConfig(hostA, appIdA, appKeyA, appSecretA, masterSecretA));
    }

    @Bean(name = "PushServiceB")
    public PushService getPushServiceB() throws IOException {
        return new PushService(
                new GtConfig(hostB, appIdB, appKeyB, appSecretB, masterSecretB));
    }

}
