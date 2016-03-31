package com.autobon.order.repository;

import com.autobon.order.entity.DetailedOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by dave on 16/3/10.
 */
@Repository
public interface DetailedOrderRepository extends JpaRepository<DetailedOrder, Integer> {

    DetailedOrder getByOrderNum(String orderNum);

    @Query("from DetailedOrder o where o.mainTech.id = ?1 and o.statusCode >= 60")
    Page<DetailedOrder> findFinishedByMainTechId(int techId, Pageable pageable);

    @Query("from DetailedOrder o where o.secondTech.id = ?1 and o.statusCode >= 60")
    Page<DetailedOrder> findFinishedBySecondTechId(int techId, Pageable pageable);

    @Query("from DetailedOrder o where (o.mainTech.id = ?1 or o.secondTech.id = ?1) and o.statusCode < 60")
    Page<DetailedOrder> findUnfinishedByTechId(int techId, Pageable pageable);

    @Query("from DetailedOrder o where (o.mainTech.id = ?1 or o.secondTech.id = ?1) and o.finishTime >= ?2 and o.finishTime < ?3")
    Page<DetailedOrder> findBetweenByTechId(int techId, Date start, Date end, Pageable pageable);

    @Query("from DetailedOrder o where o.creatorId = ?1  and o.statusCode < 60 and o.creatorType = 1")
    Page<DetailedOrder> findUnfinishedByCoopId(int coopId, Pageable pageable);

    @Query("from DetailedOrder o where o.creatorId = ?1  and o.statusCode >= 60 and o.creatorType = 1")
    Page<DetailedOrder> findFinishedByCoopId(int coopId, Pageable pageable);


    @Query("from DetailedOrder o where o.creatorId = ?1  and o.statusCode = 60 and o.creatorType = 1")
    Page<DetailedOrder> findUncommentByCoopId(int coopId, Pageable pageable);

    long countByCreatorIdAndCreatorType(int creatorId,int creatorType);
}
