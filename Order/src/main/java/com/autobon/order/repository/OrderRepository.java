package com.autobon.order.repository;

import com.autobon.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/2/22.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

    //findAllByOrderByPublishTimeDesc

    List<Order> findAllByStatusOrderByAddTimeAsc(int status);

}
