package com.autobon.order.entity;

import com.autobon.technician.entity.Technician;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

import static com.autobon.order.entity.Order.Status;

/**
 * Created by dave on 16/3/1.
 */
@Entity
@Table(name = "v_order")
public class DetailedOrder {
    @Id
    private int id;

    @Column private String orderNum;

    @Column private int orderType; // 订单类型(1-隔热膜 2-隐形车衣 3-车身改色 4-美容清洁)

    @Column private String photo;

    @Column private Date orderTime;

    @Column private Date addTime;

    @JsonIgnore
    @Column(name="status")
    private int statusCode;

    @Column private int creatorType; // 下单人类型(1-合作商户 2-后台 3-用户)

    @Column private int creatorId;

    @Column private String creatorName;

    @Column private String contactPhone;

    @Column private String positionLon;

    @Column private String positionLat;

    @Column private String remark;

    @ManyToOne
    @JoinColumn(name = "main_tech_id")
    private Technician mainTech; // 主技师

    @ManyToOne
    @JoinColumn(name = "second_tech_id")
    private Technician secondTech; // 合作技师

    @OneToOne
    @JoinColumns({@JoinColumn(name = "id", referencedColumnName = "order_id", insertable = false, updatable = false),
        @JoinColumn(name = "main_tech_id", referencedColumnName = "tech_id", insertable = false, updatable = false)})
    private Construction mainConstruct;

    @OneToOne
    @JoinColumns({@JoinColumn(name = "id", referencedColumnName = "order_id", insertable = false, updatable = false),
            @JoinColumn(name = "second_tech_id", referencedColumnName = "tech_id", insertable = false, updatable = false)})
    private Construction secondConstruct;

    @OneToOne
    @JoinColumns({@JoinColumn(name = "id", referencedColumnName = "order_id", insertable = false, updatable = false),
            @JoinColumn(name = "main_tech_id", referencedColumnName = "tech_id", insertable = false, updatable = false)})
    private Comment comment;

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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(int creatorType) {
        this.creatorType = creatorType;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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

    public Construction getMainConstruct() {
        return mainConstruct;
    }

    public void setMainConstruct(Construction mainConstruct) {
        this.mainConstruct = mainConstruct;
    }

    public Construction getSecondConstruct() {
        return secondConstruct;
    }

    public void setSecondConstruct(Construction secondConstruct) {
        this.secondConstruct = secondConstruct;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Status getStatus() {
        return Status.getStatus(this.statusCode);
    }

    public void setStatus(Order status) {
        this.statusCode = status.getStatusCode();
    }

}
