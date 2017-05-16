package com.autobon.platform.controller.coop;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.FavoriteTechnicianView;
import com.autobon.cooperators.service.FavoriteTechnicianService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wh on 2017/5/16.
 */
@RestController
@RequestMapping("/api/mobile/coop")
public class FavoriteTechnicianController {

    @Autowired
    FavoriteTechnicianService favoriteTechnicianService;

    @RequestMapping(value = "/favorite/technician/{technicianId}",method = RequestMethod.POST)
    public JsonMessage save(@PathVariable("technicianId") int technicianId,
                             HttpServletRequest request) {

        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int cooperatorId = coopAccount.getCooperatorId();

        int flag =  favoriteTechnicianService.save(technicianId, cooperatorId);
        if(flag == 1){
            return new JsonMessage(false, "已经收藏过该技师");
        }
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/favorite/technician/{technicianId}",method = RequestMethod.DELETE)
    public JsonMessage delete(@PathVariable("technicianId") int technicianId,
                            HttpServletRequest request) {

        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int cooperatorId = coopAccount.getCooperatorId();

        int flag =  favoriteTechnicianService.delete(technicianId, cooperatorId);
        if(flag == 1){
            return new JsonMessage(false, "没有收藏过该技师");
        }
        return new JsonMessage(true);
    }


    @RequestMapping(value = "/favorite/technician",method = RequestMethod.GET)
    public JsonPage<FavoriteTechnicianView> find(@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                   HttpServletRequest request) {

        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int cooperatorId = coopAccount.getCooperatorId();


        return new JsonPage<>(favoriteTechnicianService.find(cooperatorId, page, pageSize));
    }

}
