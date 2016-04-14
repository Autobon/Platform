package com.autobon.platform.listener;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.SysStat;
import com.autobon.order.service.SysStatService;
import com.autobon.shared.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
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
    @Autowired RedisCache redisCache;

    @EventListener
    public void onOrderEvent(Order order) {
        if (order.getStatus() == Order.Status.NEWLY_CREATED) this.onOrderCreated(order);
    }

    private void onOrderCreated(Order order) {
        LocalDate localDate = order.getAddTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        String dayKey = "DayOrderCount@" + new SimpleDateFormat("yyyy-MM-dd").format(day);
        String sDayOrderCount = redisCache.get(dayKey);
        int iDayOrderCount = 0;
        if (sDayOrderCount != null) {
            iDayOrderCount = Integer.parseInt(sDayOrderCount);
        }
        iDayOrderCount += 1;
        redisCache.set(dayKey, "" + iDayOrderCount, 24*3600);

        String monthKey = "MonthOrderCount@" + new SimpleDateFormat("yyyy-MM-dd").format(month);
        String sMonthOrderCount = redisCache.get(monthKey);
        int iMonthOrderCount = 0;
        if (sMonthOrderCount != null) {
            iMonthOrderCount = Integer.parseInt(sMonthOrderCount);
        } else {
            SysStat monthStat = sysStatService.getOfMonth(month);
            if (monthStat != null) {
                iMonthOrderCount = monthStat.getOrderCount();
            }
        }
        iMonthOrderCount += 1;
        redisCache.set(monthKey, "" + iMonthOrderCount, 24*3600);

        log.info("今日第" + iDayOrderCount + "单已创建, 订单编号: " + order.getOrderNum());
    }

}
