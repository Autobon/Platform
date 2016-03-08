package com.autobon.order.service;

import com.autobon.order.entity.Construction;
import com.autobon.order.repository.ConstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuh on 2016/2/29.
 */
@Service
public class ConstructionService {
    @Autowired
    private ConstructionRepository repository;

    public Construction save(Construction construction) {
        construction = repository.save(construction);
        return construction;
    }

    public Construction get(int id) {
        return repository.findOne(id);
    }

    public Construction getByTechIdAndOrderId(int techId, int orderId) {
        return repository.getByTechIdAndOrderId(techId, orderId);
    }
}
