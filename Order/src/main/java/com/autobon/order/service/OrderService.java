package com.autobon.order.service;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderShow;
import com.autobon.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuh on 2016/2/22.
 */
@Service
public class OrderService {

    private OrderRepository orderRepository = null;
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository){ this.orderRepository = orderRepository; }


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

    /**
     * 增加订单
     * @param order
     * @return 订单 Order
     */
    public Order addOrder(Order order){
        //TODO
        return orderRepository.save(order);
    }

    /**
     * 修改订单
     * @param order
     * @return
     */
    public Order updateOrder(Order order){
        //TODO
        return orderRepository.save(order);
    }

    /**
     * 查找所有订单带分页
     * @return 所有订单
     */
    public Page<Order> findAllOrders(){
        Pageable p = new PageRequest(0,10);
        Page<Order> orders = orderRepository.findAllOrders(p);
        return orders;
    }

    /**
     * 根据条件查询订单分页列表
     * @param orderNum
     * @param orderType
     * @param status
     * @param customerId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Page<OrderShow> findByKeys(String orderNum,Integer orderType, Integer status, Integer customerId,Integer currentPage, Integer pageSize,String orderByProperty,Integer ascOrDesc){
        currentPage = currentPage == null?1:currentPage;
        currentPage = currentPage <= 0?1:currentPage;
        pageSize = pageSize == null?10:pageSize;
        pageSize = pageSize <= 0?10:pageSize;

        orderByProperty = orderByProperty ==null?"addTime":orderByProperty;
        ascOrDesc = (ascOrDesc==null?0:(ascOrDesc!=1?0:ascOrDesc));//1表示升序，其他表示降序
        Sort.Direction direction = ascOrDesc==1? Sort.Direction.ASC:Sort.Direction.DESC;

        Pageable pageRequest = new PageRequest(currentPage-1,pageSize,new Sort(direction,orderByProperty));

        Page<Order> orderPage = orderRepository.findByKeys(orderNum, orderType, status, customerId, pageRequest);

        List<Order> orderList = orderPage.getContent();
        List<OrderShow> orderShows = new ArrayList<>();
        for(Order order:orderList) {
            OrderShow orderShow = new OrderShow(order);
            orderShows.add(orderShow);
        }

        return new PageImpl<OrderShow>(orderShows, pageRequest, orderPage. getTotalElements());
    }

}
