package com.autobon.technician.repository;

import com.autobon.technician.entity.TechLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by dave on 16/4/8.
 */
@Repository
public interface TechLocationRepository extends JpaRepository<TechLocation, Integer> {

    @Query("from TechLocation tl where tl.id in (select max(l.id) as maxId from Location l where " +
            "(?1 is null or l.province = ?1) and (?2 is null or l.city = ?2) group by l.techId)")
    Page<TechLocation> findByDistinctTech(String province, String city, Pageable pageable);
}
