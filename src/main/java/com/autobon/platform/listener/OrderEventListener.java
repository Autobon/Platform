package com.autobon.platform.listener;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.getui.PushService;
import com.autobon.order.entity.Order;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.OrderService;
import com.autobon.shared.RedisCache;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.service.TechStatService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;

/**
 * Created by dave on 16/3/30.
 */
@Component
public class OrderEventListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    @Autowired OrderService orderService;
    @Autowired RedisCache redisCache;
    @Autowired CoopAccountService coopAccountService;
    @Autowired TechStatService techStatService;
    @Autowired ConstructionService constructionService;
    @Autowired @Qualifier("PushServiceA") PushService pushServiceA;
    @Autowired @Qualifier("PushServiceB") PushService pushServiceB;

    @EventListener
    public void onOrderEvent(Order order) throws IOException {
        if (order.getStatus() == Order.Status.NEWLY_CREATED) this.onOrderCreated(order);
        else if (order.getStatus() == Order.Status.FINISHED) this.onOrderFinished(order);
    }

    public int getNewOrderCountOfToday() {
        String key = "DayNewOrderCount@" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        int iCount = this.getIntFromCache(key);
        if (iCount < 0) {
            iCount = orderService.countOfNew(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date());
            redisCache.set(key, "" + iCount, 24*3600);
        }
        return iCount;
    }

    public int getFinishedOrderCountOfToday() {
        String key = "DayFinishedOrderCount@" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        int iCount = this.getIntFromCache(key);
        if (iCount < 0) {
            iCount = orderService.countOfFinished(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date());
            redisCache.set(key, "" + iCount, 24*3600);
        }
        return iCount;
    }

    public int getNewOrderCountOfThisMonth() {
        String key = "MonthNewOrderCount@" + new SimpleDateFormat("yyyy-MM-01").format(new Date());
        int iCount = this.getIntFromCache(key);
        if (iCount < 0) {
            iCount = orderService.countOfNew(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date());
            redisCache.set(key, "" + iCount, 24*3600);
        }
        return iCount;
    }

    public int getFinishedOrderCountOfThisMonth() {
        String key = "MonthFinishedOrderCount@" + new SimpleDateFormat("yyyy-MM-01").format(new Date());
        int iCount = this.getIntFromCache(key);
        if (iCount < 0) {
            iCount = orderService.countOfFinished(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date());
            redisCache.set(key, "" + iCount, 24*3600);
        }
        return iCount;
    }

    private void onOrderCreated(Order order) throws IOException {
        LocalDate localDate = order.getAddTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        String dayKey = "DayNewOrderCount@" + new SimpleDateFormat("yyyy-MM-dd").format(day);
        int iDayCount = this.getIntFromCache(dayKey);
        if (iDayCount < 0) {
            iDayCount = orderService.countOfNew(day, new Date());
        }
        iDayCount += 1;
        redisCache.set(dayKey, "" + iDayCount, 24*3600);

        String monthKey = "MonthNewOrderCount@" + new SimpleDateFormat("yyyy-MM-dd").format(month);
        int iMonthCount = this.getIntFromCache(monthKey);
        if (iMonthCount < 0) {
            iMonthCount = orderService.countOfNew(month, new Date());
        }
        iMonthCount += 1;
        redisCache.set(monthKey, "" + iMonthCount, 24*3600);
        log.info("今日第" + iDayCount + "单已创建, 订单编号: " + order.getOrderNum());

        String msgTitle = "你收到新订单推送消息";
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "NEW_ORDER");
        map.put("order", order);
        map.put("title", msgTitle);
        boolean result = pushServiceA.pushToApp(msgTitle, new ObjectMapper().writeValueAsString(map), 0);
        if (!result) log.error("订单: " + order.getOrderNum() + "的推送消息发送失败");
    }

    private void onOrderFinished(Order order) throws IOException {
        LocalDate localDate = order.getAddTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        String dayKey = "DayFinishedOrderCount@" + new SimpleDateFormat("yyyy-MM-dd").format(day);
        int iDayCount = this.getIntFromCache(dayKey);
        if (iDayCount < 0) {
            iDayCount = orderService.countOfFinished(day, new Date());
        }
        iDayCount += 1;
        redisCache.set(dayKey, "" + iDayCount, 24*3600);

        String monthKey = "MonthFinishedOrderCount@" + new SimpleDateFormat("yyyy-MM-dd").format(month);
        int iMonthCount = this.getIntFromCache(monthKey);
        if (iMonthCount < 0) {
            iMonthCount = orderService.countOfFinished(month, new Date());
        }
        iMonthCount += 1;
        redisCache.set(monthKey, "" + iMonthCount, 24*3600);
        log.info("今日已完成第" + iDayCount + "单, 订单编号: " + order.getOrderNum());

        // 向商户推送订单完成消息
        if (order.getCreatorType() == 1) {
            CoopAccount coopAccount = coopAccountService.getById(order.getCreatorId());
            pushServiceB.pushToSingle(coopAccount.getPushId(), "订单: " + order.getOrderNum() + "已完成", new ObjectMapper().writeValueAsString(order), 24*3600);
        }

        // 更新主技师余额及未支付订单数
        TechStat stat = techStatService.getByTechId(order.getMainTechId());
        if (stat == null) {
            stat = new TechStat();
            stat.setTechId(order.getMainTechId());
        }
        stat.setUnpaidOrders(stat.getUnpaidOrders() + 1);
        stat.setBalance(stat.getBalance() + constructionService.getByTechIdAndOrderId(order.getMainTechId(), order.getId()).getPayment());
        techStatService.save(stat);

        // 更新次技师余额及未支付订单数
        if (order.getSecondTechId() != 0) {
            TechStat secondStat = techStatService.getByTechId(order.getSecondTechId());
            if (secondStat == null) {
                secondStat = new TechStat();
                secondStat.setTechId(order.getSecondTechId());
            }
            secondStat.setUnpaidOrders(secondStat.getUnpaidOrders() + 1);
            secondStat.setBalance(secondStat.getBalance() + constructionService.getByTechIdAndOrderId(
                    order.getSecondTechId(), order.getId()).getPayment());
            techStatService.save(secondStat);
        }
    }

    private int getIntFromCache(String key) {
        String sCount = redisCache.get(key);
        if (sCount == null) return -1;
        else return Integer.parseInt(sCount);
    }

}
