package com.autobon.platform.controller.admin;

import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.shared.JsonMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/coopList", method = RequestMethod.POST)
    public JsonMessage coopList(@RequestParam(value="fullname",required = false) String fullname,
                                @RequestParam(value="businessLicense",required = false) String businessLicense,
                                @RequestParam(value="contactPhone",required = false) String contactPhone,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize){

        Page<Cooperator> coopList = cooperatorService.findCoop(fullname,businessLicense,contactPhone,page,pageSize);
        return new JsonMessage(true,"","",coopList);
    }

}
