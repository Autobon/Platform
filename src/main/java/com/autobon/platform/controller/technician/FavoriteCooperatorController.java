package com.autobon.platform.controller.technician;

import com.autobon.cooperators.entity.FavoriteCooperator;
import com.autobon.cooperators.entity.FavoriteCooperatorView;
import com.autobon.cooperators.service.FavoriteCooperatorService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wh on 2017/5/16.
 */
@RestController
@RequestMapping("/api/mobile/technician")
public class FavoriteCooperatorController {

    @Autowired
    FavoriteCooperatorService favoriteCooperatorService;

    @RequestMapping(value = "/favorite/cooperator/{cooperatorId}",method = RequestMethod.POST)
    public JsonMessage save(@PathVariable("cooperatorId") int cooperatorId,
                            HttpServletRequest request) {

        Technician tech = (Technician) request.getAttribute("user");
        int technicianId = tech.getId();

        int flag =  favoriteCooperatorService.save(technicianId, cooperatorId);
        if(flag == 1){
            return new JsonMessage(false, "已经收藏过该商户");
        }
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/favorite/cooperator/{cooperatorId}",method = RequestMethod.DELETE)
    public JsonMessage delete(@PathVariable("cooperatorId") int cooperatorId,
                              HttpServletRequest request) {

        Technician tech = (Technician) request.getAttribute("user");
        int technicianId = tech.getId();

        int flag =  favoriteCooperatorService.delete(technicianId, cooperatorId);
        if(flag == 1){
            return new JsonMessage(false, "没有收藏过该商户");
        }
        return new JsonMessage(true);
    }


    @RequestMapping(value = "/favorite/cooperator",method = RequestMethod.GET)
    public JsonPage<FavoriteCooperatorView> find(@RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                 HttpServletRequest request) {

        Technician tech = (Technician) request.getAttribute("user");
        int technicianId = tech.getId();


        return new JsonPage<>(favoriteCooperatorService.find(technicianId, page, pageSize));
    }


    @RequestMapping(value = "/favorite/cooperator/{cooperatorId}",method = RequestMethod.GET)
    public JsonMessage findCooperator(@PathVariable("cooperatorId") int cooperatorId,
                                      HttpServletRequest request) {

        Technician tech = (Technician) request.getAttribute("user");
        int technicianId = tech.getId();

        FavoriteCooperator favoriteCooperator = favoriteCooperatorService.find(technicianId, cooperatorId);

        if(favoriteCooperator == null){
            return new JsonMessage(false);
        }

        return new JsonMessage(true);

    }
}
