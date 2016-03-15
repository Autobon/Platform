package com.autobon.technician.repository;

import com.autobon.technician.entity.DetailedTechnician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dave on 16/3/14.
 */
@Repository
public interface DetailedTechnicianRepository extends JpaRepository<DetailedTechnician, Integer> {

    DetailedTechnician getByPhone(String phone);

    Page<DetailedTechnician> findByName(String name, Pageable pageable);
}
