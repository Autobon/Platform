package com.autobon.order.service;

import com.autobon.order.entity.DetailedOrder;
import com.autobon.order.repository.DetailedOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by dave on 16/3/1.
 */
@Service
public class DetailedOrderService {
    @Autowired
    DetailedOrderRepository repository;

    public DetailedOrder get(int id) {
        return repository.findOne(id);
    }

    public Page<DetailedOrder> findAll(int page, int pageSize) {
        return repository.findAll(new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "addTime")));
    }


}
