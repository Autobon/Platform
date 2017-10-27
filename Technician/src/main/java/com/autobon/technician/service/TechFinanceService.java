package com.autobon.technician.service;

import com.autobon.technician.entity.TechFinance;
import com.autobon.technician.entity.TechFinanceView;
import com.autobon.technician.repository.TechFinanceRepository;
import com.autobon.technician.repository.TechFinanceViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by tian on 17/9/14.
 */
@Service
public class TechFinanceService {
    @Autowired
    TechFinanceViewRepository repository;
    @Autowired
    TechFinanceRepository repository0;

    public Page<TechFinanceView> find(String phone, String name, int page, int pageSize) {
        if(phone != null){
            phone = "%"+phone+"%";
        }

        if(name != null){
            name = "%"+name+"%";
        }


        return repository.find(phone, name, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.ASC, "id")));

    }

    public TechFinance save(TechFinance techFinance) {
        return repository0.save(techFinance);
    }

    public TechFinance getByTechId(int techId) {
        return repository0.getByTechId(techId);
    }
}
