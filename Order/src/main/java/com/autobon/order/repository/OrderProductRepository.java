package com.autobon.order.repository;

import com.autobon.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2016/11/18.
 */
@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {

    List<OrderProduct> findByOrderId(int orderId);


    @Query(value = "select sum(op.construction_commission) from t_order_product op where " +
            " op.order_id = ?1 " +
            " and op.construction_project_id = ?2 " +
            " and op.construction_position_id in (?3) ", nativeQuery = true )
    int getMoney(int orderId, int projectId, String positionId);

}
