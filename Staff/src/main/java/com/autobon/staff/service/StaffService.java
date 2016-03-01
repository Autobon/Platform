package com.autobon.staff.service;

import com.autobon.staff.entity.Staff;
import com.autobon.staff.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by liz on 2016/2/18.
 */

@Service
public class StaffService {
    @Autowired
    private StaffRepository repository;

    public Staff get(int id) {
        return repository.findOne(id);
    }

    public Staff save(Staff staff) {
        return repository.save(staff);
    }

    public Staff findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Staff findByPhone(String phone) {
        return repository.findByPhone(phone);
    }

    public Staff findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Page<Staff> findByName(String name, int page, int pageSize) {
        return repository.findByName(name, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

    public Page<Staff> findAll(int page, int pageSize) {
        return repository.findAll(new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

}
