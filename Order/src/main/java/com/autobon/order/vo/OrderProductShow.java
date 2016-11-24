package com.autobon.order.vo;

import com.autobon.order.entity.Product;

import java.util.List;

/**
 * Created by wh on 2016/11/24.
 */
public class OrderProductShow {

    private int project;
    private int position;
    private List<Product> productList;

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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
