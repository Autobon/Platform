package com.autobon.staff.service;

import com.autobon.staff.entity.RoleMenu;
import com.autobon.staff.repository.RoleMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/11/20.
 */
@Service
public class RoleMenuService {
    @Autowired
    RoleMenuRepository roleMenuRepository;

    public RoleMenu save(RoleMenu roleMenu){
        return roleMenuRepository.save(roleMenu);
    }

    public RoleMenu findByRid(int rid){
        return roleMenuRepository.findByRoleId(rid);
    }

    public void delete(int id){
        roleMenuRepository.delete(id);
    }
}
