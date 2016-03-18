package com.autobon.order.service;

import com.autobon.order.entity.Construction;
import com.autobon.order.repository.ConstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    // 对指定时期内完成的工单进行金额汇总
    public float sumPayment(int techId, Date from, Date to) {
        return repository.sumPayment(techId, from, to);
    }

    // 对指定时期内完成的工单清算出账
    public int settlePayment(int techId, Date from, Date to) {
        return repository.settlePayment(techId, from, to);
    }

    // 对指定时期内完成的工单完成结算
    public int batchPayoff(int techId, Date from, Date to) {
        return repository.batchPayoff(techId, from, to);
    }

    //修改施工单

}
