package com.autobon.order.vo;

import com.autobon.order.entity.Product;

import java.util.List;

/**
 * Created by wh on 2016/11/24.
 */
public class OrderProductShow {


    private int positionId;
    private String positionName;
    private List<ProductShow> productList;


    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public List<ProductShow> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductShow> productList) {
        this.productList = productList;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
