package com.autobon.order.repository;

import com.autobon.order.entity.Reassignment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Date;

/**
 * Created by wh on 2016/10/31.
 */

@Repository
public interface ReassignmentRepository extends JpaRepository<Reassignment, Integer> {


//    Page<Reassignment> get(int id,
//                           String orderId,
//                           Integer applicant,
//                           Integer assignedPerson,
//                           Integer status,Date createDateBegin, Date createDateEnd , Pageable pageable);
}
