package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.FavoriteTechnicianView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2017/5/16.
 */
@Repository
public interface FavoriteTechnicianViewRepository extends JpaRepository<FavoriteTechnicianView, Integer> {

    @Query("select v from FavoriteTechnicianView v where v.cooperatorId = ?1")
    Page<FavoriteTechnicianView> find(int cooperatorId, Pageable pageable);
}
