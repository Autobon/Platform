package com.autobon.order.entity;

import com.autobon.technician.entity.Technician;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dave on 16/3/1.
 */
@Entity
@Table(name = "v_order")
public class DetailedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String orderNum;

    @Column private int orderType;

    @Column private String photo;

    @Column private Date orderTime;

    @Column private Date addTime;

    @Column private int status;

    @Column private int customerType;

    @Column private int customerId;

    @Column private String customerName;

    @Column private String customerLon;

    @Column private String customerLat;

    @Column private String remark;

    @ManyToOne
    @JoinColumn(name = "main_tech_id")
    private Technician mainTech; // 主技师

    @ManyToOne
    @JoinColumn(name = "second_tech_id")
    private Technician secondTech; // 合作技师

    @OneToOne
    private Comment comment;

    @OneToOne
    private Construction mainConstruct;

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

    public Technician getMainTech() {
        return mainTech;
    }

    public void setMainTech(Technician mainTech) {
        this.mainTech = mainTech;
    }

    public Technician getSecondTech() {
        return secondTech;
    }

    public void setSecondTech(Technician secondTech) {
        this.secondTech = secondTech;
    }

    public Order.Status getEnumStatus() {
        return Order.Status.getStatus(this.status);
    }

    public void setEnumStatus(Order.Status status) {
        this.status = status.getStatusCode();
    }
}
