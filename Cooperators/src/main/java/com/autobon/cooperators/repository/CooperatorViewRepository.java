package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.CooperatorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2017/6/6.
 */
@Repository
public interface CooperatorViewRepository extends JpaRepository<CooperatorView, Integer> {
}
