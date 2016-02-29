package com.autobon.order.service;

import com.autobon.order.entity.OrderInvitation;
import com.autobon.order.repository.OrderInvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by dave on 16/2/29.
 */
@Service
public class OrderInvitationService {
    @Autowired
    private OrderInvitationRepository orderInvitationRepository;

    private OrderInvitation get(int id) {
        return orderInvitationRepository.findOne(id);
    }

    private Page<OrderInvitation> findByOrderId(int orderId, int page, int pageSize) {
        return orderInvitationRepository.findByOrderId(orderId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

    private Page<OrderInvitation> findByInvitedTechId(int invitedTechId, int page, int pageSize) {
        return orderInvitationRepository.findByInvitedTechId(invitedTechId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }
}
