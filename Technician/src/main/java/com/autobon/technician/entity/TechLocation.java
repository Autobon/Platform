package com.autobon.technician.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dave on 16/4/8.
 */
@Entity
@Table(name = "v_tech_location")
public class TechLocation {
    @Id private int id;

    @Column private Date createAt;

    @Column private String lng; // 经度, 东经

    @Column private String lat; // 纬度, 北纬

    @Column private String province;

    @Column private String city;

    @Column private String district;

    @Column private String street;

    @Column private String streetNumber;

    @ManyToOne
    @JoinColumn(name = "tech_id")
    private Technician technician;

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

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }
}
