package com.autobon.cooperators.service;

import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.repository.CooperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by lu on 2016/3/7.
 */
@Service
public class CooperatorService {
    @Autowired
    private CooperatorRepository cooperatorRepository;

    public Cooperator get(int id){
        return cooperatorRepository.findOne(id);
    }

    public Cooperator getByPhone(String phone) {
        return cooperatorRepository.getByPhone(phone);
    }

    public void save(Cooperator cooperator) {
        cooperatorRepository.save(cooperator);
    }

    public Page<Cooperator> find(String fullname, String corporationName, Integer statusCode, int page, int pageSize) {
        return cooperatorRepository.find(fullname, corporationName, statusCode,
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }
}
