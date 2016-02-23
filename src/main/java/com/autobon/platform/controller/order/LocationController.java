package com.autobon.platform.controller.order;

import com.autobon.order.Util.GpsData;
import com.autobon.order.service.LocationService;
import com.autobon.platform.utils.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuh on 2016/2/23.
 */

@RestController
@RequestMapping("/api/mobile")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @RequestMapping(value = "/construction/getLocation", method = RequestMethod.GET)
    public JsonMessage getLocation(
            @RequestParam("gpsData") GpsData gpsData){
        JsonMessage jsonMessage = new JsonMessage(true,"location");
        gpsData = locationService.getLocation(gpsData);
        jsonMessage.setData(gpsData);
        return  jsonMessage;
    }

}
