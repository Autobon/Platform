package com.autobon.technician.repository;

import com.autobon.technician.entity.TechCashApplyView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by tian on 17/9/21.
 */
@Repository
public interface TechCashApplyViewRepository extends JpaRepository<TechCashApplyView, Integer> {
    TechCashApplyView getByTechId(int techId);

    @Query("select t from TechCashApplyView t " +
            "where (?1 is null or t.techName like ?1) and (?2 is null or t.techId = ?2) and (?3 is null or t.state = ?3)")
    Page<TechCashApplyView> find(String techName, Integer techId, Integer state, Pageable pageable);
}
