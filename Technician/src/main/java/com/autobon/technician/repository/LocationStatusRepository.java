package com.autobon.technician.repository;

import com.autobon.technician.entity.LocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2016/10/27.
 */
@Repository
public interface LocationStatusRepository extends JpaRepository<LocationStatus,Integer> {

    LocationStatus findByTechId(int techId);


    @Query(value = "SELECT" +
            " t.id as id," +
            " t.name as name," +
            " t.phone as phone," +
            " t.film_level as film_level," +
            " t.car_cover_level as car_cover_level," +
            " t.color_modify_level as color_modify_level," +
            " t.beauty_level as beauty_level," +
            " truncate( (2 * 6378.137 * ASIN(SQRT(POW(SIN(PI() * (?1 - ls.lat) / 360),2) + COS(PI() * ?2 / 180) * COS(ls.lat * PI() / 180) * POW(SIN(PI() * (?2 - ls.lng) / 360),2)))) ,2)AS distance," +
            " ls.status as status " +
            " FROM" +
            " t_technician t " +
            " inner join t_location_status ls ON ls.tech_id = t.id " +
            " ORDER BY distance limit ?3,?4" ,nativeQuery = true)
    List<Object[]> getTech(String lat, String lng , int begin , int size);


    @Query(value = "SELECT  count(*) FROM t_technician t" +
            "       inner join t_location_status ls ON ls.tech_id = t.id ",nativeQuery = true)
    int getTech();


    @Query(value = "SELECT" +
            " t.id as id," +
            " t.name as name," +
            " t.phone as phone," +
            " t.film_level as film_level," +
            " t.car_cover_level as car_cover_level," +
            " t.color_modify_level as color_modify_level," +
            " t.beauty_level as beauty_level," +
            " truncate( (2 * 6378.137 * ASIN(SQRT(POW(SIN(PI() * (?1 - ls.lat) / 360),2) + COS(PI() * ?2 / 180) * COS(ls.lat * PI() / 180) * POW(SIN(PI() * (?2 - ls.lng) / 360),2)))) ,2)AS distance," +
            " ls.status as status " +
            " FROM" +
            " t_technician t " +
            " left join t_location_status ls ON ls.tech_id = t.id " +
            " where t.name like ?1 or t.phone like ?1 " +
            "ORDER BY distance " ,nativeQuery = true)
    List<Object[]> getTechByPhoneOrName(String name, int begin , int size);


    @Query(value = "SELECT  count(*)   FROM t_technician t " +
            "left join t_location_status ls ON ls.tech_id = t.id " +
            "where t.name like ?1 or t.phone like ?1 ORDER BY distance  ,nativeQuery = true)",nativeQuery = true)
    int getTechByPhoneOrName(String name);

}
