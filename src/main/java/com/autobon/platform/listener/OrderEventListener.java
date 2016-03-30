package com.autobon.platform.listener;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.SysStat;
import com.autobon.order.service.SysStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by dave on 16/3/30.
 */
@Component
public class OrderEventListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    @Autowired SysStatService sysStatService;

    @EventListener
    public void onOrderCreated(Order order) {
        LocalDate localDate = order.getAddTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        SysStat dayStat = sysStatService.getOfDay(day);
        if (dayStat == null) {
            dayStat = new SysStat();
            dayStat.setStatType("day");
            dayStat.setStatTime(day);
            dayStat.setOrderCount(1);
            sysStatService.save(dayStat);
        } else {
            dayStat.setOrderCount(dayStat.getOrderCount() + 1);
            sysStatService.save(dayStat);
        }

        SysStat monthStat = sysStatService.getOfMonth(month);
        if (monthStat == null) {
            monthStat = new SysStat();
            monthStat.setStatType("month");
            monthStat.setStatTime(month);
            monthStat.setOrderCount(1);
            sysStatService.save(monthStat);
        } else {
            monthStat.setOrderCount(monthStat.getOrderCount() + 1);
            sysStatService.save(monthStat);
        }
        log.info("今日第" + dayStat.getOrderCount() + "单已创建, 订单编号: " + order.getOrderNum());
    }

}
