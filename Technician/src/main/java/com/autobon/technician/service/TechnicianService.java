package com.autobon.technician.service;

import com.autobon.technician.entity.Technician;
import com.autobon.technician.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dave on 16/2/13.
 */
@Service
public class TechnicianService {
    @Autowired
    private TechnicianRepository repository;

    public Technician get(int id) {
        return repository.findOne(id);
    }

    public Technician getByPhone(String phone) {
        return repository.getByPhone(phone);
    }

    public Technician save(Technician technician) {
        return repository.save(technician);
    }

    public void deleteById(int id) {
        repository.delete(id);
    }

    public Page<Technician> findAll(int page, int pageSize) {
        return repository.findAll(new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

    public Page<Technician> findByName(String name, int page, int pageSize) {
        return repository.findByName(name, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

    public Technician getByPushId(String pushId) {
        return repository.getByPushId(pushId);
    }

    public Page<Technician> findActivedFrom(Date date, int page, int pageSize) {
        return repository.findActivedFrom(date, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "lastLoginAt")));
    }

    public Page<Technician> find(String phone, String name, Technician.Status status, int page, int pageSize) {
        Integer statusCode = status == null ? null : status.getStatusCode();
        return repository.find(phone, name, statusCode, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "lastLoginAt")));
    }

    public int countOfNew(Date from, Date to) {
        return repository.countOfNew(from, to);
    }

    public int countOfVerified(Date from, Date to) {
        return repository.countOfVerified();
    }

    public int totalOfRegistered() {
        return repository.totalOfRegistered();
    }

    public int totalOfVerified() {
        return repository.totalOfVerified();
    }
}
