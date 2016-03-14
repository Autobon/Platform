package com.autobon.platform.controller.admin;

import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.cooperators.service.ReviewCooperService;
import com.autobon.shared.JsonMessage;
import com.autobon.staff.entity.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by yuh on 2016/3/10.
 */
@RestController("adminCooperatorController")
@RequestMapping("/api/web/admin/cooperator")
public class CooperatorController {

    private static Logger log = LoggerFactory.getLogger(CooperatorController.class);

    private CooperatorService cooperatorService;

    @Autowired
    public void setCooperatorService(CooperatorService cooperatorService){
        this.cooperatorService = cooperatorService;
    }

    @Autowired
    private ReviewCooperService reviewCooperService;

    @RequestMapping(value = "/coopList", method = RequestMethod.POST)
    public JsonMessage coopList(@RequestParam(value="fullname",required = false) String fullname,
                                @RequestParam(value="businessLicense",required = false) String businessLicense,
                                @RequestParam(value="statusCode",required = false) Integer statusCode,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize){

        Page<Cooperator> coopList = cooperatorService.findCoop(fullname,businessLicense,statusCode,page,pageSize);
        return new JsonMessage(true,"","",coopList);
    }


    @RequestMapping(value="/getCoop",method = RequestMethod.GET)
    public JsonMessage getCoop(@RequestParam(value="coopId") int coopId){
        Cooperator cooperator = cooperatorService.get(coopId);
        return new JsonMessage(true,"","",cooperator);
    }


    @RequestMapping(value="/checkCoop/{coopId:[\\d]+}",method = RequestMethod.POST)
    public JsonMessage checkCoop(HttpServletRequest request,
                                 @PathVariable("coopId") int coopId,
                                 @RequestParam(value = "statusCode") int statusCode,
                                 @RequestParam(value = "resultDesc",required = false) String resultDesc){
        Cooperator cooperator = cooperatorService.get(coopId);
        if (cooperator != null) {
            ReviewCooper reviewCooper = new ReviewCooper();
            reviewCooper.setCooperatorsId(coopId);
            reviewCooper.setReviewTime(new Date());
            Staff staff = (Staff) request.getAttribute("user");
            reviewCooper.setCheckedBy(staff.getName());
            if(statusCode == 2){
                if(resultDesc!=null){
                    reviewCooper.setResultDesc(resultDesc);
                }
            }
            reviewCooperService.save(reviewCooper);
            return new JsonMessage(true,"","",reviewCooper);
        } else {
            return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个合作商户");
        }

    }




}
