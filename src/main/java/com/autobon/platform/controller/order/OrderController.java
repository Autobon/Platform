package com.autobon.platform.controller.order;

import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.platform.utils.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yuh on 2016/2/22.
 */
@RestController
@RequestMapping("/api/mobile")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/order/orderList", method = RequestMethod.GET)
    public JsonMessage orderList() throws Exception{
        JsonMessage jsonMessage = new JsonMessage(true,"orderList");
        List<Order> orderList = orderService.getOrderList();
        jsonMessage.setData(orderList);
        return jsonMessage;

    }

    @RequestMapping(value = "/order/getLocation", method = RequestMethod.GET)
    public JsonMessage getLocation(
            @RequestParam("orderId") int orderId){
        JsonMessage jsonMessage = new JsonMessage(true,"location");
        Order order  = orderService.getLocation(orderId);

        jsonMessage.setData(order);
        return  jsonMessage;
    }

}
