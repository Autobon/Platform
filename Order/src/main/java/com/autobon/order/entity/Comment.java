package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by yuh on 2016/3/2.
 */
@Entity
@Table(name="t_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Column(name = "technician_id", nullable = true, insertable = true, updatable = true)
    private int technicianId;

    @Column(name = "order_id", nullable = true, insertable = true, updatable = true)
    private int orderId;

    @Column(name = "star", nullable = true, insertable = true, updatable = true)
    private int star;

    @Column(name = "arrive_on_time", nullable = true, insertable = true, updatable = true)
    private boolean arriveOnTime;

    @Column(name = "complete_on_time", nullable = true, insertable = true, updatable = true)
    private boolean completeOnTime;

    @Column(name = "professional", nullable = true, insertable = true, updatable = true)
    private boolean professional;

    @Column(name = "dress_neatly", nullable = true, insertable = true, updatable = true)
    private boolean dressNeatly;

    @Column(name = "car_protect", nullable = true, insertable = true, updatable = true)
    private boolean carProtect;

    @Column(name = "good_attitude", nullable = true, insertable = true, updatable = true)
    private boolean goodAttitude;

    @Column(name = "advice", nullable = true, insertable = true, updatable = true)
    private String advice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public boolean isArriveOnTime() {
        return arriveOnTime;
    }

    public void setArriveOnTime(boolean arriveOnTime) {
        this.arriveOnTime = arriveOnTime;
    }

    public boolean isCompleteOnTime() {
        return completeOnTime;
    }

    public void setCompleteOnTime(boolean completeOnTime) {
        this.completeOnTime = completeOnTime;
    }

    public boolean isProfessional() {
        return professional;
    }

    public void setProfessional(boolean professional) {
        this.professional = professional;
    }

    public boolean isDressNeatly() {
        return dressNeatly;
    }

    public void setDressNeatly(boolean dressNeatly) {
        this.dressNeatly = dressNeatly;
    }

    public boolean isCarProtect() {
        return carProtect;
    }

    public void setCarProtect(boolean carProtect) {
        this.carProtect = carProtect;
    }

    public boolean isGoodAttitude() {
        return goodAttitude;
    }

    public void setGoodAttitude(boolean goodAttitude) {
        this.goodAttitude = goodAttitude;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
