package com.autobon.order.service;

import com.autobon.order.entity.TechStat;
import com.autobon.order.repository.TechStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dave on 16/3/14.
 */
@Service
public class TechStatService {
    @Autowired TechStatRepository repository;

    public TechStat save(TechStat techStat) {
        return repository.save(techStat);
    }

    public TechStat getByTechId(int techId) {
        return repository.getByTechId(techId);
    }
}
