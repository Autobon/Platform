package com.autobon.cooperators.repository;


import com.autobon.cooperators.entity.FavoriteCooperatorView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2017/5/16.
 */
@Repository
public interface FavoriteCooperatorViewRepository extends JpaRepository<FavoriteCooperatorView, Integer> {

    @Query("select v from FavoriteCooperatorView v where v.technicianId = ?1")
    Page<FavoriteCooperatorView> find(int technicianId, Pageable pageable);
}
