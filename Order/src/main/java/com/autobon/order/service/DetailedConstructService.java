package com.autobon.order.service;

import com.autobon.order.repository.DetailedConstructRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by dave on 16/4/5.
 */
@Service
public class DetailedConstructService {
    @Autowired DetailedConstructRepository repository;

    // 对指定技师在指定订单序号范围内完成的工单进行金额汇总
    public Float sumPayment(int techId, Date from, Date to) {
        return repository.sumPayment(techId, from, to);
    }

    // 对指定技师在指定订单序号范围内完成的工单清算出账
    @Transactional
    public int settlePayment(int techId, Date from, Date to) {
        return repository.settlePayment(techId, from, to);
    }

    // 对指定技师在指定订单序号范围内完成的工单完成结算
    @Transactional
    public int batchPayoff(int techId, Date from, Date to) {
        return repository.batchPayoff(techId, from, to);
    }
}
