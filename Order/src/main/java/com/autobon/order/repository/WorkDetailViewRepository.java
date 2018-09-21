package com.autobon.order.repository;

import com.autobon.order.entity.WorkDetailView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by ty on 2017/9/5.
 */
@Repository
public interface WorkDetailViewRepository extends JpaRepository<WorkDetailView, Integer> {
    @Query("select w from WorkDetailView w where w.techId = ?1 and w.createDate>'2018-08-31 23:59:59'")
    Page<WorkDetailView> findViews(int techId, Pageable pageable);

    @Query("select w from WorkDetailView w where w.createDate>'2018-08-31 23:59:59' order by w.techId")
    List<WorkDetailView> findAllViews();
}
