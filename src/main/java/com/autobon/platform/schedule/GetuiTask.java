package com.autobon.platform.schedule;

import com.autobon.getui.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by dave on 16/3/11.
 */
@Component
public class GetuiTask {
    private static final Logger log = LoggerFactory.getLogger(GetuiTask.class);

    @Autowired @Qualifier("PushServiceA")
    private PushService pushServiceA;

    // 每隔1小时连接个推服务器一次,否则可能出现sigerr错误
    @Async
    @Scheduled(fixedRate = 3600*1000)
    public void connectGetui() {
        try {
            pushServiceA.connect();
            log.info("连接个推服务器成功");
        } catch (Exception e) {
            log.info("连接个推服务器失败");
        }
    }
}
