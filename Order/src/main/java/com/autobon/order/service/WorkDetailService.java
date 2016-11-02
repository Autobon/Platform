package com.autobon.order.service;

import com.autobon.order.entity.WorkDetail;
import com.autobon.order.repository.WorkDetailRepository;
import com.autobon.technician.vo.ConstructionDetail;
import com.autobon.technician.vo.ConstructionShow;
import com.autobon.technician.vo.ConstructionWaste;
import com.autobon.technician.vo.ProjectPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wh on 2016/11/2.
 */

@Service
public class WorkDetailService {

    @Autowired
    WorkDetailRepository workDetailRepository;

    public int save(WorkDetail workDetail){
        workDetailRepository.save(workDetail);
        return 0;
    }

    public void save(ConstructionShow constructionShow){
        List<ConstructionDetail> constructionDetails = constructionShow.getConstructionDetails();
        for(ConstructionDetail constructionDetail: constructionDetails){
            WorkDetail workDetail = new WorkDetail();
            workDetail.setOrderId(constructionShow.getOrderId());
            workDetail.setTechId(constructionDetail.getTechId());
            List<ProjectPosition> projectPositions = constructionDetail.getProjectPositions();
            for(int i= 0; i<projectPositions.size();i++){
                if(i+1 ==1){
                    workDetail.setProject1(projectPositions.get(i).getProject());
                    workDetail.setPosition1(projectPositions.get(i).getPosition());
                }else if(i+1 ==2){
                    workDetail.setProject2(projectPositions.get(i).getProject());
                    workDetail.setPosition2(projectPositions.get(i).getPosition());
                }
                else if(i+1 ==3){
                    workDetail.setProject3(projectPositions.get(i).getProject());
                    workDetail.setPosition3(projectPositions.get(i).getPosition());
                }else{
                    workDetail.setProject4(projectPositions.get(i).getProject());
                    workDetail.setPosition4(projectPositions.get(i).getPosition());
                }
            }
            workDetailRepository.save(workDetail);
        }
        List<ConstructionWaste> constructionWastes = constructionShow.getConstructionWastes();
    }
}
