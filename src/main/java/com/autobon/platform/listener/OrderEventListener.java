package com.autobon.platform.listener;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.getui.PushService;
import com.autobon.order.entity.DetailedOrder;
import com.autobon.order.entity.Order;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.DetailedOrderService;
import com.autobon.order.service.OrderService;
import com.autobon.shared.RedisCache;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechStatService;
import com.autobon.technician.service.TechnicianService;
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
    private final String dayNewKeyPrefix = "DayNewOrderCount@";
    private final String dayFinishedKeyPrefix = "DayFinishedOrderCount@";
    private final String monthNewKeyPrefix = "MonthNewOrderCount@";
    private final String monthFinishedKeyPrefix = "MonthFinishedOrderCount@";
    private final String totalCreatedKey = "totalCreatedOrder";
    private final String totalFinishedKey = "totalFinishedOrder";

    @Autowired OrderService orderService;
    @Autowired DetailedOrderService detailedOrderService;
    @Autowired RedisCache redisCache;
    @Autowired CoopAccountService coopAccountService;
    @Autowired TechnicianService technicianService;
    @Autowired TechStatService techStatService;
    @Autowired ConstructionService constructionService;
    @Autowired @Qualifier("PushServiceA") PushService pushServiceA;
    @Autowired @Qualifier("PushServiceB") PushService pushServiceB;

    public static class OrderEvent extends Event<Order> {
        public OrderEvent(Order order, Action action) {
            super(order, action);
        }
    }

    @EventListener
    public void onOrderEvent(Event<Order> event) throws IOException {
        Order order = event.getPayload();
        switch(event.getAction()) {
            case CREATED:
                this.onOrderCreated(order);
                break;
            case TAKEN:
                this.onOrderTaken(order);
                break;
            case APPOINTED:
                this.onOrderAppointed(order);
                break;
            case FINISHED:
                this.orderFinished(order);
                break;
            case GIVEN_UP:
                this.onOrderGivenUp(order);
                break;
            case CANCELED:
                this.onOrderCanceled(order);
                break;
        }
    }

    public int getNewOrderCountOfToday() {
        String key = dayNewKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(orderService.countOfNew(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getFinishedOrderCountOfToday() {
        String key = dayFinishedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(orderService.countOfFinished(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getNewOrderCountOfThisMonth() {
        String key = monthNewKeyPrefix + new SimpleDateFormat("yyyy-MM-01").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(orderService.countOfNew(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getFinishedOrderCountOfThisMonth() {
        String key = monthFinishedKeyPrefix + new SimpleDateFormat("yyyy-MM-01").format(new Date());
        return Integer.parseInt(redisCache.getOrElse(key,
                () -> String.valueOf(orderService.countOfFinished(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), new Date())),
                24*3600));
    }

    public int getTotalCreatedOrderCount() {
        return Integer.parseInt(redisCache.getOrElse(totalCreatedKey, () -> String.valueOf(orderService.totalOfCreated()), 24*3600));
    }

    public int getTotalFinishedOrderCount() {
        return Integer.parseInt(redisCache.getOrElse(totalFinishedKey, () -> String.valueOf(orderService.totalOfFinished()), 24*3600));
    }

    private void onOrderCreated(Order order) throws IOException {
        LocalDate localDate = order.getAddTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        String dayKey = dayNewKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(day);
        String monthKey = monthNewKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(month);
        redisCache.getAfterUpdate(dayKey, () -> String.valueOf(orderService.countOfNew(day, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(monthKey, () -> String.valueOf(orderService.countOfNew(month, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(totalCreatedKey, () -> String.valueOf(orderService.totalOfCreated() - 1),
                24*3600, this::increase);

        if (order.getStatusCode() < Order.Status.NEWLY_CREATED.getStatusCode()) return;
        String msgTitle = "你收到新订单推送消息";
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "NEW_ORDER");
        DetailedOrder detailedOrder = detailedOrderService.get(order.getId());
        detailedOrder.setRemark("");
        detailedOrder.setPhoto("");
        map.put("order", "");
        map.put("title", msgTitle);
        boolean result = pushServiceA.pushToApp(msgTitle, new ObjectMapper().writeValueAsString(map), 0);
        if (!result) log.error("订单: " + order.getOrderNum() + "的推送消息发送失败");
    }

    private void onOrderTaken(Order order) throws IOException {
        TechStat stat = techStatService.getByTechId(order.getMainTechId());
        if (stat == null) {
            stat = new TechStat();
            stat.setTechId(order.getMainTechId());
        }
        stat.setTotalOrders(stat.getTotalOrders() + 1);
        techStatService.save(stat);

        order.setPhoto("");


        // 向商户推送订单已有人接单消息
        CoopAccount coopAccount = coopAccountService.getById(order.getCreatorId());
        String msgTitle = "订单: " + order.getOrderNum() + "已接单";
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "ORDER_COMPLETE");
        map.put("title", msgTitle);
        map.put("order", order);
        pushServiceB.pushToSingle(coopAccount.getPushId(), msgTitle, new ObjectMapper().writeValueAsString(map), 24*3600);
    }

    private void onOrderAppointed(Order order) throws IOException {
        // 更新订单总数
        Technician tech = technicianService.get(order.getMainTechId());
        TechStat stat = techStatService.getByTechId(tech.getId());
        if (stat == null) {
            stat = new TechStat();
            stat.setTechId(tech.getId());
        }
        stat.setTotalOrders(stat.getTotalOrders() + 1);
        techStatService.save(stat);

        order.setPhoto("");

        // 推送派单消息
        String msgTitle = "你收到派单消息";
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "ASSIGN_ORDER");
        map.put("order", order);
        map.put("title", msgTitle);
        boolean result = pushServiceA.pushToSingle(tech.getPushId(), msgTitle, new ObjectMapper().writeValueAsString(map), 72*3600);
        if (!result) log.error("订单: " + order.getOrderNum() + "的派单消息发送失败");
    }

    private void onOrderCanceled(Order order) throws IOException {
        if (order.getMainTechId() > 0) {
            Technician mainTech = technicianService.get(order.getMainTechId());

            order.setPhoto("");

            // 推送撤单消息
            String msgTitle = "你有订单已被商户撤销";
            HashMap<String, Object> map = new HashMap<>();
            map.put("action", "ORDER_CANCELED");
            map.put("order", order);
            map.put("title", msgTitle);

            boolean result;
            if (order.getSecondTechId() > 0) {
                result = pushServiceA.pushToList(new String[] {mainTech.getPushId(), technicianService.get(order.getSecondTechId()).getPushId()},
                            msgTitle, new ObjectMapper().writeValueAsString(map), 72*3600);
            } else {
                result = pushServiceA.pushToSingle(mainTech.getPushId(), msgTitle, new ObjectMapper().writeValueAsString(map), 72*3600);
            }

            if (!result) log.error("订单: " + order.getOrderNum() + "的撤单消息发送失败");
        }
    }

    private void onOrderGivenUp(Order order) throws IOException {
        CoopAccount account = coopAccountService.getById(order.getCreatorId());
        order.setPhoto("");

        String msgTitle = "你有订单已被技师放弃";
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "ORDER_GIVEN_UP");
        map.put("order", order);
        map.put("title", msgTitle);

        boolean result = pushServiceB.pushToSingle(account.getPushId(), msgTitle, new ObjectMapper().writeValueAsString(map), 72*3600);
        if (!result) log.error("订单: " + order.getOrderNum() + "的弃单消息发送失败");
    }

    private void onOrderFinished(Order order) throws IOException {
        LocalDate localDate = order.getFinishTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        String dayKey = dayFinishedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(day);
        String monthKey = monthFinishedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(month);
        redisCache.getAfterUpdate(dayKey, () -> String.valueOf(orderService.countOfFinished(day, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(monthKey, () -> String.valueOf(orderService.countOfFinished(month, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(totalFinishedKey, () -> String.valueOf(orderService.totalOfFinished() - 1),
                24*3600, this::increase);

        order.setPhoto("");

        // 向商户推送订单完成消息
        CoopAccount coopAccount = coopAccountService.getById(order.getCreatorId());
        String msgTitle = "订单: " + order.getOrderNum() + "已完成";
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "ORDER_COMPLETE");
        map.put("title", msgTitle);
        map.put("order", order);
        pushServiceB.pushToSingle(coopAccount.getPushId(), msgTitle, new ObjectMapper().writeValueAsString(map), 24*3600);

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


    /**
     *
     * @param order
     * @throws IOException
     */
    private void orderFinished(Order order) throws IOException {
        LocalDate localDate = order.getFinishTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date month = Date.from(localDate.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        String dayKey = dayFinishedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(day);
        String monthKey = monthFinishedKeyPrefix + new SimpleDateFormat("yyyy-MM-dd").format(month);
        redisCache.getAfterUpdate(dayKey, () -> String.valueOf(orderService.countOfFinished(day, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(monthKey, () -> String.valueOf(orderService.countOfFinished(month, new Date()) - 1),
                24*3600, this::increase);
        redisCache.getAfterUpdate(totalFinishedKey, () -> String.valueOf(orderService.totalOfFinished() - 1),
                24*3600, this::increase);

        order.setPhoto("");

        // 向商户推送订单完成消息
        CoopAccount coopAccount = coopAccountService.getById(order.getCreatorId());
        String msgTitle = "订单: " + order.getOrderNum() + "已完成";
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "ORDER_COMPLETE");
        map.put("title", msgTitle);
        map.put("order", order);
        pushServiceB.pushToSingle(coopAccount.getPushId(), msgTitle, new ObjectMapper().writeValueAsString(map), 24*3600);


    }

    private String increase(String v) {
        return String.valueOf(Integer.parseInt(v) + 1);
    }
}
