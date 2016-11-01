package com.autobon.order.repository;

import com.autobon.order.entity.ConstructionProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2016/11/1.
 */
@Repository
public interface ConstructionProjectRepository extends JpaRepository<ConstructionProject, Integer> {

    ConstructionProject findByName(String name);
}
