package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by wh on 2016/11/17.
 */
//@Entity
//@Table(name="t_order_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column  private int orderId; //����ID
    @Column private int constructionProjectId; //ʩ����ĿID
    @Column private int constructionPositionId; //ʩ����λID
    @Column private int productId; //��ƷID
    @Column private int constructionCommission;  //'ʩ�����'
    @Column private int scrapCost; //'���Ͽۿ�'

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

    public int getConstructionCommission() {
        return constructionCommission;
    }

    public void setConstructionCommission(int constructionCommission) {
        this.constructionCommission = constructionCommission;
    }

    public int getScrapCost() {
        return scrapCost;
    }

    public void setScrapCost(int scrapCost) {
        this.scrapCost = scrapCost;
    }
}
