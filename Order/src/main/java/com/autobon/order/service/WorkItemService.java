package com.autobon.order.service;

import com.autobon.order.entity.WorkItem;
import com.autobon.order.repository.WorkItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yuh on 2016/2/29.
 */
@Service
public class WorkItemService {

    @Autowired
    private WorkItemRepository workItemRepository;

    public List<WorkItem> findByOrderType(int orderType) {
        return workItemRepository.findByOrderType(orderType);
    }

    public List<WorkItem> findByOrderTypeAndCarSeat(int orderType, int carSeat) {
        return workItemRepository.findByOrderTypeAndCarSeat(orderType, carSeat);
    }

    public List<WorkItem> getOrderTypes() {
        return workItemRepository.getOrderTypes().stream().map(i -> {
            WorkItem wi = new WorkItem();
            wi.setOrderType(Integer.parseInt(i[0].toString()));
            wi.setOrderTypeName(i[1].toString());
            return wi;
        }).collect(Collectors.toList());
    }


}
