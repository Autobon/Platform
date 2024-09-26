package com.autobon.staff.vo;

import com.autobon.staff.entity.FunctionCategory;
import com.autobon.staff.entity.Menu;

import java.util.List;

/**
 * Created by wh on 2018/6/27.
 */
public class MenuShow {


    private Integer id;


    private String name;

    private String url;

    private String remark;

    private List<FunctionCategory> functionCategories;

    public MenuShow(){

    }


    public MenuShow(Menu menu, List<FunctionCategory> functionCategories){
        this.id = menu.getId();
        this.name = menu.getName();
        this.url = menu.getUrl();
        this.remark = menu.getRemark();

        this.functionCategories = functionCategories;
    }


    public List<FunctionCategory> getFunctionCategories() {
        return functionCategories;
    }

    public void setFunctionCategories(List<FunctionCategory> functionCategories) {
        this.functionCategories = functionCategories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
