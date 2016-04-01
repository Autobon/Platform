package com.autobon.test.technician.order;

import com.autobon.order.entity.Bill;
import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.service.BillService;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.DetailedOrderService;
import com.autobon.order.service.OrderService;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import com.autobon.test.MvcTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by dave on 16/3/15.
 */
public class BillControllerTest extends MvcTest {
    @Autowired TechnicianService technicianService;
    @Autowired OrderService orderService;
    @Autowired ConstructionService constructionService;
    @Autowired BillService billService;
    @Autowired DetailedOrderService detailedOrderService;
    @Value("${com.autobon.test.token}")
    String token;
    Bill bill;

    @Before
    public void setup() throws Exception {
        super.setup();
        Technician tech = technicianService.get(Technician.decodeToken(token));
        Order order = new Order();
        order.setMainTechId(tech.getId());
        order.setOrderType(4);
        order.setStatus(Order.Status.FINISHED);
        order.setFinishTime(new SimpleDateFormat("yyyy-MM-dd").parse("2015-6-3"));
        orderService.save(order);

        Construction cons = new Construction();
        cons.setOrderId(order.getId());
        cons.setTechId(tech.getId());
        cons.setStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2015-6-2"));
        cons.setEndTime(new SimpleDateFormat("yyyy-MM-dd").parse("2015-6-3"));
        cons.setPayment(520f);
        constructionService.save(cons);

        bill = new Bill(tech.getId(), new SimpleDateFormat("yyyy-MM-dd").parse("2015-7-1"));
        bill.setCount(1);
        bill.setSum(520f);
        billService.save(bill);
    }

    @Test
    public void list() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/bill")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.data.count", Matchers.greaterThanOrEqualTo(1)));
    }

    @Test
    public void showOrders() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/bill/" + bill.getId() + "/order")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.data.count", Matchers.greaterThanOrEqualTo(1)));
    }
}
