package com.autobon.platform.schedule;

import com.autobon.cooperators.service.CooperatorService;
import com.autobon.order.entity.SysStat;
import com.autobon.order.service.OrderService;
import com.autobon.order.service.SysStatService;
import com.autobon.technician.service.TechnicianService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 每日, 月系统订单,技师,商户统计
 * Created by dave on 16/3/30.
 */
@Component
public class SysStatTask {
    private static final Logger log = LoggerFactory.getLogger(SysStatTask.class);

    @Autowired TechnicianService technicianService;
    @Autowired CooperatorService cooperatorService;
    @Autowired OrderService orderService;
    @Autowired SysStatService sysStatService;

    // 每日凌晨7点开始前一日数据统计
    @Async
    @Scheduled(cron = "0 0 5 * * ?")
    public void dayStat() {
        log.info("每日统计任务开始");
        Date from = Date.from(LocalDate.now().minusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        this.statBetween(1, from, to);
        log.info("每日统计任务结束");
    }

    // 每月1号凌晨7点开始上月月度统计
    @Async
    @Scheduled(cron = "0 0 7 1 * ?")
    public void monthStat() {
        log.info("每月统计任务开始");
        Date from = Date.from(LocalDate.now().minusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        this.statBetween(2, from, to);
        log.info("每月统计任务结束");
    }

    private void statBetween(int type, Date from, Date to) {
        SysStat stat = new SysStat();
        stat.setStatType(type);
        stat.setStatTime(from);

        stat.setNewOrderCount(orderService.countOfNew(from, to));
        stat.setFinishedOrderCount(orderService.countOfFinished(from, to));
        stat.setNewCoopCount(cooperatorService.countOfNew(from, to));
        stat.setVerifiedCoopCount(cooperatorService.countOfVerified(from, to));
        stat.setNewTechCount(technicianService.countOfNew(from, to));
        stat.setVerifiedTechCount(technicianService.countOfVerified(from, to));
        sysStatService.save(stat);
    }
}
