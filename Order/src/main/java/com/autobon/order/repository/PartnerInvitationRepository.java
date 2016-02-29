package com.autobon.order.repository;

import com.autobon.order.entity.PartnerInvitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by dave on 16/2/29.
 */
@Repository
public interface PartnerInvitationRepository extends JpaRepository<PartnerInvitation, Integer> {

    @Query("from PartnerInvitation pi where pi.order.id = ?1")
    Page<PartnerInvitation> findByOrderId(int orderId, Pageable pageable);

    @Query("from PartnerInvitation pi where pi.invitedTech.id = ?1")
    Page<PartnerInvitation> findByInvitedTechId(int invitedTechId, Pageable pageable);

    @Query("from PartnerInvitation pi where pi.orderId = ?1 and pi.invitedTechId = ?2")
    Page<PartnerInvitation> findByOrderIdAndInvitedTechId(int orderId, int invitedTechId, Pageable pageable);

    @Query("update PartnerInvitation pi set pi.status = 3 where pi.orderId = ?1")
    int expireOrderInvitaions(int orderId);
}
