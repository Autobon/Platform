package com.autobon.order.entity;

import com.autobon.order.vo.ConstructionWasteShow;
import com.autobon.order.vo.OrderConstructionShow;
import com.autobon.order.vo.WorkDetailShow;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by ty on 2017/9/1.
 */

@Entity
@Table(name="t_work_detail_order_view")
public class WorkDetailOrderView {

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
        AT_WORK(56),
        FINISHED(60), // 订单已结束
        COMMENTED(70), // 订单已评论
        CANCELED(200), // 订单已撤销
        GIVEN_UP(201), // 订单已被放弃
        EXPIRED(210); // 订单已超时
        private int status;

        Status(int status) {
            this.status = status;
        }

        public static Status getStatus(int status) {
            for (Status s : Status.values()) {
                if (s.getStatusCode() == status) return s;
            }
            return null;
        }

        public int getStatusCode() {
            return this.status;
        }
    }

    @Id
    private Integer id;
    @Column
    private String orderNum;
    @Column
    private String photo; //订单照片
    @Column
    private Date agreedStartTime; //预约开始时间
    @Column
    private Date agreedEndTime;// 最晚交车时间
    @JsonIgnore
    @Column(name="status")
    private Integer statusCode;// 订单状态 // 0-新建 10-已接单 20-已发出合作邀请等待结果 30-合作人已接受\n      40-合作人已拒绝 50-工作中 60-已完成 70-已评价 200-已撤销
    @Column
    private Integer creatorType; //下单人类型(1-合作商户 2-后台 3-用户)
    @Column
    private Integer techId; //接单技师技师ID
    @Column
    private String beforePhotos; //施工前照片
    @Column
    private String afterPhotos; //施工后照片
    @Column
    private Date  startTime;  //施工开始时间
    @Column
    private Date  endTime;    //施工结束时间
    @Column
    private Date signTime;    //签到时间
    @Column
    private Date takenTime;  //接单时间
    @Column
    private Date createTime;  //订单创建时间
    @Column
    private String type; //'订单类型(1-隔热膜 2-隐形车衣 3-车身改色 4-美容清洁) 支持多个施工项目逗号分隔',
    @Column
    private Integer coopId;  //商户ID
    @Column
    private Integer creatorId; //下单人ID
    @Column
    private String creatorName; //下单人姓名
    @Column
    private String remark; //订单备注
    @Column
    private Integer productStatus;
    @Column
    private Integer reassignmentStatus;

    @Column private String vehicleModel;

    @Column private String realOrderNum;

    @Column private String license;

    @Column private String vin;

    @Column private String customerName;

    @Column private String customerPhone;

    @Column private BigDecimal turnover ;

    @Column private String salesman;

    @OneToMany
    @JoinColumn(name = "order_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<WorkDetailView> workDetails;

    @OneToMany
    @JoinColumn(name = "order_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<OrderProductView> orderProducts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getAgreedStartTime() {
        return agreedStartTime;
    }

    public void setAgreedStartTime(Date agreedStartTime) {
        this.agreedStartTime = agreedStartTime;
    }

    public Date getAgreedEndTime() {
        return agreedEndTime;
    }

    public void setAgreedEndTime(Date agreedEndTime) {
        this.agreedEndTime = agreedEndTime;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(Integer creatorType) {
        this.creatorType = creatorType;
    }

    public Integer getTechId() {
        return techId;
    }

    public void setTechId(Integer techId) {
        this.techId = techId;
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

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Date getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(Date takenTime) {
        this.takenTime = takenTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCoopId() {
        return coopId;
    }

    public void setCoopId(Integer coopId) {
        this.coopId = coopId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Status getStatus() {
        return Status.getStatus(this.statusCode);
    }

    public void setStatus(Status status) {
        this.statusCode = status.getStatusCode();
    }

    public List<WorkDetailView> getWorkDetails() {
        return workDetails;
    }

    public void setWorkDetails(List<WorkDetailView> workDetails) {
        this.workDetails = workDetails;
    }

    public List<OrderProductView> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductView> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
