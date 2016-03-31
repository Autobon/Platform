package com.autobon.order.repository;

import com.autobon.order.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by dave on 16/3/11.
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query("select b from Bill b " +
            "where (?1 is null or b.billMonth = ?1) " +
            "and (?2 is null or b.paid = ?2) " +
            "and (?3 is null or b.techId = ?3)")
    Page<Bill> find(Date month, Boolean paid, Integer techId, Pageable pageable);

    Page<Bill> findByTechId(int techId, Pageable pageable);

    Page<Bill> findByBillMonth(Date billMonth, Pageable pageable);
}
