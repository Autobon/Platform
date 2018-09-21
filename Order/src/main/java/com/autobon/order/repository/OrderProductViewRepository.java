package com.autobon.order.repository;

import com.autobon.order.entity.OrderProductView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wh on 2017/2/23.
 */
public interface OrderProductViewRepository extends JpaRepository<OrderProductView, Integer> {

    List<OrderProductView> findByOrderId(int orderId);

    OrderProductView findByOrderIdAndConstructionProjectIdAndConstructionPositionId(int orderId, int project, int position);
}
