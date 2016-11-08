package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by wh on 2016/11/1.
 */

@Entity
@Table(name="t_construction_project")
public class ConstructionProject {

    private int id;
    private String name;  //名称
    private String ids;  //施工部位ID 逗号分隔


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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
