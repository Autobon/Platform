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

    @Column(name = "technician_id",nullable = false, insertable = true, updatable = true)
    private int technicianId;

    @Column(name = "position_lon",nullable = true, insertable = true, updatable = true)
    private String rtpositionLon;

    @Column(name = "position_lat",nullable = true, insertable = true, updatable = true)
    private String rtpositionLat;

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

    @Column(name = "payment",nullable = true, insertable = true, updatable = true)
    private Float payment;

    @Column(name = "work_items",nullable = true, insertable = true, updatable = true)
    private String workItems;

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

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public String getRtpositionLon() {
        return rtpositionLon;
    }

    public void setRtpositionLon(String rtpositionLon) {
        this.rtpositionLon = rtpositionLon;
    }

    public String getRtpositionLat() {
        return rtpositionLat;
    }

    public void setRtpositionLat(String rtpositionLat) {
        this.rtpositionLat = rtpositionLat;
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

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public String getWorkItems() {
        return workItems;
    }

    public void setWorkItems(String workItems) {
        this.workItems = workItems;
    }

    public int getCarSeat() {
        return carSeat;
    }

    public void setCarSeat(int carSeat) {
        this.carSeat = carSeat;
    }
}
