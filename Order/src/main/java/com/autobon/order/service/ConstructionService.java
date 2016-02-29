package com.autobon.order.service;

import com.autobon.order.entity.Construction;
import com.autobon.order.repository.ConstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuh on 2016/2/29.
 */
@Service
public class ConstructionService {

    private ConstructionRepository constructionRepository = null;
    @Autowired
    public void setConstructionRepository(ConstructionRepository constructionRepository){ this.constructionRepository = constructionRepository; }

    public Construction save(Construction construction) {
        construction = constructionRepository.save(construction);
        return construction;
    }

    public Construction findById(int constructionId) {
        Construction construction = constructionRepository.findOne(constructionId);
        return construction;
    }

    public List<Construction> findByOrderIdAndTechnicianId(int orderId, int mainTechId) {
        List<Construction> constructionList = constructionRepository.findByOrderIdAndTechnicianId(orderId, mainTechId);
        return constructionList;
    }
}
