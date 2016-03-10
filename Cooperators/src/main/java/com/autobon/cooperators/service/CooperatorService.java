package com.autobon.cooperators.service;

import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.repository.CooperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Cooperator getByShortname(String shortname) {
        return  cooperatorRepository.getByShortname(shortname);
    }

    public Cooperator getByContactPhone(String contactPhone) {
        return cooperatorRepository.getByContactPhone(contactPhone);
    }

    public void save(Cooperator cooperator) {
        cooperatorRepository.save(cooperator);
    }
}
