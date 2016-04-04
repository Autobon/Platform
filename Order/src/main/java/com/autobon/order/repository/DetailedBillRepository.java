package com.autobon.order.repository;

import com.autobon.order.entity.DetailedBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by dave on 16/4/1.
 */
@Repository
public interface DetailedBillRepository extends JpaRepository<DetailedBill, Integer> {
    @Query("select b from DetailedBill b " +
            "where (?1 is null or b.billMonth = ?1) " +
            "and (?2 is null or b.paid = ?2) " +
            "and (?3 is null or b.technician.id = ?3)")
    Page<DetailedBill> find(Date month, Boolean paid, Integer techId, Pageable pageable);
}
