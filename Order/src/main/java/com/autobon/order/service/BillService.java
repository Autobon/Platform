package com.autobon.order.service;

import com.autobon.order.entity.Bill;
import com.autobon.order.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dave on 16/3/11.
 */
@Service
public class BillService {
    @Autowired BillRepository repository;

    public Bill get(int id) {
        return repository.findOne(id);
    }

    public Bill save(Bill bill) {
        return repository.save(bill);
    }

    public Page<Bill> findByTechId(int techId, int page, int pageSize) {
        return repository.findByTechId(techId,
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }

    @Transactional
    public int clear() {
        return repository.clear();
    }
}
