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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ty on 2017/9/5.
 */

@Service
public class WorkDetailOrderViewService {

    @Autowired
    WorkDetailOrderViewRepository workDetailOrderViewRepository;

    public List<WorkDetailOrderView> findViews(Date start, Date end, String chooseProjectIds){
        if(chooseProjectIds != null && !chooseProjectIds.equals("")){
            List<String> listIds = Arrays.asList(chooseProjectIds.split(",")).stream().map(s -> s.trim() ).collect(Collectors.toList());

            List<WorkDetailOrderView> list = workDetailOrderViewRepository.findViews(start, end, listIds);
            return list;
        }
        return null;

    }

    public List<WorkDetailOrderView> findViewsByTech(String tech, Date start, Date end, String chooseProjectIds){
        if(chooseProjectIds != null && !chooseProjectIds.equals("")){
            List<String> listIds = Arrays.asList(chooseProjectIds.split(",")).stream().map(s -> s.trim() ).collect(Collectors.toList());

            List<WorkDetailOrderView> list = workDetailOrderViewRepository.findViewsByTech(tech,start, end, listIds);
            return list;
        }
        return null;
    }

    public Page<WorkDetailOrderView> findByIds(String idList, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);

        List<Integer> listIds = Arrays.asList(idList.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        Page<WorkDetailOrderView> page = workDetailOrderViewRepository.findByIds(listIds, p);


        return page;
    }





}
