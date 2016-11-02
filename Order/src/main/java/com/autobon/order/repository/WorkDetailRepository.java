package com.autobon.order.repository;

import com.autobon.order.entity.WorkDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2016/11/2.
 */
@Repository
public interface WorkDetailRepository extends JpaRepository<WorkDetail, Integer> {

}
