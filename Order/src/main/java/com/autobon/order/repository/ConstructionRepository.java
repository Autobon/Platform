package com.autobon.order.repository;

import com.autobon.order.entity.Construction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by yuh on 2016/2/23.
 */
@Repository
public interface ConstructionRepository extends JpaRepository<Construction, Integer> {

    Construction getByTechIdAndOrderId(int techId, int orderId);

    // 对指定技师在指定日期内完成的工单进行金额汇总
    @Query("select sum(c.payment) as bill from Construction c where c.techId = ?1 and c.orderId in " +
            "(select o.id from Order o where o.id  = c.orderId " +
            "and o.statusCode >= 60 and o.statusCode < 200 " +
            "and o.finishTime >= ?2 and o.finishTime < ?3)")
    Float sumPayment(int techId, Date from, Date to);

    // 对指定技师在指定日期内完成的工单清算出账
    @Modifying
    @Query("update Construction c set c.payStatus = 1 where c.techId = ?1 and c.orderId in " +
            "(select o.id from Order o where o.id  = c.orderId " +
            "and o.statusCode >= 60 and o.statusCode < 200 " +
            "and o.finishTime >= ?2 and o.finishTime < ?3)")
    int settlePayment(int techId, Date from, Date to);

    // 对指定技师在指定日期内完成的工单完成结算
    @Modifying
    @Query("update Construction c set c.payStatus = 2 where c.techId = ?1 and c.orderId in " +
            "(select o.id from Order o where o.id  = c.orderId " +
            "and o.statusCode >= 60 and o.statusCode < 200 " +
            "and o.finishTime >= ?2 and o.finishTime < ?3)")
    int batchPayoff(int techId, Date from, Date to);
}
