package com.autobon.platform.controller;


import com.autobon.shared.JsonMessage;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yuh on 2016/2/18.
 */
@RestController
@RequestMapping("/api/mobile")
public class PubController {
    @Value("${com.autobon.skill}")
    private String skill;

    @Autowired
    private TechnicianService technicianService;

    @RequestMapping(value = "/pub/getSkill", method = RequestMethod.GET)
    public JsonMessage getSkill() throws Exception{
        String[] skills = skill.split(",");
        JsonMessage jsonMessage = new JsonMessage(true,"skill");
        jsonMessage.setData(skills);

        return jsonMessage;
    }

    @RequestMapping(value="/pub/getWork", method = RequestMethod.GET)
    public JsonMessage getWork(String codemap) throws Exception{
        List<String> workList = technicianService.getWorkByCodemap(codemap);
        JsonMessage jsonMessage = new JsonMessage(true,"work");
        jsonMessage.setData(workList);
        return jsonMessage;
    }


}
