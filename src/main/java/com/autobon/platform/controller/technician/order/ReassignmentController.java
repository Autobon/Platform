package com.autobon.platform.controller.technician.order;

import com.autobon.order.service.ReassignmentService;
import com.autobon.shared.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wh on 2016/10/31.
 */

@RestController
@RequestMapping("/api/mobile/order")
public class ReassignmentController {

    @Autowired
    ReassignmentService reassignmentService ;


    /**
     * 申请指派
     * @param orderId 订单ID
     * @param applicant 申请人ID
     * @return
     */
    @RequestMapping(value = "/reassignment", method = RequestMethod.POST)
    public JsonMessage create( @RequestParam(value = "orderId", required = true) String orderId,
                               @RequestParam(value = "applicant", required = true) int applicant){
        reassignmentService.create("12345",112);
        return new JsonMessage(true,"","",null);
    }


    /**
     * 后台处理指派申请
     * @param rid 指派申请单ID
     * @param assignedPerson 被指派人
     * @return
     */
    @RequestMapping(value = "/reassignment", method = RequestMethod.PUT)
    public JsonMessage modify( @RequestParam(value = "rid", required = true) int rid,
                               @RequestParam(value = "assignedPerson", required = true) int assignedPerson){
        reassignmentService.update(rid, assignedPerson);
        return new JsonMessage(true,"","",null);
    }


    /**
     * 查询改派单列表
     * @param rid
     * @param orderId
     * @param applicant
     * @param status
     * @param assignedPerson
     * @return
     */
    @RequestMapping(value = "/reassignment", method = RequestMethod.GET)
    public JsonMessage get( @RequestParam(value = "rid", required = false) int rid,
                            @RequestParam(value = "orderId", required = false) String orderId,
                            @RequestParam(value = "applicant", required = false) int applicant,
                            @RequestParam(value = "status", required = false) int status,
                            @RequestParam(value = "assignedPerson", required = false) int assignedPerson){

        return new JsonMessage(true,"","",null);
    }






}
