package com.autobon.order.vo;

import com.autobon.order.entity.ConstructionPosition;

import java.util.List;

/**
 * Created by wh on 2016/11/4.
 */
public class ConstructionProjectShow {

    private int id;
    private String name;  //名称
    private List<ConstructionPosition> constructionPositions;

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

    public List<ConstructionPosition> getConstructionPositions() {
        return constructionPositions;
    }

    public void setConstructionPositions(List<ConstructionPosition> constructionPositions) {
        this.constructionPositions = constructionPositions;
    }
}
