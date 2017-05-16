package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.FavoriteTechnician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2017/5/16.
 */
@Repository
public interface FavoriteTechnicianRepository extends JpaRepository<FavoriteTechnician, Integer> {

    FavoriteTechnician findByCooperatorIdAndTechnicianId(int cooperatorId, int technicianId);

}
