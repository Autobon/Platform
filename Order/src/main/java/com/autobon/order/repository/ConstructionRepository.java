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

    // 对指定时期内完成的工单进行金额汇总
    @Query("select count(payment) as bill from Construction c where c.techId = ?1 " +
            "and c.endTime >=?2 and c.endTime < ?3")
    float sumPayment(int techId, Date from, Date to);

    // 对指定时期内完成的工单清算出账
    @Modifying
    @Query("update Construction c set c.payStatus = 1 where c.techId = ?1 " +
            "and c.endTime >=?2 and c.endTime < ?3")
    int settlePayment(int techId, Date from, Date to);

    // 对指定时期内完成的工单完成结算
    @Modifying
    @Query("update Construction c set c.payStatus = 2 where c.techId = ?1 " +
            "and c.endTime >=?2 and c.endTime < ?3")
    int batchPayoff(int techId, Date from, Date to);

}
