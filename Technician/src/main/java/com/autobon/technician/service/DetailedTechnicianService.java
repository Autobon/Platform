package com.autobon.technician.service;

import com.autobon.technician.entity.DetailedTechnician;
import com.autobon.technician.repository.DetailedTechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by dave on 16/3/14.
 */
@Service
public class DetailedTechnicianService {
    @Autowired DetailedTechnicianRepository repository;

    public DetailedTechnician get(int id) {
        return repository.findOne(id);
    }

    public DetailedTechnician getByPhone(String phone) {
        return repository.getByPhone(phone);
    }

    public Page<DetailedTechnician> findByName(String name, int page, int pageSize) {
        return repository.findByName(name, new PageRequest(page - 1, pageSize,
                Sort.Direction.DESC, "lastLoginAt"));
    }

    public Page<DetailedTechnician> findAll(int page, int pageSize) {
        return repository.findAll(new PageRequest(page - 1, pageSize,
                Sort.Direction.DESC, "lastLoginAt"));
    }


}
