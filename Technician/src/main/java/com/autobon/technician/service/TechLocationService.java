package com.autobon.technician.service;

import com.autobon.technician.entity.TechLocation;
import com.autobon.technician.repository.TechLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by dave on 16/4/8.
 */
@Service
public class TechLocationService {
    @Autowired TechLocationRepository repository;

    public Page<TechLocation> findByDistinctTech(String province, String city, int page, int pageSize) {
        return repository.findByDistinctTech(province, city, new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }
}
