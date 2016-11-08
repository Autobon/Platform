package com.autobon.order.vo;



import java.util.Date;
import java.util.List;

/**
 * Created by wh on 2016/11/7.
 */
public class OrderShow {

    private Integer id;
    private String orderNum;
    private String photo; //订单照片
    private Date AgreedStartTime; //预约开始时间
    private Date AgreedEndTime;// 最晚交车时间
    private Integer status;// 订单状态 // 0-新建 10-已接单 20-已发出合作邀请等待结果 30-合作人已接受\n      40-合作人已拒绝 50-工作中 60-已完成 70-已评价 200-已撤销
    private Integer creatorType; //下单人类型(1-合作商户 2-后台 3-用户)
    private Integer techId; //接单技师技师ID
    private String techName;//接单技师姓名
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
    private String longitude; // 商户位置经度
    private String latitude;// 商户位置纬度
    private Integer creatorId; //下单人ID
    private String creatorName; //下单人姓名
    private String contactPhone; //联系电话



    private List<OrderConstructionShow> orderConstructionShow;


    public OrderShow() {}

    public OrderShow(Object[] objects) {
        this.id = Integer.valueOf(objects[0].toString());
        this.orderNum = (String)objects[1];
        this.photo = (String)objects[2];
        this.AgreedStartTime = (Date)objects[3];
        this.AgreedEndTime = (Date)objects[4];
        this.status = Integer.valueOf(objects[5].toString());
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}
