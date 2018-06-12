package com.autobon.platform.schedule;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.repository.CoopAccountRepository;
import com.autobon.order.entity.Order;
import com.autobon.order.repository.OrderRepository;
import com.autobon.platform.listener.Event;
import com.autobon.platform.listener.OrderEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

/**
 * Created by wh on 2018/6/7.
 */
public class OrderTask {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CoopAccountRepository coopAccountRepository;

    @Autowired
    ApplicationEventPublisher publisher;



    public void pushNewCreate36(){


        List<Order> orderList = orderRepository.findNewCreate36();

        for(Order order :orderList){
            CoopAccount coopAccount = coopAccountRepository.findOne(order.getCreatorId());
            if(coopAccount!= null && coopAccount.getPushId() != null){

                publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.REMIND36));

            }


        }

    }

    public void pushNewCreate12(){


        List<Order> orderList = orderRepository.findNewCreate12();

        for(Order order :orderList) {
            CoopAccount coopAccount = coopAccountRepository.findOne(order.getCreatorId());
            if (coopAccount != null && coopAccount.getPushId() != null) {

                publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.REMIND36));
            }


        }

    }


    public void pushNewCreate2(){


    }


}
