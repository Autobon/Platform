package com.autobon.platform.controller.coop;

import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuh on 2016/3/21.
 */
@RestController("coopTechnicianController")
@RequestMapping("/api/mobile/coop/technician")
public class TechnicianController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TechnicianService technicianService;

    @RequestMapping(value="/getTechnician",method = RequestMethod.GET)
    public JsonMessage getTechnician(@RequestParam(value="orderId") int orderId){
        Order order = orderService.get(orderId);
        int techId = order.getMainTechId();
        Technician technician = technicianService.get(techId);
        return new JsonMessage(true,"","",technician);
    }


}
