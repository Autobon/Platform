package com.autobon.order.repository;

import com.autobon.order.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by yuh on 2016/3/2.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer>{

    Comment getByOrderIdAndTechId(int orderId, int techId);

    @Query("select avg(c.star) from Comment c where c.techId = ?1 and c.createAt > ?2")
    float calcStarRateByTechId(int techId, Date fromDate);
}
