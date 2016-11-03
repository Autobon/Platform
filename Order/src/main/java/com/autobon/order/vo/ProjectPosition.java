package com.autobon.order.vo;

/**
 * Created by wh on 2016/11/2.
 */
public class ProjectPosition {
    private int project;
    private String position;


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }


    @Override
    public String toString() {
        return "ProjectPosition{" +
                "project=" + project +
                ", position='" + position + '\'' +
                '}';
    }
}
