package com.autobon.technician.vo;

/**
 * Created by wh on 2016/11/11.
 */
public class TechnicianLocation {

    private int id;
    private String name;
    private String phone;
    private int filmLevel;
    private int carCoverLevel;
    private int colorModifyLevel;
    private int beautyLevel;
    private double distance;
    private int status;


    public TechnicianLocation(Object[] objects){
        this.id = (int)objects[0];
        this.name = (String)objects[1];
        this.phone = (String)objects[2];
        this.filmLevel = (int)objects[3];
        this.carCoverLevel = (int)objects[4];
        this.colorModifyLevel = (int)objects[5];
        this.beautyLevel = (int)objects[6];
        this.distance = (double)objects[7];
        this.status = (int)objects[8];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getFilmLevel() {
        return filmLevel;
    }

    public void setFilmLevel(int filmLevel) {
        this.filmLevel = filmLevel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


}
