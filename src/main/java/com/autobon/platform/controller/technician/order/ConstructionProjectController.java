package com.autobon.platform.controller.technician.order;

import com.autobon.order.service.ConstructionProjectService;
import com.autobon.shared.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wh on 2016/11/1.
 */

@RestController
@RequestMapping("/api/mobile/construction")
public class ConstructionProjectController {

    @Autowired
    ConstructionProjectService constructionProjectService;

    @RequestMapping(value = "/position", method = RequestMethod.GET)
    public JsonMessage getPosition( @RequestParam(value = "project") String project){

        return new JsonMessage(true,"","",constructionProjectService.findByName(project));
    }
}
