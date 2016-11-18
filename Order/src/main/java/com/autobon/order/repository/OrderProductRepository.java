package com.autobon.order.repository;

import com.autobon.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2016/11/18.
 */
@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {

    List<OrderProduct> findByOrderId(int orderId);

}
