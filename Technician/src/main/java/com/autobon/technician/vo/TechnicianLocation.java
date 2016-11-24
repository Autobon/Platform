package com.autobon.technician.vo;

import java.math.BigInteger;

/**
 * Created by wh on 2016/11/11.
 */
public class TechnicianLocation {

    private Integer id;
    private String name;
    private String phone;
    private Integer filmLevel;
    private Integer carCoverLevel;
    private Integer colorModifyLevel;
    private Integer beautyLevel;
    private Double distance;
    private Integer status;
    private BigInteger orderCount;
    private BigInteger evaluate;
    private BigInteger cancelCount;
    private Integer filmWorkingSeniority;
    private Integer carCoverWorkingSeniority;
    private Integer colorModifyWorkingSeniority;
    private Integer beautyWorkingSeniority;
    private String avatar;



    public TechnicianLocation(Object[] objects){
        this.id = (Integer)objects[0];
        this.name = (String)objects[1];
        this.phone = (String)objects[2];
        this.filmLevel = (Integer)objects[3];
        this.carCoverLevel = (Integer)objects[4];
        this.colorModifyLevel = (Integer)objects[5];
        this.beautyLevel = (Integer)objects[6];
        this.distance = (Double)objects[7];
        this.status = (Integer)objects[8];
        this.orderCount = (BigInteger)objects[9];
        this.evaluate = (BigInteger)objects[10];
        this.cancelCount = (BigInteger)objects[11];

        this.filmWorkingSeniority = (Integer)objects[12];
        this.carCoverWorkingSeniority = (Integer)objects[13];
        this.colorModifyWorkingSeniority = (Integer)objects[14];
        this.beautyWorkingSeniority = (Integer)objects[15];
        this.avatar = (String)objects[16];
    }

    public TechnicianLocation(Object[] objects ,int ag){
        this.id = (Integer)objects[0];
        this.name = (String)objects[1];
        this.phone = (String)objects[2];
        this.filmLevel = (Integer)objects[3];
        this.carCoverLevel = (Integer)objects[4];
        this.colorModifyLevel = (Integer)objects[5];
        this.beautyLevel = (Integer)objects[6];
        this.orderCount = (BigInteger)objects[7];
        this.evaluate = (BigInteger)objects[8];
        this.cancelCount = (BigInteger)objects[9];
        this.filmWorkingSeniority = (Integer)objects[10];
        this.carCoverWorkingSeniority = (Integer)objects[11];
        this.colorModifyWorkingSeniority = (Integer)objects[12];
        this.beautyWorkingSeniority = (Integer)objects[13];
        this.avatar = (String)objects[14];
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getFilmLevel() {
        return filmLevel;
    }

    public void setFilmLevel(Integer filmLevel) {
        this.filmLevel = filmLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCarCoverLevel() {
        return carCoverLevel;
    }

    public void setCarCoverLevel(Integer carCoverLevel) {
        this.carCoverLevel = carCoverLevel;
    }

    public Integer getColorModifyLevel() {
        return colorModifyLevel;
    }

    public void setColorModifyLevel(Integer colorModifyLevel) {
        this.colorModifyLevel = colorModifyLevel;
    }

    public Integer getBeautyLevel() {
        return beautyLevel;
    }

    public void setBeautyLevel(Integer beautyLevel) {
        this.beautyLevel = beautyLevel;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


    public BigInteger getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(BigInteger orderCount) {
        this.orderCount = orderCount;
    }

    public BigInteger getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(BigInteger evaluate) {
        this.evaluate = evaluate;
    }

    public BigInteger getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(BigInteger cancelCount) {
        this.cancelCount = cancelCount;
    }

    public Integer getFilmWorkingSeniority() {
        return filmWorkingSeniority;
    }

    public void setFilmWorkingSeniority(Integer filmWorkingSeniority) {
        this.filmWorkingSeniority = filmWorkingSeniority;
    }

    public Integer getCarCoverWorkingSeniority() {
        return carCoverWorkingSeniority;
    }

    public void setCarCoverWorkingSeniority(Integer carCoverWorkingSeniority) {
        this.carCoverWorkingSeniority = carCoverWorkingSeniority;
    }

    public Integer getColorModifyWorkingSeniority() {
        return colorModifyWorkingSeniority;
    }

    public void setColorModifyWorkingSeniority(Integer colorModifyWorkingSeniority) {
        this.colorModifyWorkingSeniority = colorModifyWorkingSeniority;
    }

    public Integer getBeautyWorkingSeniority() {
        return beautyWorkingSeniority;
    }

    public void setBeautyWorkingSeniority(Integer beautyWorkingSeniority) {
        this.beautyWorkingSeniority = beautyWorkingSeniority;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
