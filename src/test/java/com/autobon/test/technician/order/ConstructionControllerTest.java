package com.autobon.test.technician.order;

import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.OrderService;
import com.autobon.test.MvcTest;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
/**
 * Created by lu on 2016/3/8.
 */
public class ConstructionControllerTest  extends MvcTest {
    @Autowired OrderService orderService;
    @Autowired TechnicianService technicianService;
    @Autowired ConstructionService constructionService;
    @Value("${com.autobon.test.token}") String token;
    Order order;
    Order newOrder;
    Technician technician;
    Construction construction;

    @Before
    public void setup() throws Exception {
        super.setup();
        technician = technicianService.get(Technician.decodeToken(token));
        order = new Order();
        order.setMainTechId(technician.getId());
        order.setOrderType(4);
        order.setStatus(Order.Status.IN_PROGRESS);
        orderService.save(order);
        construction = new Construction();
        construction.setOrderId(order.getId());
        construction.setTechId(technician.getId());
        construction.setStartTime(new Date());
        construction.setSigninTime(new Date());
        constructionService.save(construction);

        newOrder = new Order();
        newOrder.setMainTechId(technician.getId());
        orderService.save(newOrder);
    }

    @Test
    public void signInFailed() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/construct/signIn")
                .param("orderId", "" + order.getId())
                .param("positionLon", "12.3665")
                .param("positionLat", "25.5654")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    public void startWorkAndSignIn() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/construct/start")
                .param("orderId", "" + newOrder.getId())
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));

        mockMvcS.perform(post("/api/mobile/technician/construct/signIn")
                .param("orderId", "" + newOrder.getId())
                .param("positionLon", "12.3665")
                .param("positionLat", "25.5654")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void setBeforePhoto() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/construct/beforePhoto")
                .param("orderId", "" + order.getId())
                .param("urls", "a.jpg,b.jpg")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void finish() throws Exception {
        construction.setBeforePhotos("a.jpg");
        constructionService.save(construction);
        mockMvcS.perform(post("/api/mobile/technician/construct/finish")
                .param("orderId", "" + order.getId())
                .param("afterPhotos", "a.jpg,b.jpg,c.jpg")
                .param("percent", "0.2")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));

        mockMvcS.perform(get("/api/mobile/technician/order/" + order.getId())
                .param("orderId", "" + order.getId())
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }
}
