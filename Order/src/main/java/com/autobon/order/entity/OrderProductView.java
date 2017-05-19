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
    @Column  private int orderId; //订单ID
    @Column private int constructionProjectId; //施工项目ID
    @Column private int constructionPositionId; //施工部位ID
    @Column private int productId; //产品ID
    @Column private Integer type;//'施工项目'
    @Column private String brand; //'品牌'
    @Column private String code;  //'编码'
    @Column private String model; //'型号'
    @Column private Integer constructionPosition;  //'施工部位'
    @Column private Integer workingHours;   //'工时'
    @Column private Integer constructionCommission;  //'施工提成'
    @Column private Integer starLevel;  //'星级要求'
    @Column private Integer scrapCost; //'报废扣款'
    @Column private Integer warranty; //'质保


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
}
