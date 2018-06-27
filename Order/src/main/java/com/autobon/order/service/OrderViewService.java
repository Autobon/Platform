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

    public Page<OrderView> findByStatusCode(Integer statusCode, String workType, Integer orderType, Integer order, String lat, String lng, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;

        Sort.Direction direction = Sort.Direction.DESC;
        if(order != null && order == 2){
            direction = Sort.Direction.ASC;
        }else{
            direction = Sort.Direction.DESC;
        }

        Page<OrderView> page;
        if(orderType != null && orderType == 1){    //按预约开始时间排序
            page = orderViewRepository.findByStatusCode(statusCode, workType, new PageRequest(currentPage-1,pageSize, new Sort(direction, "agreedStartTime")));
        }else if(orderType != null && orderType == 2){    //按距离排序
            List<OrderView> list;
            if(order == 2){    //2:顺序
                list = orderViewRepository.findByStatusCode2(statusCode, lat, lng, (currentPage - 1) * pageSize, pageSize);
            }else{           // 1:降序
                list = orderViewRepository.findByStatusCode1(statusCode, lat, lng, (currentPage - 1) * pageSize, pageSize);
            }
            int count = orderViewRepository.findByStatusCodeCount(statusCode);
            page = new PageImpl<>(list, new PageRequest(currentPage-1,pageSize), count);
        }else{
            page = orderViewRepository.findByStatusCode(statusCode, workType, new PageRequest(currentPage-1,pageSize, new Sort(direction, "createTime")));
        }

        return page;
    }


    public Page<OrderView> findCoopOrder(Integer coopId,Integer status, String workDate, String vin, String phone, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
   //     Pageable p = new PageRequest(currentPage-1,pageSize);
        Page<OrderView> page = null;
        if(status == 1){
            //Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
            List<OrderView> list = orderViewRepository.findUnGetCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            int count = orderViewRepository.findUnGetCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(list, new PageRequest(currentPage-1,pageSize), count);
        }else if(status == 2)
        {
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
            List<OrderView> list = orderViewRepository.findWorkingCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            int count = orderViewRepository.findWorkingCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(list, new PageRequest(currentPage-1,pageSize), count);
        }
        else if(status == 3){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
            List<OrderView> list = orderViewRepository.findUnEvaluateCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            int count = orderViewRepository.findUnEvaluateCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(list, new PageRequest(currentPage-1,pageSize), count);
        }
        else if(status == 4){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
            List<OrderView> list = orderViewRepository.findEvaluatedCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            int count = orderViewRepository.findEvaluatedCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(list, new PageRequest(currentPage-1,pageSize), count);
        }
        else if(status == 5){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
            List<OrderView> list = orderViewRepository.findAllCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            int count = orderViewRepository.findAllCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(list, new PageRequest(currentPage-1,pageSize), count);
        }
        return page;
    }



}
