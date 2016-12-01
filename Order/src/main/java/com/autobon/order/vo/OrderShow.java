package com.autobon.order.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by wh on 2016/11/7.
 */
public class OrderShow {

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

    private Integer id;
    private String orderNum;
    private String photo; //订单照片
    private Date AgreedStartTime; //预约开始时间
    private Date AgreedEndTime;// 最晚交车时间
    @JsonIgnore
    private Integer statusCode;// 订单状态 // 0-新建 10-已接单 20-已发出合作邀请等待结果 30-合作人已接受\n      40-合作人已拒绝 50-工作中 60-已完成 70-已评价 200-已撤销
    private Integer creatorType; //下单人类型(1-合作商户 2-后台 3-用户)
    private Integer techId; //接单技师技师ID
    private String techName;//接单技师姓名
    private String techAvatar;
    private String techPhone; //接单技师电话
    private String beforePhotos; //施工前照片
    private String afterPhotos; //施工后照片
    private Date  startTime;  //施工开始时间
    private Date  endTime;    //施工结束时间
    private Date signTime;    //签到时间
    private Date takenTime;  //接单时间
    private Date createTime;  //订单创建时间
    private String type; //'订单类型(1-隔热膜 2-隐形车衣 3-车身改色 4-美容清洁) 支持多个施工项目逗号分隔',
    private Integer coopId;  //商户ID
    private String coopName; //商户名称
    private String address; //商户地址
    private String longitude; // 订单位置经度
    private String latitude;// 订单位置纬度
    private Integer creatorId; //下单人ID
    private String creatorName; //下单人姓名
    private String contactPhone; //联系电话
    private String remark; //订单备注
    private BigInteger evaluateStatus;
    private Integer orderCount;
    private Float evaluate;
    private BigInteger cancelCount;
    private Integer productStatus;
    private Integer reassignmentStatus;



    private List<OrderConstructionShow> orderConstructionShow;
    private String techLongitude;
    private String techLatitude;

    private List<WorkDetailShow> workDetailShows;
    private  List<ConstructionWasteShow> constructionWasteShows;



    public OrderShow() {}

    public OrderShow(Object[] objects) {
        this.id = Integer.valueOf(objects[0].toString());
        this.orderNum = (String)objects[1];
        this.photo = (String)objects[2];
        this.AgreedStartTime = (Date)objects[3];
        this.AgreedEndTime = (Date)objects[4];
        this.statusCode = Integer.valueOf(objects[5].toString());
        this.creatorType =  Integer.valueOf(objects[6].toString());
        this.techId = Integer.valueOf(objects[7].toString());
        this.techName = (String)objects[8];
        this.techPhone = (String)objects[9];
        this.beforePhotos = (String)objects[10];
        this.afterPhotos = (String)objects[11];
        this.startTime = (Date)objects[12];
        this.endTime = (Date)objects[13];
        this.signTime = (Date)objects[14];
        this.takenTime = (Date)objects[15];
        this.createTime = (Date)objects[16];
        this.type = (String)objects[17];
        this.coopId = (Integer)objects[18];
        this.coopName = (String)objects[19];
        this.creatorId = (Integer)objects[20];
        this.creatorName = (String)objects[21];
        this.contactPhone = (String)objects[22];
        this.address = (String) objects[23];
        this.longitude = (String) objects[24];
        this.latitude = (String) objects[25];
        this.remark = (String) objects[26];
        this.productStatus = (Integer) objects[27];
        this.reassignmentStatus = (Integer)objects[28];




    }

    public OrderShow(Object[] objects, int evaluateStatus) {
        this.id = Integer.valueOf(objects[0].toString());
        this.orderNum = (String)objects[1];
        this.photo = (String)objects[2];
        this.AgreedStartTime = (Date)objects[3];
        this.AgreedEndTime = (Date)objects[4];
        this.statusCode = Integer.valueOf(objects[5].toString());
        this.creatorType =  Integer.valueOf(objects[6].toString());
        this.techId = Integer.valueOf(objects[7].toString());
        this.techName = (String)objects[8];
        this.techPhone = (String)objects[9];
        this.beforePhotos = (String)objects[10];
        this.afterPhotos = (String)objects[11];
        this.startTime = (Date)objects[12];
        this.endTime = (Date)objects[13];
        this.signTime = (Date)objects[14];
        this.takenTime = (Date)objects[15];
        this.createTime = (Date)objects[16];
        this.type = (String)objects[17];
        this.coopId = (Integer)objects[18];
        this.coopName = (String)objects[19];
        this.creatorId = (Integer)objects[20];
        this.creatorName = (String)objects[21];
        this.contactPhone = (String)objects[22];
        this.address = (String) objects[23];
        this.longitude = (String) objects[24];
        this.latitude = (String) objects[25];
        this.remark = (String) objects[26];
        this.evaluateStatus = (BigInteger)objects[27];
        this.orderCount = (Integer)objects[28];
        this.evaluate = (Float)objects[29];
        this.cancelCount = (BigInteger)objects[30];
        this.techAvatar = (String)objects[31];
        this.techLongitude =(String)objects[32];
        this.techLatitude = (String)objects[33];
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getTechPhone() {
        return techPhone;
    }

    public void setTechPhone(String techPhone) {
        this.techPhone = techPhone;
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

    public String getCoopName() {
        return coopName;
    }

    public void setCoopName(String coopName) {
        this.coopName = coopName;
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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }


    public List<OrderConstructionShow> getOrderConstructionShow() {
        return orderConstructionShow;
    }

    public void setOrderConstructionShow(List<OrderConstructionShow> orderConstructionShow) {
        this.orderConstructionShow = orderConstructionShow;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTechLongitude() {
        return techLongitude;
    }

    public void setTechLongitude(String techLongitude) {
        this.techLongitude = techLongitude;
    }

    public String getTechLatitude() {
        return techLatitude;
    }

    public void setTechLatitude(String techLatitude) {
        this.techLatitude = techLatitude;
    }

    public List<WorkDetailShow> getWorkDetailShows() {
        return workDetailShows;
    }

    public void setWorkDetailShows(List<WorkDetailShow> workDetailShows) {
        this.workDetailShows = workDetailShows;
    }

    public List<ConstructionWasteShow> getConstructionWasteShows() {
        return constructionWasteShows;
    }

    public void setConstructionWasteShows(List<ConstructionWasteShow> constructionWasteShows) {
        this.constructionWasteShows = constructionWasteShows;
    }

    public BigInteger getEvaluateStatus() {
        return evaluateStatus;
    }

    public void setEvaluateStatus(BigInteger evaluateStatus) {
        this.evaluateStatus = evaluateStatus;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Float getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Float evaluate) {
        this.evaluate = evaluate;
    }

    public BigInteger getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(BigInteger cancelCount) {
        this.cancelCount = cancelCount;
    }

    public String getTechAvatar() {
        return techAvatar;
    }

    public void setTechAvatar(String techAvatar) {
        this.techAvatar = techAvatar;
    }
}
