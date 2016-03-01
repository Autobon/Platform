package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by yuh on 2016/2/29.
 */
@Entity
@Table(name="sys_work")
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Column(name = "order_type", nullable = false, insertable = true, updatable = true)
    private int orderType;

    @Column(name = "order_type_name", nullable = true, insertable = true, updatable = true)
    private String orderTypeName;

    @Column(name = "car_seat", nullable = true, insertable = true, updatable = true)
    private int carSeat;

    @Column(name = "work_name", nullable = true, insertable = true, updatable = true)
    private String workName;

    @Column(name = "price", nullable = true, insertable = true, updatable = true)
    private int price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public int getCarSeat() {
        return carSeat;
    }

    public void setCarSeat(int carSeat) {
        this.carSeat = carSeat;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
