package com.autobon.staff.repository;


import com.autobon.staff.entity.FunctionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wh on 2016/8/9.
 */
public interface FunctionCategoryRepository extends JpaRepository<FunctionCategory, Integer> {

    List<FunctionCategory> findByMenuId(int menuId);

    @Query("select f from FunctionCategory f where f.id in ?1 and f.isDefault != 1")
    List<FunctionCategory> findByIds(List<Integer> ids);

    @Query("select f from FunctionCategory f where f.menuId in ?1 and f.isDefault = 1")
    List<FunctionCategory> findByMenuIds(List<Integer> menuIds);



}
