package com.autobon.order.repository;

import com.autobon.order.entity.AgentRebate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2016/12/16.
 */
@Repository
public interface AgentRebateRepository extends JpaRepository<AgentRebate, Integer> {
}
