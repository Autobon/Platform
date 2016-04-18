package com.autobon.platform.listener;

import com.autobon.getui.PushService;
import com.autobon.shared.RedisCache;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
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
public class TechnicianEventListener {
    private final String dayNewKeyPrefix = "DayNewTechCount@";
    private final String dayVerifiedKeyPrefix = "DayVerifiedTechCount@";
    private final String monthNewKeyPrefix = "MonthNewTechCount@";
    private final String monthVerifiedKeyPrefix = "MonthVerifiedTechCount@";
    private final String totalRegisteredKey = "totalRegisteredTech";
    private final String totalVerifiedKey = "totalVerifiedTech";

    @Autowired RedisCache redisCache;
    @Autowired TechnicianService technicianService;
    @Autowired @Qualifier("PushServiceA") PushService pushServiceA;

    public static class TechnicianEvent extends Event<Technician> {
        public TechnicianEvent(Technician tech, Action action) {
            super(tech, action);
        }
    }

    @EventListener
    public void onTechnicianEvent(Event<Technician> event) throws IOException {
        if (event.getAction() == Event.Action.CREATED) this.onTechnicianCreated(event.getPayload());
        if (event.getAction() == Event.Action.VERIFIED) this.onTechnicianVerified(event.getPayload());
    }

    public int getNewTechnicianCountOfToday() {
        String key = dayNewKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(technicianService.countOfNew(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getVerifiedTechnicianCountOfToday() {
        String key = dayVerifiedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(technicianService.countOfVerified(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getNewTechnicianCountOfThisMonth() {
        String key = monthNewKeyPrefix + new SimpleDateFormat("yyyy-MM-01").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(technicianService.countOfNew(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getVerifiedTechnicianCountOfThisMonth() {
        String key = monthVerifiedKeyPrefix + new SimpleDateFormat("yyyy-MM-01").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(technicianService.countOfVerified(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getTotalRegisteredTechnicianCount() {
        return Integer.parseInt(redisCache.getOrElse(totalRegisteredKey, () -> String.valueOf(technicianService.totalOfRegistered()), 24*3600));
    }

    public int getTotalVerifiedTechnicianCount() {
        return Integer.parseInt(redisCache.getOrElse(totalVerifiedKey, () -> String.valueOf(technicianService.totalOfVerified()), 24*3600));
    }

    private void onTechnicianCreated(Technician technician) {
        LocalDate localDate = technician.getCreateAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        String dayKey = dayNewKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(day);
        String monthKey = monthNewKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(month);
        redisCache.getAfterUpdate(dayKey, () -> String.valueOf(technicianService.countOfNew(day, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(monthKey, () -> String.valueOf(technicianService.countOfNew(month, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(totalRegisteredKey, () -> String.valueOf(technicianService.totalOfRegistered() - 1),
                24*3600, this::increase);
    }

    private void onTechnicianVerified(Technician technician) throws IOException {
        if (technician.getStatus() == Technician.Status.VERIFIED) {
            LocalDate localDate = technician.getVerifyAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

            String dayKey = dayVerifiedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(day);
            String monthKey = monthVerifiedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(month);
            redisCache.getAfterUpdate(dayKey, () -> String.valueOf(technicianService.countOfVerified(day, new Date()) - 1),
                    24*3600, this::increase);
            redisCache.getAfterUpdate(monthKey, () -> String.valueOf(technicianService.countOfVerified(month, new Date()) - 1),
                    24*3600, this::increase);
            redisCache.getAfterUpdate(totalVerifiedKey, () -> String.valueOf(technicianService.totalOfVerified() - 1),
                    24*3600, this::increase);

            String title = "你已通过技师资格认证";
            pushServiceA.pushToSingle(technician.getPushId(), title,
                    "{\"action\":\"VERIFICATION_SUCCEED\",\"title\":\"" + title + "\"}",
                    3*24*3600);
        } else if (technician.getStatus() == Technician.Status.REJECTED) {
            String title = "你的技师资格认证失败: " + technician.getVerifyMsg();
            pushServiceA.pushToSingle(technician.getPushId(), title,
                    "{\"action\":\"VERIFICATION_FAILED\",\"title\":\"" + title + "\"}",
                    3*24*3600);
        }
    }

    private String increase(String v) {
        return String.valueOf(Integer.parseInt(v) + 1);
    }
}
