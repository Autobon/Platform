package com.autobon.order.repository;

import com.autobon.order.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/3/1.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location,Integer> {


    List<Location> findTop10ByTechnicianIdOrderByAddTimeDesc(int technicianId);
}
