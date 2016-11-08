package com.autobon.technician.repository;

import com.autobon.technician.entity.LocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2016/10/27.
 */
@Repository
public interface LocationStatusRepository extends JpaRepository<LocationStatus,Integer> {

    LocationStatus findByTechId(int techId);
}
