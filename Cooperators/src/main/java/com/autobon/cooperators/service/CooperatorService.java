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
    private CooperatorRepository repository;

    public Cooperator get(int id){
        return repository.findOne(id);
    }

}
