package com.autobon.order.service;

import com.autobon.order.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuh on 2016/2/29.
 */
@Service
public class WorkService {

    @Autowired
    private WorkRepository workRepository;

    public List getWorkListByOrderType(int orderType) {
        List workList = workRepository.findByOrderType(orderType);
        return workList;
    }
}
