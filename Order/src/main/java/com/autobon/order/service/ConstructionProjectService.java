package com.autobon.order.service;

import com.autobon.order.entity.ConstructionPosition;
import com.autobon.order.entity.ConstructionProject;
import com.autobon.order.repository.ConstructionPositionRepository;
import com.autobon.order.repository.ConstructionProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wh on 2016/11/1.
 */

@Service
public class ConstructionProjectService {

    @Autowired
    ConstructionProjectRepository constructionProjectRepository;
    @Autowired
    ConstructionPositionRepository constructionPositionRepository;


    public List<ConstructionPosition> findByName(String name){
        ConstructionProject constructionProject = constructionProjectRepository.findByName(name);
        if(constructionProject!= null){
            String idsStr = constructionProject.getIds();
            String[] ids = idsStr.split(",");
            List<Integer> idList = new ArrayList<>();
            for(int i=0; i<ids.length;i++){
                idList.add(Integer.valueOf(ids[i]));
            }
            return constructionPositionRepository.getByIds(idList);
        }
        return null;
    }
}
