package com.autobon.technician.repository;

import com.autobon.technician.entity.TechFinanceView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by tian on 17/9/14.
 */
@Repository
public interface TechFinanceViewRepository extends JpaRepository<TechFinanceView, Integer> {
    TechFinanceView getByTechId(int techId);

    @Query("select t from TechFinanceView t " +
            "where (?1 is null or t.phone like ?1) " +
            "and (?2 is null or t.name like ?2) ")
    Page<TechFinanceView> find(String phone, String name, Pageable pageable);
}
