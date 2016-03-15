package com.autobon.platform.controller.technician;


import com.autobon.order.entity.Location;
import com.autobon.order.service.LocationService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.DetailedTechnician;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.DetailedTechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/2/26.
 */
@RestController
@RequestMapping("/api")
public class TechnicianController {
    @Autowired
    private DetailedTechnicianService technicianService;

    @Autowired
    private LocationService locationService;

    @RequestMapping(value = "/mobile/technician/search",method = RequestMethod.GET)
    public JsonMessage search(@RequestParam("query") String query,
            @RequestParam(value = "page",     defaultValue = "1" )  int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        if (Pattern.matches("^[0-9]+$", query)) {
            DetailedTechnician t = technicianService.getByPhone(query);
            ArrayList<DetailedTechnician> list = new ArrayList<>();
            if (t != null) {
                list.add(t);
                return new JsonMessage(true, "", "", new JsonPage<>(1, 20, 1, 1, 1, list));
            } else {
                return new JsonMessage(true, "", "", new JsonPage<>(1, 20, 0, 0, 0, list));
            }
        } else {
            return new JsonMessage(true, "", "",
                    new JsonPage<>(technicianService.findByName(query, page, pageSize)));
        }
    }

    @RequestMapping(value="/mobile/technician/reportLocation",method = RequestMethod.POST)
    public JsonMessage reportLocation(HttpServletRequest request,
            @RequestParam("rtpostionLon") String rtpostionLon,
            @RequestParam("rtpositionLat") String rtpositionLat){
        JsonMessage jsonMessage =new JsonMessage(true,"setLocation");
        Technician technician = (Technician) request.getAttribute("user");
        int technicianId = technician.getId();
        //查询最近的位置信息
        Location latestLocation = locationService.findLatestLocation(technicianId);
        Date nowDate = new Date();
        Location location = new Location();
        location.setAddTime(nowDate);
        location.setRtpositionLat(rtpositionLat);
        location.setRtpositionLon(rtpostionLon);
        location.setTechnicianId(technicianId);
        if(latestLocation!=null) {
            Date latestDate = latestLocation.getAddTime();
            //判断时间间隔是否为一分钟
            Long minCount = (nowDate.getTime() - latestDate.getTime()) / (1000 * 60);
            if (minCount < 1) return jsonMessage;
        }
        locationService.save(location);
        return jsonMessage;
    }


}
