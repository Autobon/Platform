package com.autobon.staff.service;

import com.autobon.staff.entity.RoleStaff;
import com.autobon.staff.entity.StaffMenu;
import com.autobon.staff.repository.RoleStaffRepository;
import com.autobon.staff.repository.StaffMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/11/20.
 */
@Service
public class RoleStaffService {
    @Autowired
    private RoleStaffRepository repository;
    @Autowired
    private StaffMenuRepository staffMenuRepository;


    public RoleStaff findByStaffId(Integer staffId){ return repository.findByStaffId(staffId);}

    public StaffMenu findMenuByStaffId(Integer staffId){ return staffMenuRepository.findOne(staffId);}

    public void deleteOne(int id){ repository.delete(id); }

    public RoleStaff save(RoleStaff roleStaff){ return repository.save(roleStaff); }
}
