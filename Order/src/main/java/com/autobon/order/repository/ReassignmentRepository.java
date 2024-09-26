package com.autobon.order.repository;

import com.autobon.order.entity.Reassignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



/**
 * Created by wh on 2016/10/31.
 */

@Repository
public interface ReassignmentRepository extends JpaRepository<Reassignment, Integer> {


    @Query("select r from Reassignment r where 1=1 " +
            " and (?1 is null or r.applicant = ?1)" +
            " and (?2 is null or r.assignedPerson = ?2)" +
            " and (?3 is null or r.status = ?3)")
    Page<Reassignment> get(Integer applicant,
                           Integer assignedPerson,
                           Integer status, Pageable pageable);
}
