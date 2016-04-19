package com.autobon.platform.controller.admin;

import com.autobon.order.entity.SysStat;
import com.autobon.order.service.SysStatService;
import com.autobon.shared.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/4/14.
 */
@RestController
@RequestMapping("/api/web/admin/stat")
public class StatController {
    @Autowired SysStatService sysStatService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage getLast(
            @RequestParam("start") String start,
            @RequestParam("end") String end,
            @RequestParam("type") int type) throws ParseException {
        if (type != 1 && type != 2) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "错误的type参数");
        }

        if ((type == 1 && (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", start) || !Pattern.matches("\\d{4}-\\d{2}-\\d{2}", end)))
                || (type == 2 && (!Pattern.matches("\\d{4}-\\d{2}", start) || !Pattern.matches("\\d{4}-\\d{2}", end)))) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "日期起止参数格式错误");
        }

        Date dStart, dEnd;
        List<SysStat> statList;
        if (type == 1) {
            dStart = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            dEnd = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            LocalDate ldStart = dStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ldLimit = dEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusMonths(3);
            if (ldLimit.isAfter(ldStart)) {
                dStart = Date.from(ldLimit.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            }
            statList = sysStatService.findBetweenOfDay(dStart, dEnd);
        } else {
            dStart = new SimpleDateFormat("yyyy-MM").parse(start);
            dEnd = new SimpleDateFormat("yyyy-MM").parse(end);
            LocalDate ldStart = dStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ldLimit = dEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusYears(2);
            if (ldLimit.isAfter(ldStart)) {
                dStart = Date.from(ldLimit.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            }
            statList = sysStatService.findBetweenOfMonth(dStart, dEnd);
        }
        return new JsonMessage(true, "", "", statList);
    }
}
