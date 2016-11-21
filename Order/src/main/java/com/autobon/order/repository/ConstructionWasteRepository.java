package com.autobon.order.repository;

import com.autobon.order.entity.ConstructionWaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2016/11/3.
 */
@Repository
public interface ConstructionWasteRepository extends JpaRepository<ConstructionWaste, Integer> {

    @Query(value = "select cw.id,cw.order_id,cw.tech_id,cw.construction_project,cw.construction_position,cw.total,tech.name from t_construction_waste cw " +
            " left join  t_technician tech on tech.id = cw.tech_id where cw.order_id = ?1",nativeQuery = true)
    List<Object[]> get(int orderID);
}
