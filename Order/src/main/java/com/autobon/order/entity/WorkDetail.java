package com.autobon.order.entity;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by wh on 2016/11/2.
 */
@Entity
@Table(name="t_work_detail")
public class WorkDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "tech_id")
    private int techId;

    @Column(name = "project1")
    private Integer project1;
    @Column(name = "position1")
    private String position1;

    @Column(name = "project2")
    private Integer project2;
    @Column(name = "position2")
    private String position2;

    @Column(name = "project3")
    private Integer project3;
    @Column(name = "position3")
    private String position3;

    @Column(name = "project4")
    private Integer project4;
    @Column(name = "position4")
    private String position4;


    @Column(name = "payment")
    private float payment;

    @Column(name = "pay_status")
    private int payStatus; //支付状态: 0-未出帐, 1-已出账进入月度账单, 2-已转账支付

    @Column(name = "create_date")
    private Date createDate;

    public WorkDetail(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public Integer getProject1() {
        return project1;
    }

    public void setProject1(Integer project1) {
        this.project1 = project1;
    }

    public String getPosition1() {
        return position1;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    public Integer getProject2() {
        return project2;
    }

    public void setProject2(Integer project2) {
        this.project2 = project2;
    }

    public String getPosition2() {
        return position2;
    }

    public void setPosition2(String position2) {
        this.position2 = position2;
    }

    public Integer getProject3() {
        return project3;
    }

    public void setProject3(Integer project3) {
        this.project3 = project3;
    }

    public String getPosition3() {
        return position3;
    }

    public void setPosition3(String position3) {
        this.position3 = position3;
    }

    public Integer getProject4() {
        return project4;
    }

    public void setProject4(Integer project4) {
        this.project4 = project4;
    }

    public String getPosition4() {
        return position4;
    }

    public void setPosition4(String position4) {
        this.position4 = position4;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
