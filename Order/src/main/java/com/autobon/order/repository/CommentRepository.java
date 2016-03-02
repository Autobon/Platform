package com.autobon.order.repository;

import com.autobon.order.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yuh on 2016/3/2.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer>{


}
