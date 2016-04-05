package com.autobon.order.service;

import com.autobon.order.entity.Order;
import com.autobon.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yuh on 2016/2/22.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public Order get(int orderId) {
        return repository.findOne(orderId);
    }

    public void save(Order order) {
        repository.save(order);
    }

    public Page<Order> findFinishedOrderByMainTechId(int techId, int page, int pageSize) {
        return repository.findFinishedOrderByMainTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<Order> findFinishedOrderBySecondTechId(int techId, int page, int pageSize) {
        return repository.findFinishedOrderBySecondTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<Order> findUnfinishedOrderByTechId(int techId, int page, int pageSize) {
        return repository.findUnfinishedOrderByTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<Order> find(String orderNum, String creatorName, String contactPhone,
                            Integer orderType, Integer statusCode, int page, int pageSize) {
        return repository.find(orderNum, creatorName, contactPhone, orderType,
                statusCode, new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }

    public Page<Order> findExpired(Date signInBefore, Date finishBefore,  int page, int pageSize){
        return repository.findExpired(signInBefore, finishBefore, new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }

    public Page<Order> findBetweenByTechId(int techId, Date start, Date end, int page, int pageSize) {
        return repository.findBetweenByTechId(techId, start, end, new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }
}
