package com.autobon.order.vo;

import com.autobon.order.entity.ConstructionWaste;
import com.autobon.order.entity.WorkDetail;

import java.util.List;

/**
 * Created by wh on 2016/11/23.
 */
public class OrderConstructionWasteShow {

    private List<WorkDetail> workDetailList;
    private List<ConstructionWaste> constructionWasteList;

    public List<WorkDetail> getWorkDetailList() {
        return workDetailList;
    }

    public void setWorkDetailList(List<WorkDetail> workDetailList) {
        this.workDetailList = workDetailList;
    }

    public List<ConstructionWaste> getConstructionWasteList() {
        return constructionWasteList;
    }

    public void setConstructionWasteList(List<ConstructionWaste> constructionWasteList) {
        this.constructionWasteList = constructionWasteList;
    }
}
