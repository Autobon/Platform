package com.autobon.platform.controller.order;

import com.autobon.order.service.OrderService;
import com.autobon.platform.utils.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

        return jsonMessage;

    }
}
