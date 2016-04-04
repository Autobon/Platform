package com.autobon.order.repository;

import com.autobon.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by yuh on 2016/2/22.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

    @Query("from Order o where o.mainTechId = ?1 and o.statusCode >= 60")
    Page<Order> findFinishedOrderByMainTechId(int techId, Pageable pageable);

    @Query("from Order o where o.secondTechId = ?1 and o.statusCode >= 60")
    Page<Order> findFinishedOrderBySecondTechId(int techId, Pageable pageable);


    @Query("from Order o where (o.mainTechId = ?1 or o.secondTechId = ?1) and o.statusCode < 60")
    Page<Order> findUnfinishedOrderByTechId(int techId, Pageable pageable);

    @Query("select o from Order o " +
            "where (?1 is null or o.orderNum = ?1) " +
            "and (?2 is null or o.creatorName = ?2) " +
            "and (?3 is null or o.contactPhone = ?3) " +
            "and (?4 is null or o.orderType = ?4) " +
            "and (?5 is null or o.statusCode = ?5)")
    Page<Order> find(String orderNum, String creatorName, String contactPhone,
                     Integer orderType, Integer statusCode, Pageable pageable);

    @Query("select o from Order o where o.statusCode < 50 and o.orderTime <= ?1")
    Page<Order> findExpired(Date before, Pageable pageable);

    @Query("from Order o where (o.mainTechId = ?1 or o.secondTechId = ?1) and o.finishTime >= ?2 and o.finishTime < ?3")
    Page<Order> findBetweenByTechId(int techId, Date start, Date end, Pageable pageable);

}

