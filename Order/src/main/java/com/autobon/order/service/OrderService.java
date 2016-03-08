package com.autobon.order.service;

import com.autobon.order.entity.Order;
import com.autobon.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by yuh on 2016/2/22.
 */
@Service
public class OrderService {

    private OrderRepository orderRepository = null;
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository){ this.orderRepository = orderRepository; }

    public Order get(int orderId) {
        return orderRepository.findOne(orderId);
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public Page<Order> findFinishedOrderByMainTechId(int techId, int page, int pageSize) {
        return orderRepository.findFinishedOrderByMainTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<Order> findFinishedOrderBySecondTechId(int techId, int page, int pageSize) {
        return orderRepository.findFinishedOrderBySecondTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<Order> findUnFinishedOrderByTechId(int techId, int page, int pageSize) {
        return orderRepository.findUnFinishedOrderByTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<Order> findAll(int page, int pageSize) {
        return orderRepository.findAll(new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }
}
