package com.autobon.order.repository;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2016/12/5.
 */
@Repository
public interface OrderViewRepository extends JpaRepository<OrderView, Integer> {


    @Query("select ov from OrderView ov where ov.techId = ?1")
    Page<OrderView> findAllOrder(Integer techId, Pageable pageable);


    @Query("select ov from OrderView ov where ov.techId = ?1 and ov.statusCode>59 and ov.statusCode<71")
    Page<OrderView> findFinishOrder(Integer techId, Pageable pageable);


    @Query("select ov from OrderView ov where ov.techId = ?1 and ov.statusCode<60")
    Page<OrderView> findUnFinishOrder(Integer techId, Pageable pageable);



    @Query(value = "SELECT " +
            " ov.*"+
            " FROM" +
            " t_order_view ov" +
            " LEFT JOIN t_work_detail wd on wd.order_id = ov.id" +
            " where  ov.status>59 and ov.status<71 and ov.tech_id != ?1 and wd.tech_id = ?1 GROUP BY ov.id limit ?2,?3" ,nativeQuery = true)
    List<OrderView> findFinishOrderAsPartner(Integer techId, Integer begin ,Integer size);


    @Query(value = "SELECT " +
            " count(*) " +
            " FROM" +
            " t_order_view ov" +
            " LEFT JOIN t_work_detail wd on wd.order_id = ov.id" +
            " where ov.status>59 and ov.status<71 and ov.tech_id != ?1 and wd.tech_id = ?1 " ,nativeQuery = true)
    int findFinishOrderAsPartnerCount(Integer techId);

    OrderView findById(Integer orderId);

    Page<OrderView> findByStatusCode(Integer statusCode, Pageable pageable);


    @Query("select ov from OrderView ov where ov.coopId = ?1")
    Page<OrderView> findAllCoopOrder(Integer coopId, Pageable pageable);

    @Query("select ov from OrderView ov where ov.coopId = ?1 and ov.statusCode>59 and ov.statusCode<71")
    Page<OrderView> findFinishCoopOrder(Integer coopId, Pageable pageable);

    @Query("select ov from OrderView ov where ov.coopId = ?1 and ov.statusCode<60")
    Page<OrderView> findUnFinishCoopOrder(Integer coopId, Pageable pageable);

    @Query("select ov from OrderView ov where ov.coopId = ?1 and ov.statusCode>59 and ov.statusCode<70")
    Page<OrderView> findUnEvaluateCoopOrder(Integer coopId, Pageable pageable);
}
