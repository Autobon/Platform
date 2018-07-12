package com.autobon.order.repository;

import com.autobon.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yuh on 2016/2/22.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

        @Query("from Order o where o.mainTechId = ?1 and o.statusCode >= 60")
        Page<Order> findFinishedOrderByMainTechId(int techId, Pageable pageable);

        @Query("from Order o where o.secondTechId = ?1 and o.statusCode >= 60")
        Page<Order> findFinishedOrderBySecondTechId(int techId, Pageable pageable);


        @Query("from Order o where o.mainTechId = ?1")
        Page<Order> findOrderByTechId(int techId, Pageable pageable);

        @Query("select o from Order o where o.id = ?1")
        Order findOrderById(int orderId);

        @Query("from Order o where (o.mainTechId = ?1 or o.secondTechId = ?1) and o.statusCode < 60")
        Page<Order> findUnfinishedOrderByTechId(int techId, Pageable pageable);

        @Query("select o from Order o " +
                "where (?1 is null or o.orderNum like ?1) " +
                "and (?2 is null or o.creatorName like ?2) " +
                "and (?3 is null or o.contactPhone like ?3) " +
                "and (COALESCE(?4) is null or o.orderType in (?4)) " +
                "and (?5 is null or o.statusCode = ?5)")
        Page<Order> find(String orderNum, String creatorName, String contactPhone,
                         List<Integer> orderType, Integer statusCode, Pageable pageable);

        @Query("select o from Order o " +
                "where (?1 is null or o.orderNum like ?1) " +
                "and (?2 is null or o.creatorName like ?2) " +
                "and (?3 is null or o.contactPhone like ?3) " +
                "and (?4 is null or o.type like (?4)) " +
                "and (?5 is null or o.statusCode = ?5)")
        Page<Order> findOrder(String orderNum, String creatorName, String contactPhone,
                              String type, Integer statusCode, Pageable pageable);

        @Query(value = "select o.* from t_order o where 1=1 " +
                " and (?1 is null or o.order_num like ?1) " +
                " and (?2 is null or o.creator_name like ?2) " +
                " and (?3 is null or o.contact_phone like ?3) " +
                " and (?4 is null or o.type like ?4) " +
                " and (?5 is null or o.status = ?5) " +
                " and (COALESCE(?6) is null or o.coop_id in (?6)) " +
                " and (?7 is null or o.vin like ?7) " +
                " and (?8 is null or date_format(o.add_time,'%Y-%m-%d') = ?8) " +
                " and (?9 is null or date_format(o.agreed_start_time,'%Y-%m-%d') = ?9) " +
                " order by o.id desc limit ?10,?11", nativeQuery = true)
        List<Order> findOrder2(String orderNum, String creatorName, String contactPhone,
                              String type, Integer statusCode, List<String> coopIds,
                               String vin, String addTime, String orderTime, Integer page, Integer pageSize);

        @Query(value = "select o.* from t_order o where 1=1 " +
                " and (?1 is null or o.order_num like ?1) " +
                " and (?2 is null or o.creator_name like ?2) " +
                " and (?3 is null or o.contact_phone like ?3) " +
                " and (?4 is null or o.type like ?4) " +
                " and (?5 is null or o.status = ?5) " +
                " and (COALESCE(?6) is null or o.coop_id in (?6)) " +
                " and (?7 is null or o.vin like ?7) " +
                " and (?8 is null or date_format(o.add_time,'%Y-%m-%d') = ?8) " +
                " and (?9 is null or date_format(o.agreed_start_time,'%Y-%m-%d') = ?9) " +
                " order by o.agreed_start_time desc limit ?10,?11", nativeQuery = true)
        List<Order> findOrder2ByTime(String orderNum, String creatorName, String contactPhone,
                               String type, Integer statusCode, List<String> coopIds,
                               String vin, String addTime, String orderTime, Integer page, Integer pageSize);

        @Query(value = "select count(*) from t_order o where 1=1 " +
                " and (?1 is null or o.order_num like ?1) " +
                " and (?2 is null or o.creator_name like ?2) " +
                " and (?3 is null or o.contact_phone like ?3) " +
                " and (?4 is null or o.type like ?4) " +
                " and (?5 is null or o.status = ?5) " +
                " and (COALESCE(?6) is null or o.coop_id in (?6)) " +
                " and (?7 is null or o.vin like ?7) " +
                " and (?8 is null or date_format(o.add_time,'%Y-%m-%d') = ?8) " +
                " and (?9 is null or date_format(o.agreed_start_time,'%Y-%m-%d') = ?9)", nativeQuery = true)
        int findOrder2Count(String orderNum, String creatorName, String contactPhone,
                            String type, Integer statusCode, List<String> coopIds,
                            String vin, String addTime, String orderTime);

        @Query("select o from Order o " +
                "where (?1 is null or o.orderNum like ?1) " +
                "and (?2 is null or o.creatorName like ?2) " +
                "and (?3 is null or o.contactPhone like ?3) " +
                "and (?4 is null or o.type like (?4)) " +
                "and (?5 is null or o.statusCode = ?5)" +
                "and  o.mainTechId in ?6")
        Page<Order> findOrder(String orderNum, String creatorName, String contactPhone,
                              String type, Integer statusCode, List<Integer> list, Pageable pageable);

        @Query(value = "select o.* from t_order o where 1=1 " +
                " and (?1 is null or o.order_num like ?1) " +
                " and (?2 is null or o.creator_name like ?2) " +
                " and (?3 is null or o.contact_phone like ?3) " +
                " and (?4 is null or o.type like (?4)) " +
                " and (?5 is null or o.status = ?5)" +
                " and (COALESCE(?6) is null or o.coop_id in (?6)) " +
                " and (?7 is null or o.vin like ?7) " +
                " and (?8 is null or date_format(o.add_time,'%Y-%m-%d') = ?8) " +
                " and (?9 is null or date_format(o.agreed_start_time,'%Y-%m-%d') = ?9) " +
                " and  o.main_tech_id in (?10) " +
                " order by o.id desc limit ?12,?13", nativeQuery = true)
        List<Order> findOrder2(String orderNum, String creatorName, String contactPhone,
                              String type, Integer statusCode, List<String> coopIds, String vin,
                               String addTime, String orderTime, List<Integer> list, Integer page, Integer pageSize);

        @Query(value = "select o.* from t_order o where 1=1 " +
                " and (?1 is null or o.order_num like ?1) " +
                " and (?2 is null or o.creator_name like ?2) " +
                " and (?3 is null or o.contact_phone like ?3) " +
                " and (?4 is null or o.type like (?4)) " +
                " and (?5 is null or o.status = ?5)" +
                " and (COALESCE(?6) is null or o.coop_id in (?6)) " +
                " and (?7 is null or o.vin like ?7) " +
                " and (?8 is null or date_format(o.add_time,'%Y-%m-%d') = ?8) " +
                " and (?9 is null or date_format(o.agreed_start_time,'%Y-%m-%d') = ?9) " +
                " and  o.main_tech_id in (?10) " +
                " order by o.agreed_start_time desc limit ?11,?12", nativeQuery = true)
        List<Order> findOrder2ByTime(String orderNum, String creatorName, String contactPhone,
                               String type, Integer statusCode, List<String> coopIds, String vin,
                               String addTime, String orderTime, List<Integer> list, Integer page, Integer pageSize);


        @Query(value = "select count(*) from t_order o where 1=1 " +
                " and (?1 is null or o.order_num like ?1) " +
                " and (?2 is null or o.creator_name like ?2) " +
                " and (?3 is null or o.contact_phone like ?3) " +
                " and (?4 is null or o.type like (?4)) " +
                " and (?5 is null or o.status = ?5)" +
                " and (COALESCE(?6) is null or o.coop_id in (?6)) " +
                " and (?7 is null or o.vin like ?7) " +
                " and (?8 is null or date_format(o.add_time,'%Y-%m-%d') = ?8) " +
                " and (?9 is null or date_format(o.agreed_start_time,'%Y-%m-%d') = ?9) " +
                " and  o.main_tech_id in (?10)", nativeQuery = true)
        int findOrder2Count(String orderNum, String creatorName, String contactPhone, String type, Integer statusCode,
                            List<String> coopIds, String vin, String addTime, String orderTime, List<Integer> list);


        @Query("select o from Order o where (o.statusCode < 50 and o.orderTime <= ?1) or (o.statusCode = 50 and o.orderTime <= ?2)")
        Page<Order> findExpired(Date signInBefore, Date finishBefore, Pageable pageable);

        @Query("from Order o where (o.mainTechId = ?1 or o.secondTechId = ?1) and o.finishTime >= ?2 and o.finishTime < ?3")
        Page<Order> findBetweenByTechId(int techId, Date start, Date end, Pageable pageable);

        @Query("select count(o) from Order o where o.addTime between ?1 and ?2")
        int countOfNew(Date from, Date to);

        @Query("select count(o) from Order o where o.finishTime between ?1 and ?2")
        int countOfFinished(Date from, Date to);

        @Query("select count(o) from Order o")
        int totalOfCreated();

        @Query("select count(o) from Order o where o.statusCode >= 60 and o.statusCode <= 70")
        int totalOfFinished();

        @Query("select count(o) from Order o where o.creatorId = ?1")
        int countOfCoopAccount(int id);



        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " o.position_lon as longitude ," +
                " o.position_lat as latitude ," +
                " o.remark as remark ," +
                " o.product_status as productStatus," +
                " o.reassignment_status as reassignmentStatus," +
                " ifnull(wd.payment,0) as payment," +
                " ifnull(wd.pay_status,0) as payStatus" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " LEFT JOIN t_work_detail wd ON wd.tech_id = tech.id" +
                " where o.id = ?1" ,nativeQuery = true)
        List<Object[]>  getByOrderId(int orderId);





        @Query("from Order o where o.mainTechId = ?1 and o.statusCode >= 60")
        Page<Order> findFinishedOrder(int techId, Pageable pageable);

        @Query("from Order o where o.mainTechId = ?1 and o.statusCode < 60")
        Page<Order> findUnfinishedOrder(int techId, Pageable pageable);

        @Query("from Order o where o.mainTechId = ?1")
        Page<Order> findAllOrder(int techId, Pageable pageable);


        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " ct.longitude as longitude ," +
                " ct.latitude as latitude ," +
                " o.remark as remark, " +
                " o.product_status as productStatus," +
                " o.reassignment_status as reassignmentStatus," +
                " ifnull(wd.payment,0) as payment," +
                " ifnull(wd.pay_status,0) as payStatus" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " LEFT JOIN t_work_detail wd ON wd.order_id = o.id " +
                " where o.main_tech_id = ?1 and o.status < 60  GROUP BY o.id  limit ?2,?3" ,nativeQuery = true)
        List<Object[]>  getUnfinishOrder(Integer techId, Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " where o.main_tech_id = ?1 and o.status < 60" ,nativeQuery = true)
        int getUnfinishOrderCount(Integer techId);


        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " ct.longitude as longitude ," +
                " ct.latitude as latitude ," +
                " o.remark as remark, " +
                " o.product_status as productStatus," +
                " o.reassignment_status as reassignmentStatus," +
                " ifnull(wd.payment,0) as payment," +
                " ifnull(wd.pay_status,0) as payStatus" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " LEFT JOIN t_work_detail wd ON wd.order_id = o.id " +
                " where o.main_tech_id = ?1 and o.status >56 and o.status<200 GROUP BY o.id limit ?2,?3" ,nativeQuery = true)
        List<Object[]>  getfinishOrder(Integer techId, Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " where o.main_tech_id = ?1 and o.status >56 and o.status<200" ,nativeQuery = true)
        int getfinishOrderCount(Integer techId);





        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " ct.longitude as longitude ," +
                " ct.latitude as latitude ," +
                " o.remark as remark, " +
                " o.product_status as productStatus," +
                " o.reassignment_status as reassignmentStatus," +
                " ifnull(wd.payment,0) as payment," +
                " ifnull(wd.pay_status,0) as payStatus" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " LEFT JOIN t_work_detail wd ON wd.order_id = o.id " +
                " where o.main_tech_id = ?1  GROUP BY o.id  limit ?2,?3" ,nativeQuery = true)
        List<Object[]>  getAllOrder(Integer techId, Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " where o.main_tech_id = ?1 " ,nativeQuery = true)
        int getAllOrderCount(Integer techId);




        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " ct.longitude as longitude ," +
                " ct.latitude as latitude ," +
                " o.remark as remark, " +
                " o.product_status as productStatus," +
                " o.reassignment_status as reassignmentStatus," +
                " ifnull(wd.payment,0) as payment," +
                " ifnull(wd.pay_status,0) as payStatus" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " LEFT JOIN t_work_detail wd on wd.order_id = o.id" +
                " where o.main_tech_id != ?1 and wd.tech_id = ?1 GROUP BY o.id limit ?2,?3" ,nativeQuery = true)
        List<Object[]>  getCooperationOrder(Integer techId, Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_work_detail wd on wd.order_id = o.id" +
                " where o.main_tech_id != ?1 and wd.tech_id = ?1" ,nativeQuery = true)
        int getCooperationOrderCount(Integer techId);





        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " ct.longitude as longitude ," +
                " ct.latitude as latitude ," +
                " o.remark as remark ," +
                " 0 as evaluateStatus," +
                " ts.total_orders as orderCount," +
                " ts.star_rate as evaluate," +
                " 0 as cancelCount," +
                " tech.avatar," +
                " ls.lng as techLng," +
                " ls.lat as techLat" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " LEFT JOIN t_tech_stat ts ON ts.tech_id = tech.id " +
                " LEFT JOIN t_location_status ls ON ls.tech_id = tech.id " +
                " where o.coop_id = ?1 and o.status < 60 order by o.add_time desc limit ?2,?3 " ,nativeQuery = true)
        List<Object[]>  getCoopUnfinishOrder(Integer coopId, Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " where o.coop_id = ?1 and o.status < 60" ,nativeQuery = true)
        int getCoopUnfinishOrderCount(Integer coopId);




        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " ct.longitude as longitude ," +
                " ct.latitude as latitude ," +
                " o.remark as remark," +
                " ifnull(tc.id,0)  as evaluateStatus, " +
                " ts.total_orders as orderCount," +
                " ts.star_rate as evaluate," +
                " 0 as cancelCount," +
                " tech.avatar," +
                " ls.lng as techLng," +
                " ls.lat as techLat" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " LEFT JOIN t_comment tc on tc.order_id = o.id" +
                " LEFT JOIN t_tech_stat ts ON ts.tech_id = tech.id " +
                " LEFT JOIN t_location_status ls ON ls.tech_id = tech.id " +
                " where o.coop_id = ?1 and o.status >= 60 order by o.add_time desc limit ?2,?3 " ,nativeQuery = true)
        List<Object[]>  getCoopfinishOrder(Integer coopId, Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " where o.coop_id = ?1 and o.status >= 60" ,nativeQuery = true)
        int getCoopfinishOrderCount(Integer coopId);


        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " ct.longitude as longitude ," +
                " ct.latitude as latitude ," +
                " o.remark as remark, " +
                " o.product_status as productStatus," +
                " o.reassignment_status as reassignmentStatus" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " where o.status = 0 group by o.id limit ?1,?2" ,nativeQuery = true)
        List<Object[]>  getNewCreateOrder(Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " where o.status = 0" ,nativeQuery = true)
        int getNewCreateCount();






        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " ct.longitude as longitude ," +
                " ct.latitude as latitude ," +
                " o.remark as remark ," +
                " 0 as evaluateStatus," +
                " ts.total_orders as orderCount," +
                " ts.star_rate as evaluate," +
                " 0 as cancelCount," +
                " tech.avatar," +
                " ls.lng as techLng," +
                " ls.lat as techLat" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " LEFT JOIN t_tech_stat ts ON ts.tech_id = tech.id " +
                " LEFT JOIN t_location_status ls ON ls.tech_id = tech.id " +
                " where o.coop_id = ?1 and o.status>=60 and o.status < 70  order by o.add_time desc limit ?2,?3" ,nativeQuery = true)
        List<Object[]>  getUnEvaluateOrder(Integer coopId, Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " where o.coop_id = ?1 and  o.status>=60 and o.status < 70" ,nativeQuery = true)
        int getCoopUnEvaluateOrderCount(Integer coopId);



        @Query(value = "SELECT " +
                " o.id as id, " +
                " o.order_num as orderNum, " +
                " o.photo as phone, " +
                " o.agreed_start_time as agreedStartTime ," +
                " o.agreed_end_time as agreedEndTime, " +
                " o.status as status , " +
                " o.creator_type as creatorType, " +
                " o.main_tech_id as techId, " +
                " tech.name as techName, " +
                " tech.phone as techPhone, " +
                " o.before_photos as beforePhotos, " +
                " o.after_photos as afterPhotos, " +
                " o.start_time as startTime, " +
                " o.end_time as endTime, " +
                " o.sign_time as signTime, " +
                " o.taken_time as takenTime, " +
                " o.add_time as addTime, " +
                " o.type as type, " +
                " o.coop_id as coopId, " +
                " ca.shortname as coopName," +
                " o.creator_id as creatorId," +
                " o.creator_name as creatorName," +
                " ca.phone as creatorPhone ," +
                " ct.address as address ," +
                " ct.longitude as longitude ," +
                " ct.latitude as latitude ," +
                " o.remark as remark," +
                " ifnull(tc.id,0)  as evaluateStatus, " +
                " ts.total_orders as orderCount," +
                " ts.star_rate as evaluate," +
                " 0 as cancelCount," +
                " tech.avatar," +
                " ls.lng as techLng," +
                " ls.lat as techLat" +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " LEFT JOIN t_comment tc on tc.order_id = o.id" +
                " LEFT JOIN t_tech_stat ts ON ts.tech_id = tech.id " +
                " LEFT JOIN t_location_status ls ON ls.tech_id = tech.id " +
                " where o.coop_id = ?1  order by o.add_time desc limit ?2,?3 " ,nativeQuery = true)
        List<Object[]>  getCoopAllOrder(Integer coopId, Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " where o.coop_id = ?1 " ,nativeQuery = true)
        int getCoopAllOrderCount(Integer coopId);






        @Query(value = "select *  from  t_order where  hour(timediff(agreed_start_time,NOW())) =36", nativeQuery = true)
        List<Order> findNewCreate36();

        @Query(value = "select *  from  t_order where  hour(timediff(agreed_start_time,NOW())) =12", nativeQuery = true)
        List<Order> findNewCreate12();
}

