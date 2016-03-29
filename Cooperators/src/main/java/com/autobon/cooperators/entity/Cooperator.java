package com.autobon.cooperators.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lu on 2016/3/7.
 */

@Entity
@Table(name = "t_cooperators")
public class Cooperator{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column private int id;

    @Column private String phone; // 账号

    @Column private String fullname; //企业名称

    @Column private String businessLicense; //营业执照号

    @Column private String corporationName; //法人姓名

    @Column private String corporationIdNo; //法人身份证号

    @Column private String bussinessLicensePic; //营业执照副本照片

    @Column(name = "corporation_id_pic_a")
    private String corporationIdPicA; //法人身份证正面照

    @Column(name = "corporation_id_pic_b")
    private String corporationIdPicB; //法人身份证背面照

    @Column private String longitude; //商户位置经度

    @Column private String latitude; //商户位置纬度

    @Column private String invoiceHeader; //发票抬头

    @Column private String taxIdNo; //纳税识别号

    @Column private String postcode; //邮政编码

    @Column private String province; //省

    @Column private String city; //市

    @Column private String district; //区

    @Column private String address; //详细地址

    @Column private String contact; //联系人姓名

    @Column private String contactPhone; //联系人电话

    @Column private Date lastLoginTime; //上次登录时间

    @Column private String lastLoginIp; //上次登录IP

    @Column private Date createTime; //注册时间

    @Column private int statusCode; //状态 0-未审核 1-审核成功 2-审核失败

    @Column private int orderNum;//商户订单数


    public  Cooperator(){
        this.createTime=new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public String getCorporationIdNo() {
        return corporationIdNo;
    }

    public void setCorporationIdNo(String corporationIdNo) {
        this.corporationIdNo = corporationIdNo;
    }

    public String getBussinessLicensePic() {
        return bussinessLicensePic;
    }

    public void setBussinessLicensePic(String bussinessLicensePic) {
        this.bussinessLicensePic = bussinessLicensePic;
    }

    public String getCorporationIdPicA() {
        return corporationIdPicA;
    }

    public void setCorporationIdPicA(String corporationIdPicA) {
        this.corporationIdPicA = corporationIdPicA;
    }

    public String getCorporationIdPicB() {
        return corporationIdPicB;
    }

    public void setCorporationIdPicB(String corporationIdPicB) {
        this.corporationIdPicB = corporationIdPicB;
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

    public String getInvoiceHeader() {
        return invoiceHeader;
    }

    public void setInvoiceHeader(String invoiceHeader) {
        this.invoiceHeader = invoiceHeader;
    }

    public String getTaxIdNo() {
        return taxIdNo;
    }

    public void setTaxIdNo(String taxIdNo) {
        this.taxIdNo = taxIdNo;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
