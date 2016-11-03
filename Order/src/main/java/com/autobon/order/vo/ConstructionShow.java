package com.autobon.order.vo;

import com.autobon.order.entity.ConstructionWaste;

import java.util.List;

/**
 * Created by wh on 2016/11/2.
 */
public class ConstructionShow {
    private int orderId;
    private String afterPhotos;
    private List<ConstructionDetail> constructionDetails;
    private List<ConstructionWaste> constructionWastes;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getAfterPhotos() {
        return afterPhotos;
    }

    public void setAfterPhotos(String afterPhotos) {
        this.afterPhotos = afterPhotos;
    }

    public List<ConstructionDetail> getConstructionDetails() {
        return constructionDetails;
    }

    public void setConstructionDetails(List<ConstructionDetail> constructionDetails) {
        this.constructionDetails = constructionDetails;
    }

    public List<ConstructionWaste> getConstructionWastes() {
        return constructionWastes;
    }

    public void setConstructionWastes(List<ConstructionWaste> constructionWastes) {
        this.constructionWastes = constructionWastes;
    }
}
