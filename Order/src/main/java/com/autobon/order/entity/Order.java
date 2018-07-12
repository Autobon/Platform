package com.autobon.order.entity;

import com.autobon.shared.VerifyCode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by yuh on 2016/2/22.
 */
@Entity
@Table(name="t_order")
public class Order {
    public enum Status {
        REASSIGNMENT(-20), //申请改派中
        CREATED_TO_APPOINT(-10), // 已创建待指定技师
        NEWLY_CREATED(0), // 已创建并推送, 待技师抢单
        TAKEN_UP(10), // 已有人抢单
        SEND_INVITATION(20), // 已发送合作邀请并等待结果
        INVITATION_ACCEPTED(30), // 合作邀请已接受
        INVITATION_REJECTED(40), // 合作邀请已拒绝
        IN_PROGRESS(50), // 订单进入施工环节中
        SIGNED_IN(55), // 已签到
        AT_WORK(56),   //  施工中
        FINISHED(60), // 订单已结束
        COMMENTED(70), // 订单已评论
        CANCELED(200), // 订单已撤销
        GIVEN_UP(201), // 订单已被放弃
        EXPIRED(210); // 订单已超时
        private Integer statusCode;

        Status(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public static Status getStatus(Integer statusCode) {
            for (Status s : Status.values()) {
                if (s.getStatusCode() == statusCode) return s;
            }
            return null;
        }

        public Integer getStatusCode() {
            return this.statusCode;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column private String orderNum;

    @Column private Integer orderType; // 订单类型(1-隔热膜 2-隐形车衣 3-车身改色 4-美容清洁)

    @Column private String photo;

    @Column private Date orderTime; // 预约时间

    @Column private Date addTime; // 创建时间

    @Column private Date finishTime; // 施工完成时间

    @Column private Date takenTime; // 接单时间

    @JsonIgnore
    @Column(name="status")
    private Integer statusCode;

    @Column private Integer creatorId;

    @Column private Integer coopId;

    @Column private String creatorName;

    @Column private String contactPhone;

    @Column private String positionLon;

    @Column private String positionLat;

    @Column private String remark;

    @Column private Integer mainTechId;

    @Column private Integer secondTechId;

    @Column private String beforePhotos;

    @Column private String afterPhotos;

    @Column  private Date signTime;

    @Column private Date startTime;

    @Column private Date endTime;

    @Column private String type;

    @Column private Date AgreedStartTime;

    @Column private Date AgreedEndTime;

    @Column private Integer productStatus; //订单补录产品状态 0 未补录,1已补录

    @Column private Integer reassignmentStatus;  //申请改派状态 0 未申请改派,1已申请改派 2已处理

    @Column private String vehicleModel;

    @Column private String realOrderNum;

    @Column private String license;

    @Column private String vin;

    @Column private String customerName;

    @Column private String customerPhone;

    @Column private BigDecimal turnover ;

    @Column private String salesman;

    @Column private String technicianRemark;

    @Column private String makeUpRemark;

    public Order() {
        this.orderNum = generateOrderNum();
        this.setStatus(Status.NEWLY_CREATED);
        this.addTime = new Date();
    }

    public static String generateOrderNum() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHH")) +
                VerifyCode.generateVerifyCode(6);
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
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

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(Date takenTime) {
        this.takenTime = takenTime;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getCoopId() {
        return coopId;
    }

    public void setCoopId(Integer coopId) {
        this.coopId = coopId;
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

    public Integer getMainTechId() {
        return mainTechId;
    }

    public void setMainTechId(Integer mainTechId) {
        this.mainTechId = mainTechId;
    }

    public Integer getSecondTechId() {
        return secondTechId;
    }

    public void setSecondTechId(Integer secondTechId) {
        this.secondTechId = secondTechId;
    }

    public Status getStatus() {
        return Status.getStatus(this.statusCode);
    }

    public void setStatus(Status status) {
        this.statusCode = status.getStatusCode();
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

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getAgreedStartTime() {
        return AgreedStartTime;
    }

    public void setAgreedStartTime(Date agreedStartTime) {
        AgreedStartTime = agreedStartTime;
    }

    public Date getAgreedEndTime() {
        return AgreedEndTime;
    }

    public void setAgreedEndTime(Date agreedEndTime) {
        AgreedEndTime = agreedEndTime;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getReassignmentStatus() {
        return reassignmentStatus;
    }

    public void setReassignmentStatus(Integer reassignmentStatus) {
        this.reassignmentStatus = reassignmentStatus;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getRealOrderNum() {
        return realOrderNum;
    }

    public void setRealOrderNum(String realOrderNum) {
        this.realOrderNum = realOrderNum;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getTechnicianRemark() {
        return technicianRemark;
    }

    public void setTechnicianRemark(String technicianRemark) {
        this.technicianRemark = technicianRemark;
    }

    public String getMakeUpRemark() {
        return makeUpRemark;
    }

    public void setMakeUpRemark(String makeUpRemark) {
        this.makeUpRemark = makeUpRemark;
    }
}
