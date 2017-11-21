package com.autobon.staff.repository;

import com.autobon.staff.entity.RoleStaff;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/11/20.
 */
public interface RoleStaffRepository extends JpaRepository<RoleStaff, Integer> {
    RoleStaff findByStaffId(Integer staffId);
}
