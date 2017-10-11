package com.autobon.technician.service;

import com.autobon.technician.entity.TechCashApply;
import com.autobon.technician.entity.TechCashApplyView;
import com.autobon.technician.repository.TechCashApplyRepository;
import com.autobon.technician.repository.TechCashApplyViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by tian on 17/9/14.
 */
@Service
public class TechCashApplyService {
    @Autowired
    TechCashApplyViewRepository techCashApplyViewRepository;
    @Autowired
    TechCashApplyRepository techCashApplyRepository;

    public Page<TechCashApplyView> find(String techName, int techId, int page, int pageSize) {
        if(techName != null){
            techName = "%"+techName+"%";
        }


        return techCashApplyViewRepository.find(techName, techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.ASC, "id")));

    }

    public TechCashApply save(TechCashApply techCashApply){
        return techCashApplyRepository.save(techCashApply);
    }

    public TechCashApply findById(int id){
        return techCashApplyRepository.findOne(id);
    }

    public void deleteById(int id){
        techCashApplyRepository.delete(id);
    }
}
