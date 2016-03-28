package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.ReviewCooper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yuh on 2016/3/14.
 */
@Repository
public interface ReviewCooperRepository extends JpaRepository<ReviewCooper, Integer> {

    ReviewCooper getByCooperatorsId(int coopId);
}
