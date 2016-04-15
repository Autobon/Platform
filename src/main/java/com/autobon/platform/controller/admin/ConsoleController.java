package com.autobon.platform.controller.admin;

import com.autobon.order.entity.SysStat;
import com.autobon.order.service.SysStatService;
import com.autobon.platform.listener.CooperatorEventListener;
import com.autobon.platform.listener.OrderEventListener;
import com.autobon.platform.listener.TechnicianEventListener;
import com.autobon.shared.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dave on 16/4/15.
 */
@RestController
@RequestMapping("/api/web/admin/console")
public class ConsoleController {
    @Autowired OrderEventListener orderListener;
    @Autowired TechnicianEventListener technicianListener;
    @Autowired CooperatorEventListener cooperatorListener;
    @Autowired SysStatService sysStatService;

    @RequestMapping(value = "/statInfo", method = RequestMethod.GET)
    public JsonMessage getStatInfo() {
        HashMap<String, Object> map = new HashMap<>();

        SysStat thisDayStat = new SysStat();
        thisDayStat.setStatType(1);
        thisDayStat.setStatTime(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        thisDayStat.setNewOrderCount(orderListener.getNewOrderCountOfToday());
        thisDayStat.setFinishedOrderCount(orderListener.getFinishedOrderCountOfToday());
        thisDayStat.setNewCoopCount(cooperatorListener.getNewCooperatorCountOfToday());
        thisDayStat.setVerifiedCoopCount(cooperatorListener.getVerifiedCooperatorCountOfToday());
        thisDayStat.setNewTechCount(technicianListener.getNewTechnicianCountOfToday());
        thisDayStat.setVerifiedTechCount(technicianListener.getVerifiedTechnicianCountOfToday());
        map.put("thisDay", thisDayStat);

        SysStat thisMonthStat = new SysStat();
        thisMonthStat.setStatType(1);
        thisMonthStat.setStatTime(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        thisMonthStat.setNewOrderCount(orderListener.getNewOrderCountOfThisMonth());
        thisMonthStat.setFinishedOrderCount(orderListener.getFinishedOrderCountOfThisMonth());
        thisMonthStat.setNewCoopCount(cooperatorListener.getNewCooperatorCountOfThisMonth());
        thisMonthStat.setVerifiedCoopCount(cooperatorListener.getVerifiedCooperatorCountOfThisMonth());
        thisMonthStat.setNewTechCount(technicianListener.getNewTechnicianCountOfThisMonth());
        thisMonthStat.setVerifiedTechCount(technicianListener.getVerifiedTechnicianCountOfThisMonth());
        map.put("thisMonth", thisMonthStat);

        map.put("lastDay", sysStatService.getOfDay(Date.from(LocalDate.now().minusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
        map.put("lastMonth", sysStatService.getOfMonth(Date.from(LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
        map.put("totalOrder", new int[]{orderListener.getTotalCreatedOrderCount(), orderListener.getTotalFinishedOrderCount()});
        map.put("totalCoop", new int[]{cooperatorListener.getTotalRegisteredCooperatorCount(), cooperatorListener.getTotalVerifiedCooperatorCount()});
        map.put("totalTech", new int[]{technicianListener.getTotalRegisteredTechnicianCount(), technicianListener.getTotalVerifiedTechnicianCount()});
        return new JsonMessage(true, "", "", map);
    }
}
