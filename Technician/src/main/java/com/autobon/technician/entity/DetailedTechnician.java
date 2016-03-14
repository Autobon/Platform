package com.autobon.technician.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dave on 16/3/14.
 */
@Entity
@Table(name = "v_technician")
public class DetailedTechnician {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String phone; // 手机号

    @JsonIgnore
    @Column private String password; // 密码

    @Column private String name; // 姓名

    @Column private String gender; // 性别

    @Column private String avatar; // 头像

    @Column private String idNo; // 身份证编号

    @Column private String idPhoto; // 身份证正面照片

    @Column private String bank; // 银行卡归属银行

    @Column private String bankAddress; // 开户行地址

    @Column private String bankCardNo; // 银行卡号码

    @Column private Date verifyAt; // 认证通过日期

    @Column private Date requestVerifyAt; // 申请认证日期

    @Column private String verifyMsg; // 认证审核消息

    @Column private Date lastLoginAt; // 最后登录时间

    @Column private String lastLoginIp; // 最后登录IP

    @Column private Date createAt; // 注册时间

    @Column private String skill; // 技师技能,多个技能ID用逗号拼接而成的字符串

    @Column private String pushId; // 个推客户端ID, 由手机端更新

    @Column private Float starRate; // 星级

    @Column private Float balance; // 帐户余额, 未支付工单总额

    @Column private Integer unpaidOrders; // 未付账订单条数

    @Column private Integer totalOrders; // 订单总数

    @Column private Integer commentCount; // 评论条数

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public Date getVerifyAt() {
        return verifyAt;
    }

    public void setVerifyAt(Date verifyAt) {
        this.verifyAt = verifyAt;
    }

    public Date getRequestVerifyAt() {
        return requestVerifyAt;
    }

    public void setRequestVerifyAt(Date requestVerifyAt) {
        this.requestVerifyAt = requestVerifyAt;
    }

    public String getVerifyMsg() {
        return verifyMsg;
    }

    public void setVerifyMsg(String verifyMsg) {
        this.verifyMsg = verifyMsg;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public Float getStarRate() {
        return starRate;
    }

    public void setStarRate(Float starRate) {
        this.starRate = starRate;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Integer getUnpaidOrders() {
        return unpaidOrders;
    }

    public void setUnpaidOrders(Integer unpaidOrders) {
        this.unpaidOrders = unpaidOrders;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    @JsonIgnore
    @Column(name = "status")
    private int statusCode; // 帐户状态码,请使用getStatus()来获取状态枚举类型值

    public Technician.Status getStatus() {
        return Technician.Status.getStatus(this.getStatusCode());
    }

    public void setStatus(Technician.Status s) {
        this.setStatusCode(s.getStatusCode());
    }

    protected int getStatusCode() {
        return statusCode;
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
