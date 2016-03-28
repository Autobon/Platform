package com.autobon.cooperators.service;

import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.repository.ReviewCooperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuh on 2016/3/14.
 */
@Service
public class ReviewCooperService {
    @Autowired
    private ReviewCooperRepository reviewCooperRepository;

    public void save(ReviewCooper reviewCooper) {
        reviewCooperRepository.save(reviewCooper);
    }

    public ReviewCooper getByCooperatorId(int coopId) {
        return reviewCooperRepository.getByCooperatorId(coopId);
    }
}
