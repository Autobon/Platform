package com.autobon.order.repository;

import com.autobon.order.entity.OrderPartnerView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2016/12/8.
 */

@Repository
public interface OrderPartnerViewRepository extends JpaRepository<OrderPartnerView, Integer> {

    Page<OrderPartnerView> findByPartnerTechId(int partnerTechId, Pageable pageable);
}
