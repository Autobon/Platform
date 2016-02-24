package com.autobon.getui.message;

import com.autobon.getui.utils.AppConditions;

import java.util.List;

/**
 * Created by dave on 16/2/23.
 */
public class AppMessage extends Message {
    private List<String> appIdList;
    private AppConditions conditions;
    private int speed;

    public AppMessage() {}

    public List<String> getAppIdList() {
        return this.appIdList;
    }

    public void setAppIdList(List<String> appIdList) {
        this.appIdList = appIdList;
    }

    public AppConditions getConditions() {
        return this.conditions;
    }

    public void setConditions(AppConditions conditions) {
        this.conditions = conditions;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
