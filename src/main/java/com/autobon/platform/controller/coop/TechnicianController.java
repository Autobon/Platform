package com.autobon.platform.controller.coop;

import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.DetailedTechnician;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.DetailedTechnicianService;
import com.autobon.technician.service.TechStatService;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by yuh on 2016/3/21.
 */
@RestController("coopTechnicianController")
@RequestMapping("/api/mobile/coop/technician")
public class TechnicianController {

    @Autowired private OrderService orderService;
    @Autowired private TechnicianService technicianService;
    @Autowired private DetailedTechnicianService detailedTechnicianService;
    @Autowired private TechStatService techStatService;

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public JsonMessage search(@RequestParam("query") String query,
            @RequestParam(value = "page",     defaultValue = "1" )  int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        if (Pattern.matches("\\d{11}", query)) {
            DetailedTechnician t = detailedTechnicianService.getByPhone(query);
            ArrayList<DetailedTechnician> list = new ArrayList<>();
            if (t != null) {
                list.add(t);
                return new JsonMessage(true, "", "", new JsonPage<>(1, 20, 1, 1, 1, list));
            } else {
                return new JsonMessage(true, "", "", new JsonPage<>(1, 20, 0, 0, 0, list));
            }
        }

        return new JsonMessage(true, "", "",
                new JsonPage<>(technicianService.findByName(query, page, pageSize)));
    }

    @RequestMapping(value="/getTechnician",method = RequestMethod.GET)
    public JsonMessage getTechnician(@RequestParam(value="orderId") int orderId){
        Order order = orderService.get(orderId);
        if(order != null){
            int techId = 0 ;
            techId = order.getMainTechId();
            if(techId!=0){
                Map<String,Object> dataMap = new HashMap<String,Object>();
                Technician technician = technicianService.get(techId);
                //获取星级，订单数
                TechStat techStat = techStatService.getByTechId(techId);
                float starRate = -1;
                int totalOrders = -1;
                if(techStat!=null){
                    starRate = techStat.getStarRate();
                    totalOrders = techStat.getTotalOrders();
                    dataMap.put("starRate",starRate);
                    dataMap.put("totalOrders",totalOrders);
                }else{
                    dataMap.put("starRate",null);
                    dataMap.put("totalOrders",null);
                }

                dataMap.put("technician",technician);
                return new JsonMessage(true,"","",dataMap);
            }else{
                return new JsonMessage(false,"ILLEGAL_PARAM","订单没有关联主技师");
            }
        }
        return new JsonMessage(false,"ILLEGAL_PARAM","没有此订单");
    }


}
