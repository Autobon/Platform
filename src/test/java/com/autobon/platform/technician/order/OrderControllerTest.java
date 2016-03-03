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
        mockMvcS.perform(get("/api/mobile/technician/order/listMain")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.data.count", is(0)));
    }

    @Test
    public void listSecond() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/order/listSecond")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.data.count", is(0)));
    }

    @Test
    public void listUnfinished() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/order/listUnfinished")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.data.count", is(3)));
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
            .andExpect(jsonPath("$.data.id", is(newOrder.getId())));
    }

    @Test
    public void signInFailed() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/order/signIn")
                .param("orderId", "" + myOrder.getId())
                .param("positionLon", "12.3665")
                .param("positionLat", "25.5654")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    public void startWorkAndSignIn() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/order/start")
                .param("orderId", "" + myOrder.getId())
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));

        mockMvcS.perform(post("/api/mobile/technician/order/signIn")
                .param("orderId", "" + myOrder.getId())
                .param("positionLon", "12.3665")
                .param("positionLat", "25.5654")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }

}
