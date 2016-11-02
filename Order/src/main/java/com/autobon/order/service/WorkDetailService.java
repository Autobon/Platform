package com.autobon.order.service;

import com.autobon.order.entity.WorkDetail;
import com.autobon.order.repository.WorkDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wh on 2016/11/2.
 */

@Service
public class WorkDetailService {

    @Autowired
    WorkDetailRepository workDetailRepository;

    public int save(WorkDetail workDetail){
        workDetailRepository.save(workDetail);
        return 0;
    }
}
