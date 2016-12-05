package com.autobon.order.service;

import com.autobon.order.entity.OrderView;
import com.autobon.order.repository.OrderViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wh on 2016/12/5.
 */

@Service
public class OrderViewService {

    @Autowired
    OrderViewRepository orderViewRepository;

    public Page<OrderView> find(Integer techId,Integer status, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);
        Page<OrderView> page = null;
        if(status == 1){
            page = orderViewRepository.findAllOrder(techId, p);
        }else if(status == 2)
        {
            page = orderViewRepository.findUnFinishOrder(techId, p);
        }
        else if(status == 3){
            page = orderViewRepository.findFinishOrder(techId, p);
        }
        else if(status == 4){
            List<OrderView> orderViewList = orderViewRepository.findFinishOrderAsPartner(techId,(currentPage - 1) * pageSize, pageSize );
            int count = orderViewRepository.findFinishOrderAsPartnerCount(techId);
            page = new PageImpl<>(orderViewList, p,count);
        }
        return page;
    }

    public OrderView findById(Integer orderId){

        return orderViewRepository.findById(orderId);
    }

    public Page<OrderView> findByStatusCode(Integer statusCode, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);

        return orderViewRepository.findByStatusCode(statusCode, p);
    }


    public Page<OrderView> findCoopOrder(Integer coopId,Integer status, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);
        Page<OrderView> page = null;
        if(status == 1){
            page = orderViewRepository.findUnFinishCoopOrder(coopId, p);
        }else if(status == 2)
        {
            page = orderViewRepository.findFinishCoopOrder(coopId, p);
        }
        else if(status == 3){
            page = orderViewRepository.findUnEvaluateCoopOrder(coopId, p);
        }
        else if(status == 4){
            page = orderViewRepository.findAllCoopOrder(coopId, p);
        }
        return page;
    }



}
