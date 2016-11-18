package com.autobon.technician.vo;

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


}
