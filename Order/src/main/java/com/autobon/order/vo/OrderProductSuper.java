package com.autobon.order.vo;

import java.util.List;

/**
 * Created by wh on 2016/11/28.
 */
public class OrderProductSuper {
    private int orderId;
    private String orderNum;
    private List<OrderProductShow> productShowList;

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

    public List<OrderProductShow> getProductShowList() {
        return productShowList;
    }

    public void setProductShowList(List<OrderProductShow> productShowList) {
        this.productShowList = productShowList;
    }
}
