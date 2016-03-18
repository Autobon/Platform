package com.autobon.platform.controller.admin;

import com.autobon.order.entity.Construction;
import com.autobon.order.service.ConstructionService;
import com.autobon.shared.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by lu on 2016/3/18.
 */
@RestController("adminConstructionController")
@RequestMapping("/api/web/admin/construction")
public class ConstructionController {
    @Autowired
    private ConstructionService constructionService;
    //修改施工单(施工项和结算金额)
    @RequestMapping(value = "/update/{consId:[\\d]+}", method = RequestMethod.POST)
    public JsonMessage update(@PathVariable("consId") int consId,
                              @RequestParam(value="payment",required = true) Float payment,
                              @RequestParam(value="workItems",required = true) String workItems){
        Construction construction = constructionService.get(consId);
        construction.setPayment(payment);
        construction.setWorkItems(workItems);
        return new JsonMessage(true,"","",construction);
    }
    //显示施工单详细
    @RequestMapping(value="/{consId:[\\d]+}",method = RequestMethod.GET)
    public JsonMessage getConstruction(@PathVariable(value="consId") int consId){
        Construction construction = constructionService.get(consId);
        return new JsonMessage(true,"","",construction);
    }
}
