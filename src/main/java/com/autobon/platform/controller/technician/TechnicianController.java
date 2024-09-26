package com.autobon.platform.controller.technician;


import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.DetailedTechnician;
import com.autobon.technician.entity.Location;
import com.autobon.technician.entity.TechFinanceView;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.DetailedTechnicianService;
import com.autobon.technician.service.LocationService;
import com.autobon.technician.service.TechFinanceService;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Autowired
    private TechnicianService technicianService1;

    @Autowired
    private TechFinanceService techFinanceService;

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


    /**
     * 车邻邦二期
     * 查询技师
     * @param query 查询内容 纯数字则查询手机 反之查询姓名
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/mobile/v2/technician/search",method = RequestMethod.GET)
    public JsonResult getTech(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam("query") String query,
                              @RequestParam(value = "page",  defaultValue = "1" )  int page,
                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
//        Technician tech = (Technician) request.getAttribute("user");
//        if(tech == null){
//            return new JsonResult(false, "登陆过期");
//        }

        Page<Technician> technicians;

        String query1 = "%"+query+"%";
        if (Pattern.matches("\\d+", query)) {
            technicians = technicianService1.find(query1, null, page, pageSize);

        }else{
            technicians = technicianService1.find(null, query1, page, pageSize);

        }


        return new JsonResult(true, new JsonPage<>(technicians));
    }


    /**
     * 车邻帮二期
     * 上报经纬度
     * @param request
     * @param positionLon
     * @param positionLat
     * @param province
     * @param city
     * @param district
     * @param street
     * @param streetNumber
     * @return
     */
    @RequestMapping(value="/mobile/technician/v2/reportLocation", method = RequestMethod.POST)
    public JsonResult uploadLocation(HttpServletRequest request,
                                      @RequestParam("lng") String positionLon,
                                      @RequestParam("lat") String positionLat,
                                      @RequestParam("province") String province,
                                      @RequestParam("city") String city,
                                      @RequestParam(value = "district", required = false) String district,
                                      @RequestParam(value = "street", required = false) String street,
                                      @RequestParam(value = "streetNumber", required = false) String streetNumber) {

        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }
        //查询最近的位置信息
        Page<Location> locations = locationService.findByTechId(tech.getId(), 1, 1);
        if (locations.getNumberOfElements() > 0) {
            Location l = locations.getContent().get(0);
            if (new Date().getTime() - l.getCreateAt().getTime() < 60*1000) {
                return new JsonResult(false,   "请求间隔不得少于1分钟");
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
        return new JsonResult(true, "上传成功");
    }

    /**
     *查询所有技师的账单列表
     * @param request
     * @param response
     * @param phone
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/technician/finance",method = RequestMethod.GET)
    public JsonResult getTech(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "page",  defaultValue = "1" )  int page,
                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Page<TechFinanceView> views = techFinanceService.find(phone, name, page, pageSize);
        return new JsonResult(true, new JsonPage<>(views));
    }

}
