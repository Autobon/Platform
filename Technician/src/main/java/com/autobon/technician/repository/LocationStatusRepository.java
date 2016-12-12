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
            " truncate((2 * 6378.137 * ASIN(SQRT(POW(SIN(PI() * abs((?1 - ls.lat)) / 360),2) + COS(PI() *  ?2 / 180) * abs(COS(ls.lat * PI()) / 180) * POW(SIN(PI() * abs(?2 - ls.lng) / 360),2)))) ,2) AS distance," +
            " ls.status as status ," +
            " ts.total_orders as orderCount," +
            " ts.star_rate as evaluate," +
            " 0 as cancelCount," +
            " t.film_working_seniority as filmWorkingSeniority ," +
            " t.car_cover_working_seniority as carCoverWorkingSeniority," +
            " t.color_modify_working_seniority as colorModifyWorkingSeniority," +
            " t.beauty_working_seniority as beautyWorkingSeniority," +
            " t.avatar as avatar" +
            " FROM" +
            " t_technician t " +
            " inner join t_location_status ls ON ls.tech_id = t.id " +
            " left join t_tech_stat ts on ts.tech_id = t.id" +
            " where t.status = 2 " +
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
            " truncate( (2 * 6378.137 * ASIN(SQRT(POW(SIN(PI() * (?1 - ls.lat) / 360),2) + COS(PI() * ?2 / 180) * COS(ls.lat * PI() / 180) * POW(SIN(PI() * (?2 - ls.lng) / 360),2)))) ,2) AS distance," +
            " ls.status as status, " +
            " ts.total_orders as orderCount," +
            " ts.star_rate as evaluate," +
            " 0 as cancelCount," +
            " t.film_working_seniority as filmWorkingSeniority, " +
            " t.car_cover_working_seniority as carCoverWorkingSeniority," +
            " t.color_modify_working_seniority as colorModifyWorkingSeniority," +
            " t.beauty_working_seniority as beautyWorkingSeniority," +
            " t.avatar as avatar" +
            " FROM" +
            " t_technician t " +
            " left join t_location_status ls ON ls.tech_id = t.id " +
            " left join t_tech_stat ts on ts.tech_id = t.id" +
            " where t.status = 2 and (t.name like ?3 or t.phone like ?3)  " +
            " ORDER BY distance limit ?4,?5" ,nativeQuery = true)
    List<Object[]> getTechByPhoneOrName(String lat, String lng, String name, int begin , int size);


    @Query(value = "SELECT  count(*)   FROM t_technician t " +
            " left join t_location_status ls ON ls.tech_id = t.id " +
            " where t.status = 2 and  (t.name like ?1 or t.phone like ?1)",nativeQuery = true)
    int getTechByPhoneOrName(String name);




    @Query(value = "SELECT" +
            " t.id as id," +
            " t.name as name," +
            " t.phone as phone," +
            " t.film_level as film_level," +
            " t.car_cover_level as car_cover_level," +
            " t.color_modify_level as color_modify_level," +
            " t.beauty_level as beauty_level," +
            " truncate( (2 * 6378.137 * ASIN(SQRT(POW(SIN(PI() * (?1 - ls.lat) / 360),2) + COS(PI() * ?2 / 180) * COS(ls.lat * PI() / 180) * POW(SIN(PI() * (?2 - ls.lng) / 360),2)))) ,2) AS distance," +
            " ls.status as status" +
            " FROM" +
            " t_technician t " +
            " left join t_location_status ls ON ls.tech_id = t.id " +
            " where truncate( (2 * 6378.137 * ASIN(SQRT(POW(SIN(PI() * (?1 - ls.lat) / 360),2) + COS(PI() * ?2 / 180) * COS(ls.lat * PI() / 180) * POW(SIN(PI() * (?2 - ls.lng) / 360),2)))) ,2) <?3"
            ,nativeQuery = true)
    List<Object[]> getTechByDistance(String lat, String lng, int kilometre);


    @Query(value = "SELECT " +
            " ls.tech_id, " +
            " ls.lng, " +
            " ls.lat, " +
            " t.work_status, " +
            " t.name," +
            " t.phone," +
            " t.avatar," +
            " t.film_level," +
            " t.car_cover_level," +
            " t.color_modify_level," +
            " t.beauty_level," +
            " t.status as status1" +
            " from " +
            " t_location_status ls inner join t_technician t on t.id = ls.tech_id "+
            " where t.status = 2 and truncate( (2 * 6378.137 * ASIN(SQRT(POW(SIN(PI() * (?1 - ls.lat) / 360),2) + COS(PI() * ?2 / 180) * COS(ls.lat * PI() / 180) * POW(SIN(PI() * (?2 - ls.lng) / 360),2)))) ,2) <?3"
            ,nativeQuery = true)
    List<Object[]> getLocationStatusByDistance(String lat, String lng, int kilometre);



}
