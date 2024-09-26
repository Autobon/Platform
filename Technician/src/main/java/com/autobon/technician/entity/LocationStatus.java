package com.autobon.technician.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wanghao on 2016/10/27.
 */
@Entity
@Table(name="t_location_status")
public class LocationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private Date createAt;

    @Column private String lng; // 经度, 东经

    @Column private String lat; // 纬度, 北纬

    @Column private String province;

    @Column private String city;

    @Column private String district;

    @Column private String street;

    @Column private String streetNumber;

    @Column private int techId;

    @Column private int status; //1 可接单 2 工作中 3 休息中



    public LocationStatus(){}

    public LocationStatus(Location location){
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

    public LocationStatus(Object[] objects){
        this.techId = (int)objects[0];
        this.lng =(String)objects[1];
        this.lat =(String)objects[2];
        this.status = (int)objects[3];



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
}
