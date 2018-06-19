package com.autobon.order.repository;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2016/12/5.
 */
@Repository
public interface OrderViewRepository extends JpaRepository<OrderView, Integer> {


    @Query("select ov from OrderView ov where ov.techId = ?1")
    Page<OrderView> findAllOrder(Integer techId, Pageable pageable);


    @Query("select ov from OrderView ov where ov.techId = ?1 and ov.statusCode>59 and ov.statusCode<71")
    Page<OrderView> findFinishOrder(Integer techId, Pageable pageable);


    @Query("select ov from OrderView ov where ov.techId = ?1 and ov.statusCode<60")
    Page<OrderView> findUnFinishOrder(Integer techId, Pageable pageable);



    @Query(value = "SELECT " +
            " ov.*"+
            " FROM" +
            " t_order_view ov" +
            " LEFT JOIN t_work_detail wd on wd.order_id = ov.id" +
            " where  ov.status>59 and ov.status<71 and ov.tech_id != ?1 and wd.tech_id = ?1 GROUP BY ov.id limit ?2,?3" ,nativeQuery = true)
    List<OrderView> findFinishOrderAsPartner(Integer techId, Integer begin ,Integer size);


    @Query(value = "SELECT " +
            " count(*) " +
            " FROM" +
            " t_order_view ov" +
            " LEFT JOIN t_work_detail wd on wd.order_id = ov.id" +
            " where ov.status>59 and ov.status<71 and ov.tech_id != ?1 and wd.tech_id = ?1 " ,nativeQuery = true)
    int findFinishOrderAsPartnerCount(Integer techId);

    OrderView findById(Integer orderId);

    @Query("select ov from OrderView ov where ov.statusCode = ?1" +
            " and (?2 is null or ov.type = ?2) ")
    Page<OrderView> findByStatusCode(Integer statusCode, String workType, Pageable pageable);

    @Query(value = "select ov.* " +
            " from t_order_view ov where ov.status = ?1 order by " +
            " truncate(round(6378.138*2*asin(sqrt(pow(sin( (ov.latitude*pi()/180-?2*pi()/180)/2),2)+cos(ov.latitude*pi()/180)*cos(?2 *pi()/180)* pow(sin( (ov.longitude*pi()/180-?3*pi()/180)/2),2)))*1000)/1000,2) " +
            " desc limit ?4,?5", nativeQuery = true)
    List<OrderView> findByStatusCode1(Integer statusCode, String lat, String lng, int begin, int size);

    @Query(value = "select ov.* " +
            " from t_order_view ov where ov.status = ?1 order by " +
            " truncate(round(6378.138*2*asin(sqrt(pow(sin( (ov.latitude*pi()/180-?2*pi()/180)/2),2)+cos(ov.latitude*pi()/180)*cos(?2 *pi()/180)* pow(sin( (ov.longitude*pi()/180-?3*pi()/180)/2),2)))*1000)/1000,2) " +
            " asc limit ?4,?5", nativeQuery = true)
    List<OrderView> findByStatusCode2(Integer statusCode, String lat, String lng, int begin, int size);

    @Query(value = "SELECT  count(*)   FROM t_order_view ov " +
            " where ov.status = ?1",nativeQuery = true)
    int findByStatusCodeCount(Integer statusCode);


    @Query(value = "select ov.* from t_order_view ov where ov.coop_id = ?1" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4) " +
            " order by ov.id desc limit ?5,?6", nativeQuery = true)
    List<OrderView> findAllCoopOrder(Integer coopId, String workDate, String vin, String phone, Integer begin, Integer size);

    @Query(value = "select count(*) from t_order_view ov where ov.coop_id = ?1" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4)", nativeQuery = true)
    int findAllCoopOrderCount(Integer coopId, String workDate, String vin, String phone);


    @Query(value = "select ov.* from t_order_view ov where ov.coop_id = ?1 and ov.status<10" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4) " +
            " order by ov.id desc limit ?5,?6", nativeQuery = true)
    List<OrderView> findUnGetCoopOrder(Integer coopId, String workDate, String vin, String phone, Integer begin, Integer size);

    @Query(value = "select count(*) from t_order_view ov where ov.coop_id = ?1 and ov.status<10" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4)", nativeQuery = true)
    int findUnGetCoopOrderCount(Integer coopId, String workDate, String vin, String phone);

    @Query(value = "select ov.* from t_order_view ov where ov.coop_id = ?1 and ov.status<60" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4) " +
            " order by ov.id desc limit ?5,?6", nativeQuery = true)
    List<OrderView> findUnFinishCoopOrder(Integer coopId, String workDate, String vin, String phone, Integer begin, Integer size);

    @Query(value = "select count(*) from t_order_view ov where ov.coop_id = ?1 and ov.status<60" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4)", nativeQuery = true)
    int findUnFinishCoopOrderCount(Integer coopId, String workDate, String vin, String phone);

    @Query(value = "select ov.* from t_order_view ov where ov.coop_id = ?1 and ov.status>=50 and ov.status<60" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4) " +
            " order by ov.id desc limit ?5,?6", nativeQuery = true)
    List<OrderView> findWorkingCoopOrder(Integer coopId, String workDate, String vin, String phone, Integer begin, Integer size);

    @Query(value = "select count(*) from t_order_view ov where ov.coop_id = ?1 and ov.status>=50 and ov.status<60" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4)", nativeQuery = true)
    int findWorkingCoopOrderCount(Integer coopId, String workDate, String vin, String phone);

    @Query(value = "select ov.* from t_order_view ov where ov.coop_id = ?1 and ov.status>59 and ov.status<71" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4) " +
            " order by ov.id desc limit ?5,?6", nativeQuery = true)
    List<OrderView> findFinishCoopOrder(Integer coopId, String workDate, String vin, String phone, Integer begin, Integer size);

    @Query(value = "select count(*) from t_order_view ov where ov.coop_id = ?1 and ov.status>59 and ov.status<71" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4)", nativeQuery = true)
    int findFinishCoopOrderCount(Integer coopId, String workDate, String vin, String phone);

    @Query(value = "select ov.* from t_order_view ov where ov.coop_id = ?1 and ov.status>59 and ov.status<70" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4) " +
            " order by ov.id desc limit ?5,?6", nativeQuery = true)
    List<OrderView> findUnEvaluateCoopOrder(Integer coopId, String workDate, String vin, String phone, Integer begin, Integer size);

    @Query(value = "select count(*) from t_order_view ov where ov.coop_id = ?1 and ov.status>59 and ov.status<70" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4)", nativeQuery = true)
    int findUnEvaluateCoopOrderCount(Integer coopId, String workDate, String vin, String phone);

    @Query(value = "select ov.* from t_order_view ov where ov.coop_id = ?1 and ov.status=70" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4) " +
            " order by ov.id desc limit ?5,?6", nativeQuery = true)
    List<OrderView> findEvaluatedCoopOrder(Integer coopId, String workDate, String vin, String phone, Integer begin, Integer size);

    @Query(value = "select count(*) from t_order_view ov where ov.coop_id = ?1 and ov.status=70" +
            " and (?2 is null or date_format(ov.agreed_start_time,'%Y-%m-%d') = ?2) " +
            " and (?3 is null or ov.vin = ?3) " +
            " and (?4 is null or ov.contact_phone = ?4)", nativeQuery = true)
    int findEvaluatedCoopOrderCount(Integer coopId, String workDate, String vin, String phone);

}
