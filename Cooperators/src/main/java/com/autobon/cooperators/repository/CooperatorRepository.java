package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.Cooperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lu on 2016/3/7.
 */
@Repository
public interface CooperatorRepository extends JpaRepository<Cooperator, Integer> {

    Cooperator getByShortname(String shortname);

    Cooperator getByContactPhone(String contactPhone);
}
