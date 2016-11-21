package com.autobon.order.vo;

/**
 * Created by wh on 2016/11/7.
 */
public class ProjectPositionShow {

    private String project;
    private String position;

    public ProjectPositionShow(){}

    public ProjectPositionShow(String project, String position) {
        this.project = project;
        this.position = position;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
