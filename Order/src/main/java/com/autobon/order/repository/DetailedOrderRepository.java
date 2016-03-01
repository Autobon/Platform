package com.autobon.order.repository;

import com.autobon.order.entity.DetailedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dave on 16/3/1.
 */
@Repository
public interface DetailedOrderRepository extends JpaRepository<DetailedOrder, Integer> {

}
