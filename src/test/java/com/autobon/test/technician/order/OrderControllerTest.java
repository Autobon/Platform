package com.autobon.test.technician.order;

import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import com.autobon.test.MvcTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    public void setup() throws Exception {
        super.setup();
        technician = technicianService.get(Technician.decodeToken(token));
        technician.setStatus(Technician.Status.VERIFIED);
        technicianService.save(technician);
        myOrder = new Order();
        myOrder.setMainTechId(technician.getId());
        orderService.save(myOrder);
        newOrder = new Order();
        newOrder.setOrderType(3);
        orderService.save(newOrder);
    }

    @Test
    public void listMain() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/order/listMain")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void listSecond() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/order/listSecond")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void listUnfinished() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/order/listUnfinished")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.data.count", is(2)));
    }


    @Test
    public void show() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/order/" + myOrder.getId())
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.data.id", is(myOrder.getId())));
    }

    @Test
    public void takeUpOrder() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/order/takeup")
                .param("orderId", "" + newOrder.getId())
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.error", is("TECH_SKILL_NOT_SUFFICIANT")));
    }



}
