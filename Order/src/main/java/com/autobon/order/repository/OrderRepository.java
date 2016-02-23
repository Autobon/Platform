package com.autobon.order.repository;

import com.autobon.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/2/22.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

    //findAllByOrderByPublishTimeDesc
    List<Order> findAllByStatusOrderByAddTimeAsc(int status);

    @Query("select od from Order od order by id desc")
    Page<Order> findAllOrders(Pageable p);

    @Query("select od from Order od where 1=1 " +
            " and (?1 is null or od.orderNum = ?1)" +
            " and (?2 is null or od.orderType = ?2)" +
            " and (?3 is null or od.status = ?3)" +
            " and (?4 is null or od.customerId = ?4)")
    Page<Order> findByKeys(String orderNum,Integer orderType, Integer status, Integer customerId,Pageable pageRequest);
}
