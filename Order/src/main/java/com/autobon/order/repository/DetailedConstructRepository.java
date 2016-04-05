package com.autobon.order.repository;

import com.autobon.order.entity.DetailedConstruct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by dave on 16/4/5.
 */
@Repository
public interface DetailedConstructRepository extends JpaRepository<DetailedConstruct, Integer> {
    // 对指定技师在指定日期内完成的工单进行金额汇总
    @Query("select sum(payment) as bill from DetailedConstruct c where c.techId = ?1 " +
            "and c.order.statusCode >= 60 and c.order.statusCode < 200 and c.order.finishTime >= ?2 and c.order.finishTime < ?3")
    Float sumPayment(int techId, Date from, Date to);

    // 对指定技师在指定日期内完成的工单清算出账
    @Modifying
    @Query("update Construction c set c.payStatus = 1 where c.orderId in " +
            "(select o.id from Order o where (o.mainTechId = ?1 or o.secondTechId = ?1) " +
            "and o.statusCode >= 60 and o.statusCode < 200 " +
            "and o.finishTime >= ?2 and o.finishTime < ?3)")
    int settlePayment(int techId, Date from, Date to);

    // 对指定技师在指定日期内完成的工单完成结算
    @Modifying
    @Query("update Construction c set c.payStatus = 2 where c.orderId in " +
            "(select o.id from Order o where (o.mainTechId = ?1 or o.secondTechId = ?1) " +
            "and o.statusCode >= 60 and o.statusCode < 200 " +
            "and o.finishTime >= ?2 and o.finishTime < ?3)")
    int batchPayoff(int techId, Date from, Date to);
}
