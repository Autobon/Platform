package com.autobon.order.repository;

import com.autobon.order.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yuh on 2016/3/1.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location,Integer> {

    Page<Location> findByTechId(int techId, Pageable pageable);
}
