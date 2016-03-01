package com.autobon.order.repository;

import com.autobon.order.entity.WorkItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/2/29.
 */
@Repository
public interface WorkItemRepository extends JpaRepository<WorkItem, Integer> {

    List<WorkItem> findByOrderType(int orderType);
}
