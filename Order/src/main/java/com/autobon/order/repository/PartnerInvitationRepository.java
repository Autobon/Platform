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

    @Query("from OrderInvitation oi where oi.order.id = ?1")
    Page<PartnerInvitation> findByOrderId(int orderId, Pageable pageable);

    @Query("from OrderInvitation oi where oi.invitedTech.id = ?1")
    Page<PartnerInvitation> findByInvitedTechId(int invitedTechId, Pageable pageable);

    @Query("from OrderInvitation oi where oi.orderId = ?1 and oi.invitedTechId = ?2")
    Page<PartnerInvitation> findByOrderIdAndInvitedTechId(int orderId, int invitedTechId, Pageable pageable);
}
