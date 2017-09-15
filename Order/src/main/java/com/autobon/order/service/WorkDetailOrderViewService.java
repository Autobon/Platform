package com.autobon.order.service;

import com.autobon.order.entity.OrderView;
import com.autobon.order.entity.WorkDetailOrderView;
import com.autobon.order.repository.WorkDetailOrderViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by ty on 2017/9/5.
 */

@Service
public class WorkDetailOrderViewService {

    @Autowired
    WorkDetailOrderViewRepository workDetailOrderViewRepository;

    public Page<WorkDetailOrderView> findViews(Date start, Date end, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);
        Page<WorkDetailOrderView> page = workDetailOrderViewRepository.findViews(start, end, p);


        return page;
    }

    public List<WorkDetailOrderView> findViewsByTech(String tech, Date start, Date end, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        List<WorkDetailOrderView> page = workDetailOrderViewRepository.findViewsByTech(tech,start, end, currentPage, pageSize);


        return page;
    }





}
