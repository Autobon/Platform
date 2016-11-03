package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by wh on 2016/11/2.
 */
@Entity
@Table(name="t_construction_waste")
public class ConstructionWaste {
    private int id;
    private int techId;
    private int orderId;
    private int project;
    private int position;
    private int total;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "tech_id",nullable = true, insertable = true, updatable = true)
    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    @Column(name = "order_id",nullable = true, insertable = true, updatable = true)
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Column(name = "construction_project",nullable = true, insertable = true, updatable = true)
    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    @Column(name = "construction_position",nullable = true, insertable = true, updatable = true)
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Column(name = "total",nullable = true, insertable = true, updatable = true)
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
