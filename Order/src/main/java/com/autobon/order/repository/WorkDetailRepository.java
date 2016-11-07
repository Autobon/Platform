package com.autobon.order.repository;

import com.autobon.order.entity.WorkDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2016/11/2.
 */
@Repository
public interface WorkDetailRepository extends JpaRepository<WorkDetail, Integer> {


    WorkDetail findByOrderIdAndTechId(int orderId, int techId);

    @Query(value ="select " +
            " wd.id as id, wd.order_id as orderId, wd.tech_id as techId, " +
            " wd.project1 as project1 ,wd.position1 as position1," +
            " wd.project2 as project2 ,wd.position2 as position2," +
            " wd.project1 as project3 ,wd.position3 as position3," +
            " wd.project1 as project4 ,wd.position4 as position4," +
            " wd.payment as payment, wd.pay_status as payStatus," +
            " wd.create_date as createDate, t.name as techName  from t_order o " +
            " LEFT JOIN t_work_detail wd  on wd.order_id = o.id " +
            " LEFT JOIN t_technician t on t.id = wd.tech_id where o.id= ?1",nativeQuery = true)
    List<Object[]> getByOrderId(int orderId);
}
