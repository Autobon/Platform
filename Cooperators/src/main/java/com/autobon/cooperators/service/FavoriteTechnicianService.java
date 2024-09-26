package com.autobon.cooperators.service;

import com.autobon.cooperators.entity.FavoriteTechnician;
import com.autobon.cooperators.entity.FavoriteTechnicianView;
import com.autobon.cooperators.repository.FavoriteCooperatorViewRepository;
import com.autobon.cooperators.repository.FavoriteTechnicianRepository;
import com.autobon.cooperators.repository.FavoriteTechnicianViewRepository;
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
public class FavoriteTechnicianService {

    @Autowired
    FavoriteTechnicianRepository favoriteTechnicianRepository;
    @Autowired
    FavoriteTechnicianViewRepository favoriteTechnicianViewRepository;

    public int save(int technicianId, int cooperatorId){

        if(favoriteTechnicianRepository.findByCooperatorIdAndTechnicianId(cooperatorId, technicianId) != null){
            return 1;
        }

        FavoriteTechnician favoriteTechnician = new FavoriteTechnician();
        favoriteTechnician.setCooperatorId(cooperatorId);
        favoriteTechnician.setTechnicianId(technicianId);
        favoriteTechnician.setCreateTime(new Date());
        favoriteTechnicianRepository.save(favoriteTechnician);
        return 0;
    }

    public int delete(int technicianId, int cooperatorId){
        FavoriteTechnician favoriteTechnician = favoriteTechnicianRepository.findByCooperatorIdAndTechnicianId(cooperatorId, technicianId);
        if(favoriteTechnician == null){
            return 1;
        }
        favoriteTechnicianRepository.delete(favoriteTechnician);
        return 0;
    }

    public Page<FavoriteTechnicianView> find(int cooperatorId, int currentPage, int pageSize){


        Pageable pageable = new PageRequest(currentPage -1, pageSize);


        return favoriteTechnicianViewRepository.find(cooperatorId, pageable);
    }
}
