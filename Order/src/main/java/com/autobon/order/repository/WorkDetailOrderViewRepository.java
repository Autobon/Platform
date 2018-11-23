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

    @Query("select w from WorkDetailOrderView w " +
            " where (?1 is null or w.createTime >= ?1) " +
            " and (?2 is null or w.createTime <= ?2) " +
            " and (COALESCE(?3) is null or w.type in (?3))")
    List<WorkDetailOrderView> findViews(Date start, Date end, List<String> ids);

    @Query(value = "select o.* from t_work_detail_view w LEFT JOIN t_work_detail_order_view o ON o.id = w.order_id " +
            "where w.tech_name=?1 and (?2 is null or o.create_time >= ?2) " +
            "and (?3 is null or o.create_time <= ?3) " +
            "and (?4 is null or o.type in ?4) group by o.id", nativeQuery = true)
    List<WorkDetailOrderView> findViewsByTech(String tech, Date start, Date end, List<String> ids);

    @Query("select w from WorkDetailOrderView w " +
            "where (COALESCE(?1) is null or w.id in (?1)) ")
    Page<WorkDetailOrderView> findByIds(List<Integer> idList, Pageable pageable);
}
