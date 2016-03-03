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
        PushService p = new PushService(
                new GtConfig(hostA, appIdA, appKeyA, appSecretA, masterSecretA));
        // 在返回PushService实例前,与个推服务器建立一次连接,后面可直接发送推送消息
        if (!p.connect()) {
            throw new IOException("连接个推服务器失败");
        }
        return p;
    }

}
