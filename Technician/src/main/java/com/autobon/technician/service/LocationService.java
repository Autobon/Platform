package com.autobon.technician.service;

import com.autobon.technician.entity.Location;
import com.autobon.technician.entity.LocationStatus;
import com.autobon.technician.repository.LocationRepository;
import com.autobon.technician.repository.LocationStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by yuh on 2016/3/1.
 */
@Service
public class LocationService {

    @Autowired
    private LocationRepository repository;

    @Autowired
    private LocationStatusRepository locationStatusRepository;

    public Location save(Location location) {
        LocationStatus locationStatus = locationStatusRepository.findByTechId(location.getTechId());
        if(locationStatus != null){
            LocationStatus locationStatus1 = new LocationStatus(location);
            locationStatus1.setId(locationStatus.getId());
            locationStatus1.setStatus(locationStatus.getStatus());
            locationStatusRepository.save(locationStatus1);
        }else{
            LocationStatus locationStatus1 = new LocationStatus(location);
            locationStatus1.setStatus(1);
            locationStatusRepository.save(locationStatus1);
        }
        return repository.save(location);
    }

    public Page<Location> findByTechId(int techId, int page, int pageSize) {
        return repository.findByTechId(techId, new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }
}
