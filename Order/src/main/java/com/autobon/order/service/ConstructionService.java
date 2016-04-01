package com.autobon.order.service;

import com.autobon.order.entity.Construction;
import com.autobon.order.repository.ConstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 对指定技师在指定订单序号范围内完成的工单进行金额汇总
    public float sumPayment(int techId, int fromOrderId, int toOrderId) {
        return repository.sumPayment(techId, fromOrderId, toOrderId);
    }

    // 对指定技师在指定订单序号范围内完成的工单清算出账
    @Transactional
    public int settlePayment(int techId, int fromOrderId, int toOrderId) {
        return repository.settlePayment(techId, fromOrderId, toOrderId);
    }

    // 对指定技师在指定订单序号范围内完成的工单完成结算
    @Transactional
    public int batchPayoff(int techId, int fromOrderId, int toOrderId) {
        return repository.batchPayoff(techId, fromOrderId, toOrderId);
    }

}
