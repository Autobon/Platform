package com.autobon.order.service;

import com.autobon.order.entity.OrderStatusRecord;
import com.autobon.order.repository.OrderStatusRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tian on 18/6/7.
 */
@Service
public class OrderStatusRecordService {
    @Autowired OrderStatusRecordRepository repository;

    public OrderStatusRecord get(int id) {
        return repository.findOne(id);
    }

    public OrderStatusRecord save(OrderStatusRecord orderStatusRecord) {
        return repository.save(orderStatusRecord);
    }

    public List<OrderStatusRecord> findByOrderId(int orderId) {
        return repository.findByOrderId(orderId);
    }

    public OrderStatusRecord findByOrderIdAndStatus(int orderId, int status){
        return repository.findByOrderIdAndStatus(orderId, status);
    }
}
