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

    //findAllByOrderByPublishTimeDesc
//    List<Order> findAllByStatusOrderByAddTimeAsc(int status);

    @Query("from Order o where o.mainTechId = ?1 and o.statusCode >= 60")
    Page<Order> findFinishedOrderByMainTechId(int techId, Pageable pageable);

    @Query("from Order o where o.secondTechId = ?1 and o.statusCode >= 60")
    Page<Order> findFinishedOrderBySecondTechId(int techId, Pageable pageable);


    @Query("from Order o where (o.mainTechId = ?1 or o.secondTechId = ?1) and o.statusCode < 60")
    Page<Order> findUnFinishedOrderByTechId(int techId, Pageable pageable);


    @Query("select od from Order od where 1=1 " +
            " and (?1 is null or od.orderNum = ?1)" +
            " and (?2 is null or od.orderType = ?2)" +
            " and (?3 is null or od.statusCode = ?3)" +
            " and (?4 is null or od.creatorId = ?4)")
    Page<Order> findByKeys(String orderNum,Integer orderType, Integer statusCode, Integer creatorId,Pageable pageRequest);
}
