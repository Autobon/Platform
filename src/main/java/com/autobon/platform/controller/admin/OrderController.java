package com.autobon.platform.controller.admin;

import com.autobon.order.entity.DetailedOrder;
import com.autobon.order.service.DetailedOrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by dave on 16/3/1.
 */
@RestController("adminOrderController")
@RequestMapping("/api/admin/order")
public class OrderController {
    @Autowired
    DetailedOrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return new JsonMessage(true, "", "",
                new JsonPage<>(orderService.findAll(page, pageSize)));
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("orderId") int orderId) {
        DetailedOrder order = orderService.get(orderId);
        if (order != null) return new JsonMessage(true, "", "", order);
        else return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个订单");
    }
}
