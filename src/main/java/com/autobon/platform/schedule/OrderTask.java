package com.autobon.platform.schedule;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.repository.CoopAccountRepository;
import com.autobon.order.entity.Order;
import com.autobon.order.repository.OrderRepository;
import com.autobon.platform.listener.Event;
import com.autobon.platform.listener.OrderEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Created by wh on 2018/6/7.
 */
public class OrderTask {

    private static final Logger log = LoggerFactory.getLogger(OrderTask.class);

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CoopAccountRepository coopAccountRepository;

    @Autowired
    ApplicationEventPublisher publisher;


    @Scheduled(cron = "0 0 * * * ?")
    public void pushNewCreate36(){


        log.info("预约36小时超时检索开始---");
        List<Order> orderList = orderRepository.findNewCreate36();

        for(Order order :orderList){
            log.info("预约36小时超时检索：" + "订单编号" +order.getOrderNum() +" 超时");
            CoopAccount coopAccount = coopAccountRepository.findOne(order.getCreatorId());
            if(coopAccount!= null && coopAccount.getPushId() != null){

                publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.REMIND36));

            }


        }

    }
    @Scheduled(cron = "0 5 * * * ?")
    public void pushNewCreate12(){

        log.info("预约12小时提醒检索开始---");
        List<Order> orderList = orderRepository.findNewCreate12();

        for(Order order :orderList) {
            log.info("预约36小时超时检索：" + "订单编号" +order.getOrderNum() +" 提醒");
            CoopAccount coopAccount = coopAccountRepository.findOne(order.getCreatorId());
            if (coopAccount != null && coopAccount.getPushId() != null) {

                publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.REMIND12));
            }


        }

    }


    public void pushNewCreate2(){


    }


}
