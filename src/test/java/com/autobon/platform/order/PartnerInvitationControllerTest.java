package com.autobon.platform.order;

import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.platform.MvcTest;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        super.setUp();
        technician = technicianService.get(Technician.decodeToken(token));
        partner = new Technician();
        partner.setPhone("tempPhoneNo");
        partner.setPushId("");
        technicianService.save(partner);
        order = new Order();
        order.setOrderNum("test-order-num");
        order.setOrderType(1);
        order.setOrderTime(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        order.setStatus(0);
        order.setMainTechId(technician.getId());
        orderService.save(order);
    }

    @Test
    public void inviteAndAccept() throws Exception {
        final int[] invitationIds = new int[1];
        mockMvcS.perform(post("/api/mobile/technician/order/" + order.getId() + "/invite/" + partner.getId())
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andDo(new ResultHandler() {
                @Override
                public void handle(MvcResult result) throws Exception {
                    String json = result.getResponse().getContentAsString();
                    JsonNode root = new ObjectMapper().readTree(json);
                    invitationIds[0] = root.path("data").path("id").asInt();
                }
            })
            .andExpect(jsonPath("$.result", is(true)));

        mockMvcS.perform(post("/api/mobile/technician/order/invitation/" + invitationIds[0])
                .param("accepted", "true")
                .cookie(new Cookie("autoken", Technician.makeToken(partner.getId()))))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }
}
