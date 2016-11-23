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
            "where (?1 is null or o.orderNum = ?1) " +
            "and (?2 is null or o.creatorName = ?2) " +
            "and (?3 is null or o.contactPhone = ?3) " +
            "and (COALESCE(?4) is null or o.orderType in (?4)) " +
            "and (?5 is null or o.statusCode = ?5)")
    Page<Order> find(String orderNum, String creatorName, String contactPhone,
                     List<Integer> orderType, Integer statusCode, Pageable pageable);

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
            " ct.longitude as longitude ," +
            " ct.latitude as latitude ," +
            " o.remark as remark " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
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
            " o.remark as remark " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
            " where o.main_tech_id = ?1 and o.status < 60 limit ?2,?3" ,nativeQuery = true)
    List<Object[]>  getUnfinishOrder(Integer techId, Integer begin ,Integer size);


    @Query(value = "SELECT " +
            " count(*) " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
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
            " o.remark as remark " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
            " where o.main_tech_id = ?1 and o.status >= 60 limit ?2,?3" ,nativeQuery = true)
    List<Object[]>  getfinishOrder(Integer techId, Integer begin ,Integer size);


    @Query(value = "SELECT " +
            " count(*) " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
            " where o.main_tech_id = ?1 and o.status >= 60" ,nativeQuery = true)
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
            " o.remark as remark " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
            " where o.main_tech_id = ?1  limit ?2,?3" ,nativeQuery = true)
    List<Object[]>  getAllOrder(Integer techId, Integer begin ,Integer size);


    @Query(value = "SELECT " +
            " count(*) " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
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
            " o.remark as remark " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
            " LEFT JOIN t_work_detail wd on wd.order_id = o.id" +
            " where o.main_tech_id != ?1 and wd.tech_id = ?1 limit ?2,?3" ,nativeQuery = true)
    List<Object[]>  getCooperationOrder(Integer techId, Integer begin ,Integer size);


    @Query(value = "SELECT " +
            " count(*) " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
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
            " o.remark as remark " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
            " where o.coop_id = ?1 and o.status < 60 limit ?2,?3" ,nativeQuery = true)
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
            " o.remark as remark " +
            " FROM" +
            " t_order o" +
            " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
            " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
            " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
            " where o.coop_id = ?1 and o.status >= 60 limit ?2,?3" ,nativeQuery = true)
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
                " o.remark as remark " +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " where o.status = 0 limit ?1,?2" ,nativeQuery = true)
        List<Object[]>  getNewCreateOrder(Integer begin ,Integer size);


        @Query(value = "SELECT " +
                " count(*) " +
                " FROM" +
                " t_order o" +
                " LEFT JOIN t_technician tech ON tech.id = o.main_tech_id " +
                " LEFT JOIN t_coop_account ca ON ca.id = o.creator_id " +
                " LEFT JOIN t_cooperators ct ON ct.id = o.coop_id" +
                " where o.status = 0" ,nativeQuery = true)
        int getNewCreateCount();


}

