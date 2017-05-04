package com.autobon.order.repository;

import com.autobon.order.entity.ConstructionPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2016/11/1.
 */
@Repository
public interface ConstructionPositionRepository extends JpaRepository<ConstructionPosition, Integer> {

    @Query("select p from ConstructionPosition p where p.id in ?1 order by p.id desc")
    List<ConstructionPosition> getByIds(List<Integer> ids);


}
