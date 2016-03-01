package com.autobon.staff.repository;

import com.autobon.staff.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by liz on 2016/2/18.
 */
public interface StaffRepository extends JpaRepository<Staff,Integer>{

//    Staff findByUsername(String username);
//
//    @Query("select s from Staff s where" +
//            " (?1 is null or s.id = ?1)" +
//            " and (?2 is null or s.username = ?2)" +
//            " and (?3 is null or s.registTime >= ?3)" +
//            " and (?4 is null or s.registTime <= ?4)")
//    Page<Staff> findByKeys(Integer id, String userName, Date beginTime, Date endTime, Pageable p);
//
//    @Query("select s from Staff s where" +
//            " (?1 is null or s.id = ?1)" +
//            " and (?2 is null or s.username like ?2)" +
//            " and (?3 is null or s.registTime >= ?3)" +
//            " and (?4 is null or s.registTime <= ?4)")
//    Page<Staff> findMoreByKeys(Integer id,String userName,Date beginTime,Date endTime,Pageable p);


}
