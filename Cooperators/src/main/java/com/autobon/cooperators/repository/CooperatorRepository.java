package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.Cooperator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by lu on 2016/3/7.
 */
@Repository
public interface CooperatorRepository extends JpaRepository<Cooperator, Integer> {

    @Query("select c from Cooperator c " +
            "where (?1 is null or c.fullname =?1) " +
            "and (?2 is null or c.businessLicense = ?2) " +
            "and (?3 is null or c.statusCode = ?3) " )
    Page<Cooperator> findCoop(String fullname, String businessLicense, Integer statusCode, Pageable p);

    @Query("select c from Cooperator c " +
            "where (?1 is null or c.fullname = ?1) " +
            "and (?2 is null or c.corporationName = ?2) " +
            "and (?3 is null or c.statusCode = ?3)")
    Page<Cooperator> find(String fullname, String corporationName, Integer statusCode, Pageable p);

    @Query("from Cooperator c where (?1 is null or c.province = ?1) and (?2 is null or c.city = ?2)")
    Page<Cooperator> findByLocation(String province, String city, Pageable pageable);
}
