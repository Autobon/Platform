package com.autobon.platform.controller.technician.order;

import com.autobon.order.service.ReassignmentService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wh on 2016/10/31.
 */

@RestController
@RequestMapping("/api/web/admin/order")
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
    public JsonResult create( @RequestParam(value = "orderId", required = true) int orderId,
                               @RequestParam(value = "applicant", required = true) int applicant){
        reassignmentService.create(orderId,applicant);
        return new JsonResult(true,"处理成功");
    }


    /**
     * 后台处理指派申请
     * @param rid 指派申请单ID
     * @param assignedPerson 被指派人
     * @return
     */
    @RequestMapping(value = "/reassignment", method = RequestMethod.PUT)
    public JsonResult modify( @RequestParam(value = "rid", required = true) int rid,
                               @RequestParam(value = "assignedPerson", required = true) int assignedPerson){
        reassignmentService.update(rid, assignedPerson);
        return new JsonResult(true,"处理成功");
    }


    /**
     * 查询改派单列表
     * @param applicant
     * @param status
     * @param assignedPerson
     * @return
     */
    @RequestMapping(value = "/reassignment", method = RequestMethod.GET)
    public JsonResult get( @RequestParam(value = "applicant", required = false) Integer applicant,
                            @RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "assignedPerson", required = false) Integer assignedPerson,
                            @RequestParam(value = "currentPage", required = false) Integer currentPage,
                            @RequestParam(value = "pageSize", required = false) Integer pageSize){

        return new JsonResult(true, reassignmentService.get(applicant,assignedPerson,status,currentPage,pageSize));
    }


}
