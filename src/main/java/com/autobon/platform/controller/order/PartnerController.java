package com.autobon.platform.controller.order;

import com.autobon.getui.PushService;
import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.platform.utils.JsonMessage;
import com.autobon.technician.entity.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dave on 16/2/28.
 */
@RestController
@RequestMapping("/api/mobile/technician/order")
public class PartnerController {
    @Autowired private OrderService orderService;
    @Autowired private PushService pushService;

    @RequestMapping(value = "/{orderId}/addPartner/", method = RequestMethod.GET)
    public JsonMessage addPartner(HttpServletRequest request,
            @RequestParam("partnerId") int partnerId,
            @PathVariable("orderId")   int orderId) {
        Technician technician = (Technician) request.getAttribute("user");
        return null;
    }
}
