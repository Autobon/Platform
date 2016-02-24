package com.autobon.order.service;

import com.autobon.order.entity.Order;
import com.autobon.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuh on 2016/2/22.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrderList() {
        List<Order> orderList01 = orderRepository.findAllByStatusOrderByAddTimeAsc(1);
        List<Order> orderList02 = orderRepository.findAllByStatusOrderByAddTimeAsc(2);
        List<Order> orderList = new ArrayList<>();
        if(orderList01!=null && orderList01.size()>0){
            for(Order order:orderList01){
                orderList.add(order);
            }
        }
        if(orderList02!=null && orderList02.size()>0){
            for(Order order:orderList02){
                orderList.add(order);
            }
        }
        return orderList;
    }

    public Order getLocation(int orderId) {
        Order order = orderRepository.findOne(orderId);
        return  order;
    }
}
