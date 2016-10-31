package com.autobon.order.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 申请改派表
 * Created by wh on 2016/10/31.
 */

@Entity
@Table(name="t_reassignment")
public class Reassignment {

    private Integer id;
    private String orderId;  //订单ID
    private Integer applicant;  //申请人
    private Integer assignedPerson;  //被指派人
    private Integer status; //指派状态  0 未指派  1已指派
    private int createUser;
    private Date createDate;
    private int modifyUser;
    private Date modifyDate;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getApplicant() {
        return applicant;
    }

    public void setApplicant(Integer applicant) {
        this.applicant = applicant;
    }

    public Integer getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(Integer assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(int modifyUser) {
        this.modifyUser = modifyUser;
    }

}
