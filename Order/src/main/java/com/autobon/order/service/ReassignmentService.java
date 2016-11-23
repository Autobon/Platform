package com.autobon.order.service;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.Reassignment;
import com.autobon.order.repository.OrderRepository;
import com.autobon.order.repository.ReassignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by wh on 2016/10/31.
 */

@Service
public class ReassignmentService {

    @Autowired
    ReassignmentRepository reassignmentRepository;
    @Autowired
    OrderRepository orderRepository;

    /**
     * 申请指派
     * @param orderId  订单ID
     * @param applicant 申请人ID
     * @return 0 成功  1 失败
     */
    public int create(Integer orderId, int applicant){
        Reassignment reassignment = new Reassignment();
        reassignment.setOrderId(orderId);
        reassignment.setStatus(0);
        reassignment.setApplicant(applicant);
        reassignment.setCreateDate(new Date());
        reassignment.setCreateUser(applicant);

        reassignmentRepository.save(reassignment);

        return 0;
    }


    /**
     * 后台指派
     * @param rid 指派申请ID
     * @param assignedPerson  被指派人ID
     * @return 0 成功 1 失败
     */
    @Transactional
    public int update(int rid, int assignedPerson){
        Reassignment reassignment = reassignmentRepository.findOne(rid);
        if(reassignment != null){
            reassignment.setAssignedPerson(assignedPerson);
            reassignment.setModifyDate(new Date());
            reassignment.setStatus(1);
            reassignmentRepository.save(reassignment);
            Order order = orderRepository.getOne(reassignment.getOrderId());
            order.setMainTechId(assignedPerson);
            orderRepository.save(order);
        }
        return 1;
    }


    /**
     * 查询指派表列表
     * @param applicant 申请人ID
     * @param assignedPerson 被指派人ID
     * @param status 指派状态 0未指派  1已指派
     * @return
     */
    public Page<Reassignment> get( Integer applicant, Integer assignedPerson,Integer status, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);

        Page<Reassignment> page = reassignmentRepository.get(applicant, assignedPerson, status, p);

        return page;
    }

}
