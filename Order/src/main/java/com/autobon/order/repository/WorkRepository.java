package com.autobon.order.repository;

import com.autobon.order.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/2/29.
 */
@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {

    List<Work> findByOrderType(int orderType);
}
