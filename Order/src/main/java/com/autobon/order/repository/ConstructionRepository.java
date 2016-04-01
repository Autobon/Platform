package com.autobon.order.repository;

import com.autobon.order.entity.Construction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by yuh on 2016/2/23.
 */
@Repository
public interface ConstructionRepository extends JpaRepository<Construction, Integer> {

    Construction getByTechIdAndOrderId(int techId, int orderId);

    // 对指定技师在指定订单序号范围内完成的工单进行金额汇总
    @Query("select sum(payment) as bill from Construction c where c.techId = ?1 " +
            "and c.orderId between ?2 and ?3")
    float sumPayment(int techId, int fromOrderId, int toOrderId);

    // 对指定技师在指定订单序号范围内完成的工单清算出账
    @Modifying
    @Query("update Construction c set c.payStatus = 1 where c.techId = ?1 " +
            "and c.orderId between ?2 and ?3")
    int settlePayment(int techId, int fromOrderId, int toOrderId);

    // 对指定技师在指定订单序号范围内完成的工单完成结算
    @Modifying
    @Query("update Construction c set c.payStatus = 2 where c.techId = ?1 " +
            "and c.orderId between ?2 and ?3")
    int batchPayoff(int techId, int fromOrderId, int toOrderId);

}
