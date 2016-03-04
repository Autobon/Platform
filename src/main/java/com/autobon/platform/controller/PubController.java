package com.autobon.platform.controller;

import com.autobon.order.entity.WorkItem;
import com.autobon.order.service.WorkItemService;
import com.autobon.shared.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yuh on 2016/2/18.
 */
@RestController
@RequestMapping("/api/mobile/pub")
public class PubController {

    @Autowired
    private WorkItemService workItemService;

    @RequestMapping(value = "/technician/skills", method = RequestMethod.GET)
    public JsonMessage getSkills() {
        return getOrderTypes();
    }

    @RequestMapping(value = "/orderTypes", method = RequestMethod.GET)
    public JsonMessage getOrderTypes() {
        return new JsonMessage(true, "", "",
                workItemService.getOrderTypes().stream().map(t -> {
                    HashMap<String, Object> m = new HashMap<>();
                    m.put("id", t.getOrderType());
                    m.put("name", t.getOrderTypeName());
                    return m;
                }).collect(Collectors.toList()));
    }

    @RequestMapping(value="/technician/workItems", method = RequestMethod.GET)
    public JsonMessage getWorkItems(
            @RequestParam("orderType") int orderType,
            @RequestParam(value = "carSeat", defaultValue = "0")   int carSeat) {
        List<WorkItem> list = null;
        if (carSeat == 0) list = workItemService.findByOrderType(orderType);
        else list = workItemService.findByOrderTypeAndCarSeat(orderType, carSeat);
        return new JsonMessage(true, "", "", list.stream().map(i -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", i.getId());
                    map.put("seat", i.getCarSeat());
                    map.put("name", i.getWorkName());
                    return map;
                }).collect(Collectors.toList()));
    }

}
