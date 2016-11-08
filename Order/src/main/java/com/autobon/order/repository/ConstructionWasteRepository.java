package com.autobon.order.repository;

import com.autobon.order.entity.ConstructionWaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2016/11/3.
 */
@Repository
public interface ConstructionWasteRepository extends JpaRepository<ConstructionWaste, Integer> {
}
