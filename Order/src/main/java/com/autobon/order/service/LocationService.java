package com.autobon.order.service;

import com.autobon.order.entity.Location;
import com.autobon.order.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuh on 2016/3/1.
 */
@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public void save(Location location) {
        locationRepository.save(location);
    }

    public Location findLatestLocation(int technicianId) {
        List<Location> locationList = locationRepository.findTop10ByTechnicianIdOrderByAddTimeDesc(technicianId);
        return locationList.get(0);
    }
}
