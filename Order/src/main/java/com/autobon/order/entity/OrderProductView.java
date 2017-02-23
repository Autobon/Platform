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
    @Column  private int orderId; //����ID
    @Column private int constructionProjectId; //ʩ����ĿID
    @Column private int constructionPositionId; //ʩ����λID
    @Column private int productId; //��ƷID
    @Column private Integer type;//'ʩ����Ŀ'
    @Column private String brand; //'Ʒ��'
    @Column private String code;  //'����'
    @Column private String model; //'�ͺ�'
    @Column private Integer constructionPosition;  //'ʩ����λ'
    @Column private Integer workingHours;   //'��ʱ'
    @Column private Integer constructionCommission;  //'ʩ�����'
    @Column private Integer starLevel;  //'�Ǽ�Ҫ��'
    @Column private Integer scrapCost; //'���Ͽۿ�'
    @Column private Integer warranty; //'�ʱ�


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
