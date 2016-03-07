package com.autobon.cooperators.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lu on 2016/3/7.
 */

@Entity
@Table(name = "t_cooperators")
public class Cooperator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column private int id;

    @Column private String phone; // �˺�

    @Column private String shortname; //��ҵ���

    @JsonIgnore
    @Column private String password; //����

    @Column private String fullname; //��ҵ����

    @Column private String businessLicense; //Ӫҵִ�պ�

    @Column private String corporationName; //��������

    @Column private String corporationIdNo; //�������֤��

    @Column private String bussinessLicensePic; //Ӫҵִ�ո�����Ƭ

    @Column private String corporationIdPicA; //�������֤������

    @Column private String corporationIdPicB; //�������֤������

    @Column private String longitude; //�̻�λ�þ���

    @Column private String latitude; //�̻�λ��γ��

    @Column private String invoiceHeader; //��Ʊ̧ͷ

    @Column private String taxIdNo; //��˰ʶ���

    @Column private String postcode; //��������

    @Column private String province; //ʡ

    @Column private String city; //��

    @Column private String district; //��

    @Column private String address; //��ϸ��ַ

    @Column private String contact; //��ϵ������

    @Column private String contactPhone; //��ϵ�˵绰

    @Column private int statusCode; //״̬ 0-δ��� 1-��˳ɹ� 2-���ʧ�� 3-�˺Ž���

    @Column private Date lastLoginTime; //�ϴε�¼ʱ��

    @Column private String lastLoginIp; //�ϴε�¼IP

    @Column private Date createTime; //ע��ʱ��

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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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
}
