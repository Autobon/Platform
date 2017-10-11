package com.autobon.technician.repository;

import com.autobon.technician.entity.TechCashApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by tian on 17/9/21.
 */
@Repository
public interface TechCashApplyRepository extends JpaRepository<TechCashApply, Integer> {
    TechCashApply getByTechId(int techId);
}
