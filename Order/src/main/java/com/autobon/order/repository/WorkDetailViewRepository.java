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
    @Query("select w from WorkDetailView w where w.techId = ?1 ")
    Page<WorkDetailView> findViews(int techId, Pageable pageable);
}
