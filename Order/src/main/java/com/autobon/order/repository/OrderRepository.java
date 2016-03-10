package com.autobon.order.repository;

import com.autobon.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
    Page<Order> findUnFinishedOrderByTechId(int techId, Pageable pageable);

}
