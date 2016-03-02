package com.autobon.platform.technician.order;

import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.platform.MvcTest;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by dave on 16/3/2.
 */
public class OrderControllerTest extends MvcTest {
    @Autowired OrderService orderService;
    @Autowired TechnicianService technicianService;
    @Value("${com.autobon.test.token}") String token;
    Technician technician;
    Order myOrder;
    Order newOrder;

    @Before
    public void setup() {
        super.setup();
        technician = technicianService.get(Technician.decodeToken(token));
        myOrder = new Order();
        myOrder.setMainTechId(technician.getId());
        orderService.save(myOrder);
        newOrder = new Order();
        orderService.save(newOrder);
    }

    @Test
    public void listMain() throws Exception {

    }

    @Test
    public void listSecond() throws Exception {

    }


    @Test
    public void show() throws Exception {

    }

    @Test
    public void takeUpOrder() throws Exception {

    }

    @Test
    public void startWork() throws Exception {

    }

    @Test
    public void signIn() throws Exception {

    }

}
