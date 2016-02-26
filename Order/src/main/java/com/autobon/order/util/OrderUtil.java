package com.autobon.order.util;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderShow;

/**
 * Created by liz on 2016/2/24.
 */
public class OrderUtil{

    /**
     *
     * @param order
     * @return
     */
    public static OrderShow order2OrderShow(Order order){

        OrderShow orderShow = new OrderShow();
        orderShow.setId(order.getId());
        orderShow.setOrderNum(order.getOrderNum());
        orderShow.setOrderType(order.getOrderType());
        orderShow.setPhoto(order.getPhoto());
        orderShow.setOrderTime(order.getOrderTime());
        orderShow.setAddTime(order.getAddTime());
        orderShow.setStatus(order.getStatus());
        orderShow.setCustomerType(order.getCustomerType());
        orderShow.setCustomerId(order.getCustomerId());
        orderShow.setRemark(order.getRemark());
        orderShow.setMainTechId(order.getMainTechId());
        orderShow.setSecondTechId(order.getSecondTechId());

        return  orderShow;
    }

    /**
     *
     * @param orderShow
     * @return
     */
    public static Order orderShow2Order(OrderShow orderShow){

        Order order = new Order();
        order.setId(orderShow.getId());
        order.setOrderNum(orderShow.getOrderNum());
        order.setOrderType(orderShow.getOrderType());
        order.setPhoto(orderShow.getPhoto());
        order.setOrderTime(orderShow.getOrderTime());
        order.setAddTime(orderShow.getAddTime());
        order.setStatus(orderShow.getStatus());
        order.setCustomerType(orderShow.getCustomerType());
        order.setCustomerId(orderShow.getCustomerId());
        order.setRemark(orderShow.getRemark());
        order.setMainTechId(orderShow.getMainTechId());
        order.setSecondTechId(orderShow.getSecondTechId());

        return  order;
    }

}
