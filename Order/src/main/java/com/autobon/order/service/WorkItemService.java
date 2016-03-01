package com.autobon.order.service;

import com.autobon.order.repository.WorkItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuh on 2016/2/29.
 */
@Service
public class WorkItemService {

    @Autowired
    private WorkItemRepository workItemRepository;

    public List getWorkListByOrderType(int orderType) {
        List workList = workItemRepository.findByOrderType(orderType);
        return workList;
    }
}
