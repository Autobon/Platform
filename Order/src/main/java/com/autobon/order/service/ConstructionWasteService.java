package com.autobon.order.service;

import com.autobon.order.entity.ConstructionWaste;
import com.autobon.order.repository.ConstructionWasteRepository;
import com.autobon.order.vo.ConstructionWasteShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wh on 2016/11/21.
 */
@Service
public class ConstructionWasteService {

    @Autowired
    ConstructionWasteRepository constructionWasteRepository;

    public int save(List<ConstructionWaste> constructionWastes){
        if(constructionWastes!=null&&constructionWastes.size()>0){
            for(ConstructionWaste constructionWaste:constructionWastes){
                constructionWasteRepository.save(constructionWaste);
            }
        }
        return 0;
    }

    public List<ConstructionWasteShow> getByOrderId(int orderId){
        List<Object[]> list = constructionWasteRepository.get(orderId);
        List<ConstructionWasteShow> constructionWasteShows = new ArrayList<>();
        for(Object[] objects : list){
            ConstructionWasteShow constructionWasteShow = new ConstructionWasteShow(objects);
            constructionWasteShows.add(constructionWasteShow);
        }
        return constructionWasteShows;
    }

    public ConstructionWaste getByOrderIdAndTechId(int orderId, int techId){
        return constructionWasteRepository.findByOrderIdAndTechId(orderId,techId);
    }
    public int save(ConstructionWaste constructionWaste){
        constructionWasteRepository.save(constructionWaste);
        return 0;
    }
}
