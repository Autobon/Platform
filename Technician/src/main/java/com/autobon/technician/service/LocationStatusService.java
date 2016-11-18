package com.autobon.technician.service;


import com.autobon.technician.entity.LocationStatus;
import com.autobon.technician.repository.LocationStatusRepository;
import com.autobon.technician.vo.TechnicianLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wh on 2016/11/11.
 */

@Service
public class LocationStatusService {

    @Autowired
    LocationStatusRepository locationStatusRepository;

    public LocationStatus findByTechId(int techId){
        return locationStatusRepository.findByTechId(techId);
    }


    public Page<TechnicianLocation> getTechByDistance(String lat, String lng, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);
        List<Object[]> techList = locationStatusRepository.getTech(lat, lng, (currentPage - 1) * pageSize, pageSize);
        int count = locationStatusRepository.getTech();

        List<TechnicianLocation> technicianLocations =  new ArrayList<>();
        for(Object[] objects: techList){
            TechnicianLocation technicianLocation = new TechnicianLocation(objects);
            technicianLocations.add(technicianLocation);
        }
        return new PageImpl<>(technicianLocations,p,count);
    }


    public Page<TechnicianLocation> getTechByName(String lat, String lng, String query, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);
        if(query != null){
            query ="%"+query+"%";
        }
        List<Object[]> techList = locationStatusRepository.getTechByPhoneOrName(lat,lng, query, (currentPage - 1) * pageSize, pageSize);
        int count = locationStatusRepository.getTechByPhoneOrName(query);

        List<TechnicianLocation> technicianLocations =  new ArrayList<>();
        for(Object[] objects: techList){
            TechnicianLocation technicianLocation = new TechnicianLocation(objects);
            technicianLocations.add(technicianLocation);
        }
        return new PageImpl<>(technicianLocations,p,count);
    }
}
