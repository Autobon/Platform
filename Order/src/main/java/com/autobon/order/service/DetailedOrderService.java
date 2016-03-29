package com.autobon.order.service;

import com.autobon.order.entity.DetailedOrder;
import com.autobon.order.repository.DetailedOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dave on 16/3/10.
 */
@Service
public class DetailedOrderService {
    @Autowired DetailedOrderRepository repository;

    public DetailedOrder get(int id) {
        return repository.findOne(id);
    }

    public DetailedOrder getByOrderNum(String orderNum) {
        return repository.getByOrderNum(orderNum);
    }

    public Page<DetailedOrder> findFinishedByMainTechId(int techId, int page, int pageSize) {
        return repository.findFinishedByMainTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<DetailedOrder> findFinishedBySecondTechId(int techId, int page, int pageSize) {
        return repository.findFinishedBySecondTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<DetailedOrder> findUnfinishedByTechId(int techId, int page, int pageSize) {
        return repository.findUnfinishedByTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    // 查找日期在start至end之间完成施工的订单, 包含start时间点, 不包含end时间点
    public Page<DetailedOrder> findBetweenByTechId(int techId, Date start, Date end, int page, int pageSize) {
        return repository.findBetweenByTechId(techId, start, end,
                new PageRequest(page - 1, pageSize, Sort.Direction.ASC, "id"));
    }

    public Page<DetailedOrder> findAll(int page, int pageSize) {
        return repository.findAll(new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<DetailedOrder> findUnfinishedByCoopId(int coopId, int page, int pageSize) {

        return repository.findUnfinishedByCoopId(coopId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<DetailedOrder> findFinishedByCoopId(int coopId, int page, int pageSize) {

        return repository.findFinishedByCoopId(coopId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<DetailedOrder> findUncommentByCoopId(int coopId, int page, int pageSize) {

        return repository.findUncommentByCoopId(coopId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public long findCountByCreatorId(int coopAccountId) {
        return repository.countByCreatorIdAndCreatorType(coopAccountId, 1);
    }
}
