package com.autobon.technician.repository;

import com.autobon.technician.entity.TechStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dave on 16/3/14.
 */
@Repository
public interface TechStatRepository extends JpaRepository<TechStat, Integer> {
    TechStat getByTechId(int techId);
}
