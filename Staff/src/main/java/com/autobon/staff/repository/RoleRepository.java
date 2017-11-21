package com.autobon.staff.repository;

import com.autobon.staff.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/11/20.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String Name);
}
