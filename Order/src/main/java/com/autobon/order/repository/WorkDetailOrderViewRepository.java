package com.autobon.order.repository;

import com.autobon.order.entity.WorkDetailOrderView;
import com.autobon.order.entity.WorkDetailView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by ty on 2017/9/5.
 */
@Repository
public interface WorkDetailOrderViewRepository extends JpaRepository<WorkDetailOrderView, Integer> {
    @Query("select w from WorkDetailOrderView w " +
            "where (?1 is null or w.createTime >= ?1) " +
            "and (?2 is null or w.createTime <= ?2) ")
    Page<WorkDetailOrderView> findViews(Date start, Date end, Pageable pageable);

    @Query(value = "select o.* from t_work_detail_view w LEFT JOIN t_work_detail_order_view o ON o.id = w.order_id " +
            "where w.tech_name=?1 and o.create_time >= ?2 and o.create_time <= ?3 group by o.id LIMIT ?4,?5", nativeQuery = true)
    List<WorkDetailOrderView> findViewsByTech(String tech, Date start, Date end, Integer page ,Integer pageSize);
}
