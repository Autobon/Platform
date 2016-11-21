package com.autobon.order.service;

import com.autobon.order.entity.Reassignment;
import com.autobon.order.repository.ReassignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by wh on 2016/10/31.
 */

@Service
public class ReassignmentService {

    @Autowired
    ReassignmentRepository reassignmentRepository;

    /**
     * 申请指派
     * @param orderId  订单ID
     * @param applicant 申请人ID
     * @return 0 成功  1 失败
     */
    public int create(String orderId, int applicant){
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
    public int update(int rid, int assignedPerson){
        Reassignment reassignment = reassignmentRepository.findOne(rid);
        if(reassignment != null){
            reassignment.setAssignedPerson(assignedPerson);
            reassignment.setModifyDate(new Date());
            reassignment.setStatus(1);
            reassignmentRepository.save(reassignment);
        }
        return 1;
    }


    /**
     * 查询指派表列表
     * @param orderId  订单ID
     * @param applicant 申请人ID
     * @param status 指派状态 0未指派  1已指派
     * @return
     */
    public Page<Reassignment> get(String orderId, int applicant ,int status){

        return null;
    }

}
