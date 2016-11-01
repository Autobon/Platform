package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by wh on 2016/11/1.
 */


@Entity
@Table(name="t_construction_position")
public class ConstructionPosition {

    private int id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
