package com.autobon.platform.schedule;

import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 订单超时清理任务
 * Created by lu on 2016/3/28.
 */
@Component
public class ExpireOrdersTask {
    private static final Logger log = LoggerFactory.getLogger(ExpireOrdersTask.class);
    @Autowired
    OrderService orderService;

    @Async
    @Scheduled(fixedRate = 3600 * 1000)
    public void expireOrders() {
        log.info("订单超时清理任务开始");
        Date before = Date.from(LocalDateTime.now().minusHours(3).atZone(ZoneId.systemDefault()).toInstant());
        int totalPages = 0;
        int pageNo = 1;
        long orderCount = 0;
        do{
            Page<Order> page = orderService.findExpired(before, pageNo++, 20);
            totalPages = page.getTotalPages();
            orderCount = page.getTotalElements();
            for (Order order : page.getContent()) {
                order.setStatus(Order.Status.EXPIRED);
                orderService.save(order);
            }
        } while (pageNo < totalPages);
        log.info("订单超时清理任务完成, 共" + orderCount + "条");
    }
}
