package com.autobon.staff.repository;

import com.autobon.staff.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String Name);

    @Query("select r from Role r " +
            "where (?1 is null or r.name like ?1)")
    Page<Role> find(String name, Pageable pageable);


    @Query(value = "select * from t_role r where r.uid in (select role_id from t_role_staff rs where rs.staff_id = ?1)", nativeQuery = true)
    List<Role> findByUserId(int uid);
}