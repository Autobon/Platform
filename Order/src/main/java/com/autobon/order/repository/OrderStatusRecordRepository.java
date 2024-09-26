package com.autobon.order.repository;

import com.autobon.order.entity.OrderStatusRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by tian on 18/6/7.
 */
@Repository
public interface OrderStatusRecordRepository extends JpaRepository<OrderStatusRecord, Integer> {

    List<OrderStatusRecord> findByOrderId(Integer orderId);

    OrderStatusRecord findByOrderIdAndStatus(Integer orderId, Integer status);
}
