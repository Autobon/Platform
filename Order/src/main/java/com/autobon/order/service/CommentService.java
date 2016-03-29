package com.autobon.order.service;

import com.autobon.order.entity.Comment;
import com.autobon.order.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yuh on 2016/3/2.
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment get(int id) {
        return commentRepository.findOne(id);
    }

    public int countByTechId(int techId) {
        return commentRepository.countByTechId(techId);
    }

    public Comment getByOrderIdAndTechId(int orderId, int techId) {
        return commentRepository.getByOrderIdAndTechId(orderId, techId);
    }

    // 计算指定日期起的星级平均值
    public float calcStarRateByTechId(int techId, Date fromDate) {
        return commentRepository.calcStarRateByTechId(techId, fromDate);
    }


}
