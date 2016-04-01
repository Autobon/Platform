package com.autobon.order.service;

import com.autobon.order.entity.DetailedBill;
import com.autobon.order.repository.DetailedBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dave on 16/4/1.
 */
@Service
public class DetailedBillService {
    @Autowired DetailedBillRepository repository;

    public DetailedBill get(int id) {
        return repository.findOne(id);
    }

    public Page<DetailedBill> find(Date month, Boolean paid, Integer techId, int page, int pageSize) {
        return repository.find(month, paid, techId,
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "sum"));
    }
}
