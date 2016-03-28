package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.ReviewCooper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/3/14.
 */
@Repository
public interface ReviewCooperRepository extends JpaRepository<ReviewCooper, Integer> {

    List<ReviewCooper> findTop10ByCooperatorsIdOrderByReviewTimeDesc(int coopId);
}
