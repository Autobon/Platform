package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.FavoriteCooperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2017/5/16.
 */
@Repository
public interface FavoriteCooperatorRepository extends JpaRepository<FavoriteCooperator, Integer> {

    FavoriteCooperator findByTechnicianIdAndCooperatorId(int technicianId, int cooperatorId);

}
