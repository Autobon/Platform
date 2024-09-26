package com.autobon.technician.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by wh on 2016/11/29.
 */
public class LocationStatusShow {

    public enum certificationStatus {
        NEWLY_CREATED(0), IN_VERIFICATION(1), VERIFIED(2), REJECTED(3), BANNED(4);
        private int statusCode;

        certificationStatus(int statusCode) {
            this.statusCode = statusCode;
        }

        public static certificationStatus getStatus(int statusCode) {
            for (certificationStatus s : certificationStatus.values()) {
                if (s.getStatusCode() == statusCode) return s;
            }
            return null;
        }
        public int getStatusCode() {
            return this.statusCode;
        }
    }


    private int id;

    private Date createAt;

    private String lng; // 经度, 东经

    private String lat; // 纬度, 北纬

    private String province;

    private String city;

    private String district;

    private String street;

    private String streetNumber;

    private int techId;

    private int status; //1 可接单 2 工作中 3 休息中

    private String techName;
    private String phone;
    private String avatar;
    private int filmLevel;
    private int carCoverLevel;
    private int colorModifyLevel;
    private int beautyLevel;
    @JsonIgnore
    private int statusCode;



    public LocationStatusShow(){}

    public LocationStatusShow(LocationStatusShow location){
        this.createAt = location.getCreateAt();
        this.lng = location.getLng();
        this.lat = location.getLat();
        this.province = location.getProvince();
        this.city = location.getCity();
        this.district = location.getDistrict();
        this.street = location.getStreet();
        this.streetNumber = location.getStreetNumber();
        this.techId =location.getTechId();



    }

    public LocationStatusShow(Object[] objects){
        this.techId = (int)objects[0];
        this.lng =(String)objects[1];
        this.lat =(String)objects[2];
        this.status = (int)objects[3];
        this.techName = (String) objects[4];
        this.phone = (String) objects[5];
        this.avatar = (String) objects[6];

        this.filmLevel = (int)objects[7];
        this.carCoverLevel = (int)objects[8];
        this.colorModifyLevel = (int)objects[9];
        this.beautyLevel = (int)objects[10];
        this.statusCode = (int)objects[11];
    }
    public certificationStatus getCertificationStatus() {
        return certificationStatus.getStatus(this.getStatusCode());
    }

    public void setCertificationStatus(certificationStatus s) {
        this.setStatusCode(s.getStatusCode());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFilmLevel() {
        return filmLevel;
    }

    public void setFilmLevel(int filmLevel) {
        this.filmLevel = filmLevel;
    }

    public int getCarCoverLevel() {
        return carCoverLevel;
    }

    public void setCarCoverLevel(int carCoverLevel) {
        this.carCoverLevel = carCoverLevel;
    }

    public int getColorModifyLevel() {
        return colorModifyLevel;
    }

    public void setColorModifyLevel(int colorModifyLevel) {
        this.colorModifyLevel = colorModifyLevel;
    }

    public int getBeautyLevel() {
        return beautyLevel;
    }

    public void setBeautyLevel(int beautyLevel) {
        this.beautyLevel = beautyLevel;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
