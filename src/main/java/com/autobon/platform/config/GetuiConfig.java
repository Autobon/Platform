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
    public PushService getPushService() throws IOException {
        PushService p = new PushService(
                new GtConfig(host, appId, appKey, appSecret, masterSecret));
        // 在返回PushService实例前,与个推服务器建立一次连接,后面可直接发送推送消息
        if (!p.connect()) {
            throw new IOException("连接个推服务器失败");
        }
        return p;
    }

}
