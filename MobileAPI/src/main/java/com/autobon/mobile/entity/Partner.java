package com.autobon.mobile.entity;

import sun.plugin2.message.Message;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by dave on 16/2/5.
 */
@Entity
@Table
public class Partner {
    public enum Status {
        NOTVERIFIED(0), VERIFIED(1), REJECTED(2), BANNED(3);
        private int statusCode;

        Status(int statusCode) {
            this.statusCode = statusCode;
        }

        public static Status getStatus(int statusCode) {
            for (Status s : Status.values()) {
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
    private int id;

    @Column private String email;

    @Column private String password;

    @Column private String title;

    @Column private String province;

    @Column private String city;

    @Column private String distric;

    @Column private String address;

    @Column private String longitude;

    @Column private String latitude;

    @Column private String phone;

    @Column private String logoUrl;

    @Column private String description;

    @Column private Date lastLoginAt;

    @Column private String lastLoginIp;

    @Column private Date createAt;

    @Column(name = "status")
    private int statusCode; //0: 待审核，1：审核通过，2：审核未通过，3：帐户禁用

    public Partner() {
        this.setStatus(Status.NOTVERIFIED);
        this.createAt = new Date();
    }

    public boolean isAvailable() {
        return this.getStatus() == Status.VERIFIED;
    }

    public boolean isActivated() {
        return this.getStatus() != Status.NOTVERIFIED;
    }

    public boolean isBanned() {
        return this.getStatus() == Status.BANNED;
    }

    public void setBanned() {
        this.setStatus(Status.BANNED);
    }

    public void setActived() {
        this.setStatus(Status.VERIFIED);
    }

    public void setDeactived() {
        this.setStatus(Status.NOTVERIFIED);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDistric() {
        return distric;
    }

    public void setDistric(String distric) {
        this.distric = distric;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    protected int getStatusCode() {
        return statusCode;
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Status getStatus() {
        return Status.getStatus(this.getStatusCode());
    }

    public void setStatus(Status s) {
        this.setStatusCode(s.getStatusCode());
    }
}
