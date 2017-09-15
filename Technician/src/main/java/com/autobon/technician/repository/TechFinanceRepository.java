package com.autobon.technician.repository;

import com.autobon.technician.entity.TechFinance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by tian on 17/9/14.
 */
@Repository
public interface TechFinanceRepository extends JpaRepository<TechFinance, Integer> {
    TechFinance getByTechId(int techId);
}
