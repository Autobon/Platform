package com.autobon.order.vo;

import java.util.List;

/**
 * Created by wh on 2016/11/7.
 */
public class OrderConstructionShow {
    private Integer techId;
    private String techName;
    private Integer isMainTech;
    private Integer payStatus;
    private Float  payment;

    private List<ProjectPositionShow> projectPosition;


    public OrderConstructionShow(){

    }

    public Integer getTechId() {
        return techId;
    }

    public void setTechId(Integer techId) {
        this.techId = techId;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public Integer getIsMainTech() {
        return isMainTech;
    }

    public void setIsMainTech(Integer isMainTech) {
        this.isMainTech = isMainTech;
    }

    public List<ProjectPositionShow> getProjectPosition() {
        return projectPosition;
    }

    public void setProjectPosition(List<ProjectPositionShow> projectPosition) {
        this.projectPosition = projectPosition;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }
}
