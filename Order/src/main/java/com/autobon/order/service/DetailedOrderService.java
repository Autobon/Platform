package com.autobon.order.service;

import com.autobon.order.entity.DetailedOrder;
import com.autobon.order.repository.DetailedOrderRepository;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by dave on 16/3/10.
 */
@Service
public class DetailedOrderService {
    @Autowired DetailedOrderRepository repository;
//    @Autowired SessionFactory sessionFactory;

    public DetailedOrder get(int id) {
//        return (DetailedOrder) sessionFactory.getCurrentSession().createCriteria(DetailedOrder.class)
//                .setFetchMode("mainTech", FetchMode.JOIN)
//                .setFetchMode("secondTech", FetchMode.JOIN)
//                .setFetchMode("mainConstruct", FetchMode.JOIN)
//                .setFetchMode("secondConstruct", FetchMode.JOIN)
//                .setFetchMode("comment", FetchMode.JOIN)
//                .add(Restrictions.idEq(orderId))
//                .uniqueResult();
        return repository.findOne(id);

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

    public Page<DetailedOrder> findAll(int page, int pageSize) {
        return repository.findAll(new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }
}
