package com.autobon.order.entity;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by yuh on 2016/2/23.
 */

@Entity
@Table(name="t_construction")
public class Construction {
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

    @Column(name = "work_items")
    private String workItems;

    @Column(name = "work_percent")
    private float workPercent;

    @Column(name = "car_seat",nullable = true, insertable = true, updatable = true)
    private int carSeat;

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
}
