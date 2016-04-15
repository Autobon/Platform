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
    private final String dayNewKeyPrefix = "DayNewCoopCount@";
    private final String dayVerifiedKeyPrefix = "DayVerifiedCoopCount@";
    private final String monthNewKeyPrefix = "MonthNewCoopCount@";
    private final String monthVerifiedKeyPrefix = "MonthVerifiedCoopCount@";
    private final String totalRegisteredKey = "totalRegisteredCooper";
    private final String totalVerifiedKey = "totalVerifiedCooper";

    @Autowired RedisCache redisCache;
    @Autowired CooperatorService cooperatorService;
    @Autowired CoopAccountService coopAccountService;
    @Autowired ReviewCooperService reviewCooperService;
    @Autowired @Qualifier("PushServiceB") PushService pushServiceB;

    @EventListener
    public void onCooperatorEvent(Event<Cooperator> event) throws IOException {
        if (event.getAction() == Event.Action.CREATED) this.onCooperatorCreated(event.getPayload());
        else if (event.getAction() == Event.Action.VERIFIED) this.onCooperatorVerified(event.getPayload());
    }

    public int getNewCooperatorCountOfToday() {
        String key = dayNewKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(cooperatorService.countOfNew(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getVerifiedCooperatorCountOfToday() {
        String key = dayVerifiedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(cooperatorService.countOfVerified(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getNewCooperatorCountOfThisMonth() {
        String key = monthNewKeyPrefix + new SimpleDateFormat("yyyy-MM-01").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(cooperatorService.countOfNew(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getVerifiedCooperatorCountOfThisMonth() {
        String key = monthVerifiedKeyPrefix + new SimpleDateFormat("yyyy-MM-01").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(cooperatorService.countOfVerified(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getTotalRegisteredCooperatorCount() {
        return Integer.parseInt(redisCache.getOrElse(totalRegisteredKey, () -> String.valueOf(cooperatorService.totalOfRegistered()), 24*3600));
    }

    public int getTotalVerifiedCooperatorCount() {
        return Integer.parseInt(redisCache.getOrElse(totalVerifiedKey, () -> String.valueOf(cooperatorService.totalOfVerified()), 24*3600));
    }

    private void onCooperatorCreated(Cooperator cooper) {
        LocalDate localDate = cooper.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        String dayKey = dayNewKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(day);
        String monthKey = monthNewKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(month);
        redisCache.getAfterUpdate(dayKey, () -> String.valueOf(cooperatorService.countOfNew(day, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(monthKey, () -> String.valueOf(cooperatorService.countOfNew(month, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(totalRegisteredKey, () -> String.valueOf(cooperatorService.totalOfRegistered() - 1),
                24*3600, this::increase);
    }

    private void onCooperatorVerified(Cooperator cooper) throws IOException {
        ReviewCooper review = reviewCooperService.getByCooperatorId(cooper.getId()).get(0);
        CoopAccount coopAccount = coopAccountService.getByCooperatorIdAndIsMain(cooper.getId(), true);

        if (cooper.getStatusCode() == 1) {
            LocalDate localDate = review.getReviewTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            String dayVerifiedKey = dayVerifiedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(day);
            String monthVerifiedKey = monthVerifiedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(month);

            redisCache.getAfterUpdate(dayVerifiedKey, () -> String.valueOf(cooperatorService.countOfVerified(day, new Date()) - 1),
                    24*3600, this::increase);
            redisCache.getAfterUpdate(monthVerifiedKey, () -> String.valueOf(cooperatorService.countOfVerified(month, new Date()) - 1),
                    24*3600, this::increase);
            redisCache.getAfterUpdate(totalVerifiedKey, () -> String.valueOf(cooperatorService.totalOfVerified() - 1),
                    24*3600, this::increase);

            String title = "你已通过合作商户资格认证";
            pushServiceB.pushToSingle(coopAccount.getPushId(), title,
                    "{\"action\":\"VERIFICATION_SUCCEED\",\"title\":\"" + title + "\"}",
                    3 * 24 * 3600);
        } else if (cooper.getStatusCode() == 2) {
            String title = "你的合作商户资格认证失败: " + review.getRemark();
            pushServiceB.pushToSingle(coopAccount.getPushId(), title,
                    "{\"action\":\"VERIFICATION_FAILED\",\"title\":\"" + title + "\"}",
                    3 * 24 * 3600);
        }
    }

    private String increase(String v) {
        return String.valueOf(Integer.parseInt(v) + 1);
    }
}
