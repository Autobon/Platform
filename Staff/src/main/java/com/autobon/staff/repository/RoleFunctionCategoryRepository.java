package com.autobon.staff.repository;


import com.autobon.staff.entity.RoleFunctionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wh on 2016/8/9.
 */
public interface RoleFunctionCategoryRepository extends JpaRepository<RoleFunctionCategory, Integer> {

    RoleFunctionCategory findByRoleId(int roleId);
}
