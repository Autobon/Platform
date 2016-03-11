package com.autobon.order.repository;

import com.autobon.order.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by dave on 16/3/11.
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    Page<Bill> findByTechId(int techId, Pageable pageable);

    Page<Bill> findByYearMonth(Date yearMonth, Pageable pageable);
}
