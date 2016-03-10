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
public class ScheduledTask {
    private static Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired @Qualifier("PushServiceA")
    private PushService pushServiceA;

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
