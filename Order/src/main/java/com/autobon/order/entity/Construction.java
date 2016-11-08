package com.autobon.order.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * Created by yuh on 2016/2/23.
 */

@Entity
@Table(name="t_construction")
public class Construction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Column(name = "order_id",nullable = false, insertable = true, updatable = true)
    private int orderId;

    @Column(name = "tech_id",nullable = false, insertable = true, updatable = true)
    private int techId;

    @Column(name = "position_lon",nullable = true, insertable = true, updatable = true)
    private String positionLon;

    @Column(name = "position_lat",nullable = true, insertable = true, updatable = true)
    private String positionLat;

    @Column(name = "start_time",nullable = true, insertable = true, updatable = true)
    private Date startTime;

    @Column(name = "signin_time",nullable = true, insertable = true, updatable = true)
    private Date signinTime;

    @Column(name = "end_time",nullable = true, insertable = true, updatable = true)
    private Date endTime;

    @Column(name = "before_photos",nullable = true, insertable = true, updatable = true)
    private String beforePhotos;

    @Column(name = "after_photos",nullable = true, insertable = true, updatable = true)
    private String afterPhotos;

    @Column(name = "payment")
    private float payment;

    @Column
    private int payStatus; //支付状态: 0-未出帐, 1-已出账进入月度账单, 2-已转账支付

    @Column(name = "work_items")
    private String workItems;

    @Column(name = "work_percent")
    private float workPercent;

    @Column(name = "car_seat",nullable = true, insertable = true, updatable = true)
    private int carSeat;

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

    public String getPositionLon() {
        return positionLon;
    }

    public void setPositionLon(String positionLon) {
        this.positionLon = positionLon;
    }

    public String getPositionLat() {
        return positionLat;
    }

    public void setPositionLat(String positionLat) {
        this.positionLat = positionLat;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getSigninTime() {
        return signinTime;
    }

    public void setSigninTime(Date signinTime) {
        this.signinTime = signinTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getBeforePhotos() {
        return beforePhotos;
    }

    public void setBeforePhotos(String beforePhotos) {
        this.beforePhotos = beforePhotos;
    }

    public String getAfterPhotos() {
        return afterPhotos;
    }

    public void setAfterPhotos(String afterPhotos) {
        this.afterPhotos = afterPhotos;
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

    public String getWorkItems() {
        return workItems;
    }

    public void setWorkItems(String workItems) {
        this.workItems = workItems;
    }

    public float getWorkPercent() {
        return workPercent;
    }

    public void setWorkPercent(float workPercent) {
        this.workPercent = workPercent;
    }

    public int getCarSeat() {
        return carSeat;
    }

    public void setCarSeat(int carSeat) {
        this.carSeat = carSeat;
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
}
