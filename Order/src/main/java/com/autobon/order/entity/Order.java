package com.autobon.order.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yuh on 2016/2/22.
 */
@Entity
@Table(name="t_order")
public class Order {
    public enum EnumStatus {
        INVITATION_NOT_ACCEPTED(0), INVITATION_ACCEPTED(1), INVITATION_REJECT(-1),
        IN_PROGRESS(2), FINISHED(3), COMMENTED(4), CANCELED(5);
        private int statusCode;

        EnumStatus(int statusCode) {
            this.statusCode = statusCode;
        }

        public static EnumStatus getStatus(int statusCode) {
            for (EnumStatus s : EnumStatus.values()) {
                if (s.getStatusCode() == statusCode) return s;
            }
            return null;
        }
        public int getStatusCode() {
            return this.statusCode;
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Column(name="order_num",nullable = false, insertable = true, updatable = true)
    private String orderNum;

    @Column(name="order_type",nullable = true, insertable = true, updatable = true)
    private int orderType;

    @Column(name="photo",nullable = true, insertable = true, updatable = true)
    private String photo;

    @Column(name="order_time",nullable = true, insertable = true, updatable = true)
    private Date orderTime;

    @Column(name="add_time",nullable = true, insertable = true, updatable = true)
    private Date addTime;

    @Column(name="status",nullable = true, insertable = true, updatable = true)
    private int status;

    @Column(name="customer_type",nullable = true, insertable = true, updatable = true)
    private int customerType;

    @Column(name="customer_id",nullable = true, insertable = true, updatable = true)
    private int customerId;

    @Column(name="customer_name",nullable = true, insertable = true, updatable = true)
    private String customerName;

    @Column(name="customer_lon",nullable = true, insertable = true, updatable = true)
    private String customerLon;

    @Column(name="customer_lat",nullable = true, insertable = true, updatable = true)
    private String customerLat;

    @Column(name="remark",nullable = true, insertable = true, updatable = true)
    private String remark;

    @Column(name="main_tech_id",nullable = true, insertable = true, updatable = true)
    private int mainTechId;

    @Column(name="second_tech_id",nullable = true, insertable = true, updatable = true)
    private int secondTechId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLon() {
        return customerLon;
    }

    public void setCustomerLon(String customerLon) {
        this.customerLon = customerLon;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMainTechId() {
        return mainTechId;
    }

    public void setMainTechId(int mainTechId) {
        this.mainTechId = mainTechId;
    }

    public int getSecondTechId() {
        return secondTechId;
    }

    public void setSecondTechId(int secondTechId) {
        this.secondTechId = secondTechId;
    }

    public EnumStatus getEnumStatus() {
        return EnumStatus.getStatus(this.status);
    }

    public void setEnumStatus(EnumStatus status) {
        this.status = status.getStatusCode();
    }
}
