package com.autobon.order.vo;


import java.util.Date;

/**
 * Created by wh on 2016/11/7.
 */
public class WorkDetailShow {

    private Integer id;

    private Integer orderId;

    private Integer techId;

    private String techName;

    private Integer isMainTech = 0;


    private Integer project1;

    private String position1;


    private Integer project2;

    private String position2;


    private Integer project3;

    private String position3;


    private Integer project4;


    private String position4;


    private float payment;


    private int payStatus; //支付状态: 0-未出帐, 1-已出账进入月度账单, 2-已转账支付


    private Date createDate;


    public WorkDetailShow(Object[] objects) {
        this.id = (Integer)objects[0];
        this.orderId = (Integer)objects[1];
        this.techId = (Integer)objects[2];
        this.project1 = (Integer)objects[3];
        this.position1 = (String)objects[4];
        this.project2 = (Integer)objects[5];
        this.position2 = (String)objects[6];
        this.project3 = (Integer)objects[7];
        this.position3 = (String)objects[8];
        this.project4 = (Integer)objects[9];
        this.position4 = (String)objects[10];
        this.payment = (float)objects[11];
        this.payStatus = (Integer)objects[12];
        this.createDate = (Date)objects[13];
        this.techName = (String)objects[14];
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getTechId() {
        return techId;
    }

    public void setTechId(Integer techId) {
        this.techId = techId;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public Integer getIsMainTech() {
        return isMainTech;
    }

    public void setIsMainTech(Integer isMainTech) {
        this.isMainTech = isMainTech;
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
