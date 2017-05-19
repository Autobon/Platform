package com.autobon.order.service;

import com.autobon.order.entity.OrderPartnerView;
import com.autobon.order.entity.OrderView;
import com.autobon.order.repository.OrderPartnerViewRepository;
import com.autobon.order.repository.OrderViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wh on 2016/12/5.
 */

@Service
public class OrderViewService {

    @Autowired
    OrderViewRepository orderViewRepository;
    @Autowired
    OrderPartnerViewRepository orderPartnerViewRepository;

    public Page<OrderView> find(Integer techId,Integer status, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
     //   Pageable p = new PageRequest(currentPage-1,pageSize);
        Page<OrderView> page = null;
        if(status == 1){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
            page = orderViewRepository.findAllOrder(techId, p);
        }else if(status == 2)
        {
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.ASC, "agreedStartTime"));
            page = orderViewRepository.findUnFinishOrder(techId, p);
        }
        else if(status == 3){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "endTime"));
            page = orderViewRepository.findFinishOrder(techId, p);
        }

        return page;
    }


    public Page<OrderPartnerView> find(Integer techId, Integer currentPage, Integer pageSize){
        Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "endTime"));
        return orderPartnerViewRepository.findByPartnerTechId(techId, p);
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
        Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));

        return orderViewRepository.findByStatusCode(statusCode, p);
    }


    public Page<OrderView> findCoopOrder(Integer coopId,Integer status, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
   //     Pageable p = new PageRequest(currentPage-1,pageSize);
        Page<OrderView> page = null;
        if(status == 1){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
            page = orderViewRepository.findUnFinishCoopOrder(coopId, p);
        }else if(status == 2)
        {
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "endTime"));
            page = orderViewRepository.findFinishCoopOrder(coopId, p);
        }
        else if(status == 3){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "endTime"));
            page = orderViewRepository.findUnEvaluateCoopOrder(coopId, p);
        }
        else if(status == 4){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
            page = orderViewRepository.findAllCoopOrder(coopId, p);
        }
        return page;
    }



}
