package com.autobon.cooperators.service;

import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.repository.CooperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Cooperator> findCoop(String fullname, String businessLicense, String contactPhone, Integer currentPage, Integer pageSize) {
        currentPage = currentPage == null?1:currentPage;
        currentPage = currentPage == null?1:currentPage;
        currentPage = currentPage <= 0?1:currentPage;
        pageSize = pageSize == null?10:pageSize;
        pageSize = pageSize <= 0?10:pageSize;

        Pageable p = new PageRequest(currentPage-1,pageSize);
        Page<Cooperator> coopPage = cooperatorRepository.findCoop(fullname,businessLicense,contactPhone,p);
        return  coopPage;
    }
}
