package com.autobon.order.vo;

import java.util.List;

/**
 * Created by wh on 2016/11/28.
 */
public class OrderProductSuper {
    private int orderId;
    private String orderNum;
    private String photo;
    private List<ProjectShow> project;

    public OrderProductSuper() {

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }


    public List<ProjectShow> getProject() {
        return project;
    }

    public void setProject(List<ProjectShow> project) {
        this.project = project;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
