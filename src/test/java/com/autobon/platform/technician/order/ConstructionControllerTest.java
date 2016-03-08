package com.autobon.platform.technician.order;

import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.OrderService;
import com.autobon.platform.MvcTest;
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
    Technician technician;
    Construction construction;

    @Before
    public void setup() {
        super.setup();
        technician = technicianService.get(Technician.decodeToken(token));
        order = new Order();
        order.setMainTechId(technician.getId());
        orderService.save(order);
        construction = new Construction();
        construction.setOrderId(order.getId());
        construction.setTechId(technician.getId());
        construction.setStartTime(new Date());
        construction.setSigninTime(new Date());
        constructionService.save(construction);
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
}
