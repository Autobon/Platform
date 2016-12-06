package com.autobon.technician.repository;

import com.autobon.technician.entity.Technician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by dave on 16/2/13.
 */
@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Integer> {
    Technician getByPhone(String phone);

    Page<Technician> findByName(String name, Pageable pageable);

    Technician getByPushId(String pushId);

    @Query("from Technician t where t.lastLoginAt > ?1")
    Page<Technician> findActivedFrom(Date date, Pageable pageable);

    @Query("select t from Technician t " +
            "where (?1 is null or t.phone = ?1) " +
            "and (?2 is null or t.name = ?2) " +
            "and (?3 is null or t.statusCode = ?3)")
    Page<Technician> find(String phone, String name, Integer statusCode, Pageable pageable);

    @Query("select count(t) from Technician t where t.createAt between ?1 and ?2")
    int countOfNew(Date from, Date to);

    @Query("select count(t) from Technician t where t.verifyAt between ?1 and ?2 and t.statusCode = 2")
    int countOfVerified(Date from, Date to);

    @Query("select count(t) from Technician t")
    int totalOfRegistered();

    @Query("select count(t) from Technician t where t.statusCode = 2")
    int totalOfVerified();



    @Query("select dt from Technician dt where 1 =1 " +
            " and (?1 is null or dt.phone like ?1) " +
            " and (?2 is null or dt.name like ?2) ")
    Page<Technician> find(String phone, String name,  Pageable pageable);


    @Query("select dt from Technician dt where dt.statusCode = 2 " +
            " and (?1 is null or dt.phone like ?1) " +
            " and (?2 is null or dt.name like ?2)" +
            " and (?3 is null or dt.id != ?3) ")
    Page<Technician> findTech(String phone, String name, Integer techId,  Pageable pageable);


    @Query(value = "SELECT" +
            " t.id as id," +
            " t.name as name," +
            " t.phone as phone," +
            " t.film_level as film_level," +
            " t.car_cover_level as car_cover_level," +
            " t.color_modify_level as color_modify_level," +
            " t.beauty_level as beauty_level," +
            " 0 as orderCount," +
            " 0 as evaluate," +
            " 0 as cancelCount," +
            " t.film_working_seniority as filmWorkingSeniority ," +
            " t.car_cover_working_seniority as carCoverWorkingSeniority," +
            " t.color_modify_working_seniority as colorModifyWorkingSeniority," +
            " t.beauty_working_seniority as beautyWorkingSeniority," +
            " t.avatar as avatar" +
            " FROM" +
            " t_technician t " +
            " where t.id = ?1 " ,nativeQuery = true)
    List<Object[]> getByTechId(int techId);

}
