package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by wh on 2016/11/1.
 */

@Entity
@Table(name="t_product")
public class Product {

    private int id;//
    private int type;//'施工项目'
    private String brand; //'品牌'
    private String code;  //'编码'
    private String model; //'型号'
    private int constructionPosition;  //'施工部位'
    private int workingHours;   //'工时'
    private int constructionCommission;  //'施工提成'
    private int starLevel;  //'星级要求'
    private int scrapCost; //'报废扣款'
    private int warranty; //'质保


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public int getConstructionPosition() {
        return constructionPosition;
    }

    public void setConstructionPosition(int constructionPosition) {
        this.constructionPosition = constructionPosition;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public int getConstructionCommission() {
        return constructionCommission;
    }

    public void setConstructionCommission(int constructionCommission) {
        this.constructionCommission = constructionCommission;
    }

    public int getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(int starLevel) {
        this.starLevel = starLevel;
    }

    public int getScrapCost() {
        return scrapCost;
    }

    public void setScrapCost(int scrapCost) {
        this.scrapCost = scrapCost;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", type=" + type +
                ", brand='" + brand + '\'' +
                ", code='" + code + '\'' +
                ", model='" + model + '\'' +
                ", constructionPosition=" + constructionPosition +
                ", workingHours=" + workingHours +
                ", constructionCommission=" + constructionCommission +
                ", starLevel=" + starLevel +
                ", scrapCost=" + scrapCost +
                ", warranty=" + warranty +
                '}';
    }
}
