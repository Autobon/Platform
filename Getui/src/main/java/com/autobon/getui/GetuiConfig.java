package com.autobon.getui;

import com.autobon.getui.utils.GtConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by dave on 16/2/22.
 */
@SpringBootApplication
public class GetuiConfig {
    @Value("${com.autobon.getui.host}")
    private String host;
    @Value("${com.autobon.getui.appId}")
    private String appId;
    @Value("${com.autobon.getui.appKey}")
    private String appKey;
    @Value("${com.autobon.getui.appSecret}")
    private String appSecret;
    @Value("${com.autobon.getui.masterSecret}")
    private String masterSecret;

    @Bean
    public GtConfig getGtConfig() {
        return new GtConfig(host, appId, appKey, appSecret, masterSecret);
    }
}
