package com.autobon.order.service;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.repository.CoopAccountRepository;
import com.autobon.cooperators.repository.CooperatorRepository;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderPartnerView;
import com.autobon.order.entity.OrderView;
import com.autobon.order.entity.WorkDetail;
import com.autobon.order.repository.OrderPartnerViewRepository;
import com.autobon.order.repository.OrderViewRepository;
import com.autobon.order.repository.WorkDetailRepository;
import com.autobon.technician.entity.LocationStatus;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.repository.LocationStatusRepository;
import com.autobon.technician.repository.TechStatRepository;
import com.autobon.technician.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    TechnicianRepository technicianRepository;
    @Autowired
    CooperatorRepository cooperatorRepository;
    @Autowired
    TechStatRepository techStatRepository;
    @Autowired
    LocationStatusRepository locationStatusRepository;
    @Autowired
    WorkDetailRepository workDetailRepository;
    @Autowired
    CoopAccountRepository coopAccountRepository;

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
            Page<Order> page0 = orderViewRepository.findAllOrder(techId, p);
            List<Order> list = page0.getContent();
            List<OrderView> listView = toView(list);
            page = new PageImpl<>(listView, new PageRequest(currentPage-1,pageSize), page0.getTotalElements());
        }else if(status == 2)
        {
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.ASC, "AgreedStartTime"));
            Page<Order> page0 = orderViewRepository.findUnFinishOrder(techId, p);
            List<Order> list = page0.getContent();
            List<OrderView> listView = toView(list);
            page = new PageImpl<>(listView, new PageRequest(currentPage-1,pageSize), page0.getTotalElements());
        }
        else if(status == 3){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "endTime"));
            Page<Order> page0 = orderViewRepository.findFinishOrder(techId, p);
            List<Order> list = page0.getContent();
            List<OrderView> listView = toView(list);
            page = new PageImpl<>(listView, new PageRequest(currentPage-1,pageSize), page0.getTotalElements());
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
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "id"));
            // List<OrderView> list = orderViewRepository.findUnGetCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            Page<Order> page0 = orderViewRepository.findUnGetCoopOrder(coopId, workDate, vin, phone, p);
            List<Order> list = page0.getContent();
            List<OrderView> listView = toView(list);
            // int count = orderViewRepository.findUnGetCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(listView, new PageRequest(currentPage-1,pageSize), page0.getTotalElements());
        }else if(status == 2)
        {
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "id"));
            // List<OrderView> list = orderViewRepository.findWorkingCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            Page<Order> page0 = orderViewRepository.findWorkingCoopOrder(coopId, workDate, vin, phone, p);
            List<Order> list = page0.getContent();
            List<OrderView> listView = toView(list);
            // int count = orderViewRepository.findWorkingCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(listView, new PageRequest(currentPage-1,pageSize), page0.getTotalElements());
        }
        else if(status == 3){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "id"));
            // List<OrderView> list = orderViewRepository.findUnEvaluateCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            Page<Order> page0 = orderViewRepository.findUnEvaluateCoopOrder(coopId, workDate, vin, phone, p);
            List<Order> list = page0.getContent();
            List<OrderView> listView = toView(list);
            // int count = orderViewRepository.findUnEvaluateCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(listView, new PageRequest(currentPage-1,pageSize), page0.getTotalElements());
        }
        else if(status == 4){
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "id"));
            // List<OrderView> list = orderViewRepository.findEvaluatedCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            Page<Order> page0 = orderViewRepository.findEvaluatedCoopOrder(coopId, workDate, vin, phone, p);
            List<Order> list = page0.getContent();
            List<OrderView> listView = toView(list);
            //int count = orderViewRepository.findEvaluatedCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(listView, new PageRequest(currentPage-1,pageSize), page0.getTotalElements());
        }
        else if(status == 5){
            // Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
            Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "id"));
            // List<Order> list = orderViewRepository.findAllCoopOrder(coopId, workDate, vin, phone, (currentPage - 1) * pageSize, pageSize);
            Page<Order> page0 = orderViewRepository.findAllCoopOrder(coopId, workDate, vin, phone, p);
            List<Order> list = page0.getContent();
            List<OrderView> listView = toView(list);
            // int count = orderViewRepository.findAllCoopOrderCount(coopId, workDate, vin, phone);
            page = new PageImpl<>(listView, new PageRequest(currentPage-1,pageSize), page0.getTotalElements());
        }
        return page;
    }


    public Page<OrderView> findAllOrderM(Integer coopId, Integer currentPage, Integer pageSize){
//        Pageable p = new PageRequest(currentPage-1,pageSize, new Sort(Sort.Direction.DESC, "createTime"));
//        return orderViewRepository.findAllOrderM(coopId, p);
        List<OrderView> list = orderViewRepository.findAllOrderM(coopId, (currentPage - 1) * pageSize, pageSize);
        int count = orderViewRepository.findAllOrderMCount(coopId);
        Page<OrderView> page = new PageImpl<>(list, new PageRequest(currentPage-1,pageSize), count);
        return page;
    }

    public List<OrderView> toView(List<Order> list){
        List<OrderView> listView = new ArrayList<>();
        for(Order o : list){
            OrderView orderView = new OrderView();
            orderView.setId(o.getId());
            orderView.setOrderNum(o.getOrderNum());
            orderView.setPhoto(o.getPhoto());
            orderView.setAgreedStartTime(o.getAgreedStartTime());
            orderView.setAgreedEndTime(o.getAgreedEndTime());
            orderView.setStatusCode(o.getStatusCode());
            //orderView.setCreatorType(o.creator());
            orderView.setTechId(o.getMainTechId());
            if(o.getMainTechId() != null){
                Technician technician = technicianRepository.getById(o.getMainTechId());
                if(technician != null){
                    orderView.setTechName(technician.getName());
                    orderView.setTechAvatar(technician.getAvatar());
                    orderView.setTechPhone(technician.getPhone());
                }
            }
            orderView.setBeforePhotos(o.getBeforePhotos());
            orderView.setAfterPhotos(o.getAfterPhotos());
            orderView.setStartTime(o.getStartTime());
            orderView.setEndTime(o.getEndTime());
            orderView.setSignTime(o.getSignTime());
            orderView.setTakenTime(o.getTakenTime());
            orderView.setCreateTime(o.getAddTime());
            orderView.setType(o.getType());
            orderView.setCoopId(o.getCoopId());
            if(o.getCoopId() != null){
                Cooperator cooperator = cooperatorRepository.getOne(o.getCoopId());
                CoopAccount coopAccount = coopAccountRepository.getByCooperatorIdAndIsMain(o.getCoopId(), true);
                if(coopAccount != null && coopAccount.getId() > 0){
                    orderView.setCoopName(coopAccount.getShortname());
                }
                if(cooperator != null){
                    orderView.setCoopName(cooperator.getFullname());
                    orderView.setAddress(cooperator.getAddress());
                    orderView.setLongitude(cooperator.getLongitude());
                    orderView.setLatitude(cooperator.getLatitude());
                }
            }
            orderView.setCreatorId(o.getCreatorId());
            orderView.setCreatorName(o.getCreatorName());
            orderView.setContactPhone(o.getContactPhone());
            orderView.setRemark(o.getRemark());
            if(o.getMainTechId() != null){
                TechStat techStat = techStatRepository.getByTechId(o.getMainTechId());
                if(techStat != null){
                    // orderView.setEvaluateStatus();
                    orderView.setOrderCount(techStat.getTotalOrders());
                    orderView.setEvaluate(techStat.getStarRate());
                }
            }
            orderView.setCancelCount(0);
            orderView.setProductStatus(o.getProductStatus());
            orderView.setReassignmentStatus(o.getReassignmentStatus());
            if(o.getMainTechId() != null){
                WorkDetail workDetail = workDetailRepository.findByOrderIdAndTechId(o.getId(), o.getMainTechId());
                if(workDetail != null){
                    orderView.setPayment(workDetail.getPayment());
                    orderView.setPayStatus(workDetail.getPayStatus());
                }
            }
            if(o.getMainTechId() != null){
                LocationStatus locationStatus = locationStatusRepository.findByTechId(o.getMainTechId());
                if(locationStatus != null){
                    orderView.setTechLongitude(locationStatus.getLng());
                    orderView.setTechLatitude(locationStatus.getLat());
                }
            }
            // orderView.setComment();
            orderView.setVehicleModel(o.getVehicleModel());
            orderView.setRealOrderNum(o.getRealOrderNum());
            orderView.setLicense(o.getLicense());
            orderView.setVin(o.getVin());
            orderView.setCustomerName(o.getCustomerName());
            orderView.setCustomerPhone(o.getCustomerPhone());
            orderView.setTurnover(o.getTurnover());
            orderView.setSalesman(o.getSalesman());
            orderView.setTechnicianRemark(o.getTechnicianRemark());
            orderView.setMakeUpRemark(o.getMakeUpRemark());

            listView.add(orderView);
        }
        return listView;
    }

}
