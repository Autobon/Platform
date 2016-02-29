package com.autobon.order.repository;

import com.autobon.order.entity.OrderInvitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by dave on 16/2/29.
 */
@Repository
public interface OrderInvitationRepository extends JpaRepository<OrderInvitation, Integer> {

    @Query("from OrderInvitation oi where oi.order.id = ?1")
    Page<OrderInvitation> findByOrderId(int orderId, Pageable pageable);

    @Query("from OrderInvitation oi where oi.invitedTech.id = ?1")
    Page<OrderInvitation> findByInvitedTechId(int invitedTechId, Pageable pageable);
}
