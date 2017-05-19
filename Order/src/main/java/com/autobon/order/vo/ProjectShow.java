package com.autobon.order.vo;

import java.util.List;

/**
 * Created by wh on 2016/11/29.
 */
public class ProjectShow {
    private int projectId;
    private String projectName;
    private List<OrderProductShow> productShowList;


    public ProjectShow() {

    }

    public List<OrderProductShow> getProductShowList() {
        return productShowList;
    }

    public void setProductShowList(List<OrderProductShow> productShowList) {
        this.productShowList = productShowList;
    }


    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
