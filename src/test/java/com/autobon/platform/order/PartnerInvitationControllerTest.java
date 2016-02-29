package com.autobon.platform.order;

import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.platform.MvcTest;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by dave on 16/2/29.
 */
public class PartnerInvitationControllerTest extends MvcTest {
    @Autowired TechnicianService technicianService;
    @Autowired OrderService orderService;
    @Value("${com.autobon.test.token}")
    String token;

    private Technician technician;
    private Technician partner;
    private Order order;

    @Before
    public void setUp() {
        technician = technicianService.get(Technician.decodeToken(token));
        partner = new Technician();
        partner.setPhone("tempPhoneNo");
        partner.setPushId("");
        technicianService.save(partner);
        order = new Order();
        order.setOrderNum("test-order-num");
        order.setOrderType(1);

    }

    @Test
    public void invite() throws Exception {

    }


    @Test
    public void accept() throws Exception {

    }
}
