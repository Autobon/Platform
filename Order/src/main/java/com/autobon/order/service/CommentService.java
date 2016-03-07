package com.autobon.order.service;

import com.autobon.order.entity.Comment;
import com.autobon.order.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Comment getByOrderIdAndTechId(int orderId, int techId) {
        return commentRepository.getByOrderIdAndTechId(orderId, techId);
    }


}
