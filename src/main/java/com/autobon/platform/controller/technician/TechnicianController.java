package com.autobon.platform.controller.technician;


import com.autobon.technician.entity.Location;
import com.autobon.technician.service.LocationService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.DetailedTechnician;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.DetailedTechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        if (Pattern.matches("\\d{11}", query)) {
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

    @RequestMapping(value="/mobile/technician/reportLocation", method = RequestMethod.POST)
    public JsonMessage reportLocation(HttpServletRequest request,
            @RequestParam("lng") String positionLon,
            @RequestParam("lat") String positionLat,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "street", required = false) String street,
            @RequestParam(value = "streetNumber", required = false) String streetNumber) {
        Technician tech = (Technician) request.getAttribute("user");

        //查询最近的位置信息
        Page<Location> locations = locationService.findByTechId(tech.getId(), 1, 1);
        if (locations.getNumberOfElements() > 0) {
            Location l = locations.getContent().get(0);
            if (new Date().getTime() - l.getCreateAt().getTime() < 60*1000) {
                return new JsonMessage(false, "TOO_FREQUENT_REQUEST", "请求间隔不得少于1分钟");
            }
        }

        Location location = new Location();
        location.setTechId(tech.getId());
        location.setCreateAt(new Date());
        location.setLat(positionLat);
        location.setLng(positionLon);
        location.setProvince(province);
        location.setCity(city);
        location.setDistrict(district);
        location.setStreet(street);
        location.setStreetNumber(streetNumber);
        locationService.save(location);
        return new JsonMessage(true);
    }

}
