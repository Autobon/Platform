package com.autobon.order.repository;

import com.autobon.order.entity.ConstructionProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2016/11/1.
 */
@Repository
public interface ConstructionProjectRepository extends JpaRepository<ConstructionProject, Integer> {

    ConstructionProject findByName(String name);


    @Query("select cp from ConstructionProject cp where cp.id in ?1")
    List<ConstructionProject> getByIds(List<Integer> ids);

    List<ConstructionProject> findAll();
}
