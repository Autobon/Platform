package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by wh on 2017/2/23.
 */

@Entity
@Table(name="t_order_product_view")
public class OrderProductView {


    @Id
    private int id;
    @Column(name = "order_id")  private int orderId;
    @Column private int constructionProjectId;
    @Column private int constructionPositionId;
    @Column private String name;
    @Column private int productId;
    @Column private Integer type;
    @Column private String brand;
    @Column private String code;
    @Column private String model;
    @Column private Integer constructionPosition;
    @Column private Integer workingHours;
    @Column private Integer constructionCommission;
    @Column private Integer starLevel;
    @Column private Integer scrapCost;
    @Column private Integer warranty;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getConstructionProjectId() {
        return constructionProjectId;
    }

    public void setConstructionProjectId(int constructionProjectId) {
        this.constructionProjectId = constructionProjectId;
    }

    public int getConstructionPositionId() {
        return constructionPositionId;
    }

    public void setConstructionPositionId(int constructionPositionId) {
        this.constructionPositionId = constructionPositionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getConstructionPosition() {
        return constructionPosition;
    }

    public void setConstructionPosition(Integer constructionPosition) {
        this.constructionPosition = constructionPosition;
    }

    public Integer getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Integer workingHours) {
        this.workingHours = workingHours;
    }

    public Integer getConstructionCommission() {
        return constructionCommission;
    }

    public void setConstructionCommission(Integer constructionCommission) {
        this.constructionCommission = constructionCommission;
    }

    public Integer getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(Integer starLevel) {
        this.starLevel = starLevel;
    }

    public Integer getScrapCost() {
        return scrapCost;
    }

    public void setScrapCost(Integer scrapCost) {
        this.scrapCost = scrapCost;
    }

    public Integer getWarranty() {
        return warranty;
    }

    public void setWarranty(Integer warranty) {
        this.warranty = warranty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
