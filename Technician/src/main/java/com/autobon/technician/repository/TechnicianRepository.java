package com.autobon.technician.repository;

import com.autobon.technician.entity.Technician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by dave on 16/2/13.
 */
@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Integer> {
    Technician getByPhone(String phone);

    Page<Technician> findByName(String name, Pageable pageable);

    Technician getByPushId(String pushId);

    @Query("from Technician t where t.lastLoginAt > ?1")
    Page<Technician> findActivedFrom(Date date, Pageable pageable);

    @Query("select t from Technician t " +
            "where (?1 is null or t.phone = ?1) " +
            "and (?2 is null or t.name = ?2) " +
            "and (?3 is null or t.statusCode = ?3)")
    Page<Technician> find(String phone, String name, Integer statusCode, Pageable pageable);

    @Query("select count(t) from Technician t where t.createAt between ?1 and ?2")
    int countOfNew(Date from, Date to);

    @Query("select count(t) from Technician t where t.verifyAt between ?1 and ?2 and t.statusCode = 2")
    int countOfVerified(Date from, Date to);

    @Query("select count(t) from Technician t")
    int totalOfRegistered();

    @Query("select count(t) from Technician t where t.statusCode = 2")
    int totalOfVerified();
}
