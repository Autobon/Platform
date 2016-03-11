package com.autobon.order.repository;

import com.autobon.order.entity.DetailedOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by dave on 16/3/10.
 */
@Repository
public interface DetailedOrderRepository extends JpaRepository<DetailedOrder, Integer> {

    @Query("from DetailedOrder o where o.mainTech.id = ?1 and o.statusCode >= 60")
    Page<DetailedOrder> findFinishedByMainTechId(int techId, Pageable pageable);

    @Query("from DetailedOrder o where o.secondTech.id = ?1 and o.statusCode >= 60")
    Page<DetailedOrder> findFinishedBySecondTechId(int techId, Pageable pageable);

    @Query("from DetailedOrder o where (o.mainTech.id = ?1 or o.secondTech.id = ?1) and o.statusCode < 60")
    Page<DetailedOrder> findUnfinishedByTechId(int techId, Pageable pageable);
}
