package com.autobon.order.vo;

/**
 * Created by wh on 2016/11/21.
 */
public class ConstructionWasteShow {

    private int id;
    private String techName;
    private int techId;
    private int orderId;
    private int project;
    private int position;
    private int total;


    public ConstructionWasteShow(){}

    public ConstructionWasteShow(Object[] objects){
        this.id = (int)objects[0];
        this.techId = (int)objects[1];
        this.orderId = (int)objects[2];
        this.project = (int)objects[3];
        this.position = (int)objects[4];
        this.total = (int)objects[5];
        this.techName = (String)objects[6];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
