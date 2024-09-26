package com.autobon.cooperators.service;

import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.CooperatorView;
import com.autobon.cooperators.repository.CooperatorRepository;
import com.autobon.cooperators.repository.CooperatorViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by lu on 2016/3/7.
 */
@Service
public class CooperatorService {
    @Autowired CooperatorRepository repository;

    @Autowired
    CooperatorViewRepository cooperatorViewRepository;

    public Cooperator get(int id){
        return repository.findOne(id);
    }

    public void save(Cooperator cooperator) {
        repository.save(cooperator);
    }

    public Page<Cooperator> find(String fullname, String corporationName, Integer statusCode, int page, int pageSize) {
        if(fullname != null){
            fullname = "%"+fullname+"%";
        }
        if(corporationName != null){
            corporationName = "%"+corporationName+"%";
        }
        return repository.find(fullname, corporationName, statusCode,
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }

    public Page<Cooperator> findByLocation(String province, String city, int page, int pageSize) {
        return repository.findByLocation(province, city, new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }

    public int countOfNew(Date from, Date to) {
        return repository.countOfNew(from, to);
    }

    public int countOfVerified(Date from, Date to) {
        return repository.countOfVerified(from, to);
    }

    public int totalOfRegistered() {
        return repository.totalOfRegistered();
    }

    public int totalOfVerified() {
        return repository.totalOfVerified();
    }

    public List<CooperatorView> findAll(){

        return  cooperatorViewRepository.findAll();
    }
}
