package com.autobon.staff.service;

import com.autobon.staff.entity.Role;
import com.autobon.staff.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/11/20.
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Page<Role> findRoles(String name, int page, int pageSize){

        if (name != null) {
            name = "%" + name + "%";
        }
        return  roleRepository.find(name, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Role findById(int id){
        return roleRepository.findOne(id);
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public Role findByName(String name){
        return roleRepository.findByName(name);
    }

    public void delete(int id){
        roleRepository.delete(id);
    }

}
