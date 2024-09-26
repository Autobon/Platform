package com.autobon.cooperators.service;

import com.autobon.cooperators.entity.FavoriteCooperator;
import com.autobon.cooperators.entity.FavoriteCooperatorView;
import com.autobon.cooperators.repository.FavoriteCooperatorRepository;
import com.autobon.cooperators.repository.FavoriteCooperatorViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by wh on 2017/5/16.
 */
@Service
public class FavoriteCooperatorService {

    @Autowired
    FavoriteCooperatorRepository favoriteCooperatorRepository;
    @Autowired
    FavoriteCooperatorViewRepository favoriteCooperatorViewRepository;

    public FavoriteCooperator find(int technicianId, int cooperatorId){

        return favoriteCooperatorRepository.findByTechnicianIdAndCooperatorId(technicianId, cooperatorId);

    }

    public int save(int technicianId, int cooperatorId){

        if(favoriteCooperatorRepository.findByTechnicianIdAndCooperatorId(technicianId, cooperatorId) != null){
            return 1;
        }

        FavoriteCooperator favoriteCooperator = new FavoriteCooperator();
        favoriteCooperator.setCooperatorId(cooperatorId);
        favoriteCooperator.setTechnicianId(technicianId);
        favoriteCooperator.setCreateTime(new Date());
        favoriteCooperatorRepository.save(favoriteCooperator);
        return 0;
    }

    public int delete(int technicianId, int cooperatorId){
        FavoriteCooperator favoriteCooperator = favoriteCooperatorRepository.findByTechnicianIdAndCooperatorId(technicianId, cooperatorId);
        if(favoriteCooperator == null){
            return 1;
        }
        favoriteCooperatorRepository.delete(favoriteCooperator);
        return 0;
    }

    public Page<FavoriteCooperatorView> find(int technicianId, int currentPage, int pageSize){


        Pageable pageable = new PageRequest(currentPage -1, pageSize);


        return favoriteCooperatorViewRepository.find(technicianId, pageable);
    }
}
