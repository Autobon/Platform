package com.autobon.platform.listener;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.cooperators.service.ReviewCooperService;
import com.autobon.getui.PushService;
import com.autobon.shared.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by dave on 16/3/30.
 */
@Component
public class CooperatorEventListener {
    private static final Logger log = LoggerFactory.getLogger(CooperatorEventListener.class);
    @Autowired RedisCache redisCache;
    @Autowired CooperatorService cooperatorService;
    @Autowired CoopAccountService coopAccountService;
    @Autowired ReviewCooperService reviewCooperService;
    @Autowired @Qualifier("PushServiceB") PushService pushServiceB;

    @EventListener
    public void onCooperatorEvent(Event<Cooperator> event) throws IOException {
        if (event.getAction() == Event.Action.CREATED) this.onCooperatorCreated(event.getData());
        else if (event.getAction() == Event.Action.VERIFIED) this.onCooperatorVerified(event.getData());
    }

    private void onCooperatorCreated(Cooperator cooper) {
        String key = "DayNewCoopCount@" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        int iCount = redisCache.getInt(key, -1);
        if (iCount < 0) {
            iCount = cooperatorService.countOfNew(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date());
        } else {
            iCount += 1;
        }
        redisCache.set(key, "" + iCount, 24*3600);
    }

    private void onCooperatorVerified(Cooperator cooper) throws IOException {
        CoopAccount coopAccount = coopAccountService.getByCooperatorIdAndIsMain(cooper.getId(), true);
        if (cooper.getStatusCode() == 1) {
            String title = "你已通过合作商户资格认证";
            pushServiceB.pushToSingle(coopAccount.getPushId(), title,
                    "{\"action\":\"VERIFICATION_SUCCEED\",\"title\":\"" + title + "\"}",
                    3 * 24 * 3600);
        } else if (cooper.getStatusCode() == 2) {
            ReviewCooper review = reviewCooperService.getByCooperatorId(cooper.getId()).get(0);
            String title = "你的合作商户资格认证失败: " + review.getRemark();
            pushServiceB.pushToSingle(coopAccount.getPushId(), title,
                    "{\"action\":\"VERIFICATION_FAILED\",\"title\":\"" + title + "\"}",
                    3 * 24 * 3600);
        }
    }
}
