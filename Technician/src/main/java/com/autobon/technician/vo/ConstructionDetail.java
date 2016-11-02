package com.autobon.technician.vo;

import java.util.List;

/**
 * Created by wh on 2016/11/2.
 */
public class ConstructionDetail {


    private int techId;
    private List<ProjectPosition> projectPositions;


    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public List<ProjectPosition> getProjectPositions() {
        return projectPositions;
    }

    public void setProjectPositions(List<ProjectPosition> projectPositions) {
        this.projectPositions = projectPositions;
    }

    @Override
    public String toString() {
        return "ConstructionDetail{" +
                "techId=" + techId +
                ", projectPositions=" + projectPositions +
                '}';
    }
}
