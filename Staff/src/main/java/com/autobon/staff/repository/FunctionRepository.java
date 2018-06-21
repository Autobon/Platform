package com.autobon.staff.repository;


import com.autobon.staff.entity.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wh on 2016/2/25.
 */
public interface FunctionRepository extends JpaRepository<Function, Integer> {

    @Query("select f from Function f where f.id in ?1")
    Page<Function> getFunctions(List<Integer> ids, Pageable p);


    @Query("select f from Function f where f.id in ?1")
    List<Function> getFunctions(List<Integer> ids);

    @Query("select f from Function f where" +
            " (?1 is null or f.id = ?1)" +
            " and (?2 is null or f.functionName = ?2)"+
            " and (?3 is null or f.vendorId = ?3)"+
            " and (?4 is null or f.menuId = ?4)")
    Page<Function> findByKeys(Integer id, String functionName, Integer vendorId, Integer menuId, Pageable p);

    @Query("select f from Function f where f.functionDir = ?1 and f.functionType = ?2")
    Function findByUrlAndMethod(String functionDir, String methodType);

    List<Function> findAll();

    @Query("select f from Function f where f.categoryId in ?1")
    List<Function> findByCategoryIds(List<Integer> categoryIds);
}
