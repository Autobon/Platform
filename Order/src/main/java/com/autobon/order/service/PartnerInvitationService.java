package com.autobon.order.service;

import com.autobon.order.entity.PartnerInvitation;
import com.autobon.order.repository.PartnerInvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by dave on 16/2/29.
 */
@Service
public class PartnerInvitationService {
    @Autowired
    private PartnerInvitationRepository repository;

    public PartnerInvitation save(PartnerInvitation invitation) {
        return repository.save(invitation);
    }

    public PartnerInvitation get(int id) {
        return repository.findOne(id);
    }

    public Page<PartnerInvitation> findByOrderId(int orderId, int page, int pageSize) {
        return repository.findByOrderId(orderId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

    public Page<PartnerInvitation> findByInvitedTechId(int invitedTechId, int page, int pageSize) {
        return repository.findByInvitedTechId(invitedTechId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

    public PartnerInvitation findLastByOrderIdAndInvitedTechId(int orderId, int invitedTechId) {
        Page<PartnerInvitation> page = repository.findByOrderIdAndInvitedTechId(orderId, invitedTechId,
                new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "createdAt")));
        if (page.getNumberOfElements() > 0) return page.getContent().get(0);
        else return null;
    }
}
