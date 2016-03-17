package com.autobon.order.service;

import com.autobon.order.entity.Location;
import com.autobon.order.repository.LocationRepository;
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

    public Location save(Location location) {
        return repository.save(location);
    }

    public Page<Location> findByTechId(int techId, int page, int pageSize) {
        return repository.findByTechId(techId, new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }
}
