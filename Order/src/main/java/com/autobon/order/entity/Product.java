package com.autobon.order.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by wh on 2016/11/1.
 */

@Entity
@Table(name="t_product")
public class Product implements Serializable{

    private Integer id;//
    private Integer type;//'施工项目'
    private String brand; //'品牌'
    private String code;  //'编码'
    private String model; //'型号'
    private Integer constructionPosition;  //'施工部位'
    private Integer workingHours;   //'工时'
    private Integer constructionCommission;  //'施工提成'
    private Integer starLevel;  //'星级要求'
    private Integer scrapCost; //'报废扣款'
    private Integer warranty; //'质保


    public Product(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
