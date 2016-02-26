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

    @Column(name = "rtposition_lon",nullable = true, insertable = true, updatable = true)
    private String rtpositionLon;

    @Column(name = "rtposition_lat",nullable = true, insertable = true, updatable = true)
    private String rtpositionLat;

    @Column(name = "start_time",nullable = true, insertable = true, updatable = true)
    private Date startTime;

    @Column(name = "signin_time",nullable = true, insertable = true, updatable = true)
    private Date signinTime;

    @Column(name = "end_time",nullable = true, insertable = true, updatable = true)
    private Date endTime;

    @Column(name = "before_pic_a",nullable = true, insertable = true, updatable = true)
    private String beforePicA;

    @Column(name = "before_pic_b",nullable = true, insertable = true, updatable = true)
    private String beforePicB;

    @Column(name = "before_pic_c",nullable = true, insertable = true, updatable = true)
    private String beforePicC;

    @Column(name = "after_pic_a",nullable = true, insertable = true, updatable = true)
    private String afterPicA;

    @Column(name = "after_pic_b",nullable = true, insertable = true, updatable = true)
    private String afterPicB;

    @Column(name = "after_pic_c",nullable = true, insertable = true, updatable = true)
    private String afterPicC;

    @Column(name = "after_pic_d",nullable = true, insertable = true, updatable = true)
    private String afterPicD;

    @Column(name = "after_pic_e",nullable = true, insertable = true, updatable = true)
    private String afterPicE;

    @Column(name = "after_pic_f",nullable = true, insertable = true, updatable = true)
    private String afterPicF;

    @Column(name = "payfor",nullable = true, insertable = true, updatable = true)
    private Float payfor;

    @Column(name = "workload",nullable = true, insertable = true, updatable = true)
    private String workload;

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

    public String getBeforePicA() {
        return beforePicA;
    }

    public void setBeforePicA(String beforePicA) {
        this.beforePicA = beforePicA;
    }

    public String getBeforePicB() {
        return beforePicB;
    }

    public void setBeforePicB(String beforePicB) {
        this.beforePicB = beforePicB;
    }

    public String getBeforePicC() {
        return beforePicC;
    }

    public void setBeforePicC(String beforePicC) {
        this.beforePicC = beforePicC;
    }

    public String getAfterPicA() {
        return afterPicA;
    }

    public void setAfterPicA(String afterPicA) {
        this.afterPicA = afterPicA;
    }

    public String getAfterPicB() {
        return afterPicB;
    }

    public void setAfterPicB(String afterPicB) {
        this.afterPicB = afterPicB;
    }

    public String getAfterPicC() {
        return afterPicC;
    }

    public void setAfterPicC(String afterPicC) {
        this.afterPicC = afterPicC;
    }

    public String getAfterPicD() {
        return afterPicD;
    }

    public void setAfterPicD(String afterPicD) {
        this.afterPicD = afterPicD;
    }

    public String getAfterPicE() {
        return afterPicE;
    }

    public void setAfterPicE(String afterPicE) {
        this.afterPicE = afterPicE;
    }

    public String getAfterPicF() {
        return afterPicF;
    }

    public void setAfterPicF(String afterPicF) {
        this.afterPicF = afterPicF;
    }

    public Float getPayfor() {
        return payfor;
    }

    public void setPayfor(Float payfor) {
        this.payfor = payfor;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }
}
