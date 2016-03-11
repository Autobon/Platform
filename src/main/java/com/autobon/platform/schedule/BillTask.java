package com.autobon.platform.schedule;

import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 技师每月帐单任务计划
 * Created by dave on 16/3/11.
 */
@Component
public class BillTask {
    private static final Logger log = LoggerFactory.getLogger(BillTask.class);

    @Autowired TechnicianService technicianService;

    // 每月1号执行月度帐单结算
    @Async
    @Scheduled(cron = "0 0 2 1 * ?")
    public void assembleBills() {
        Date fromYearMonth = Date.from(LocalDate.now().minusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date toYearMonth = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        int totalPages = 0;
        int pageNo = 0;
        do {
            Page<Technician> page = technicianService.findActivedFrom(fromYearMonth, pageNo++, 20);
            totalPages = page.getTotalPages();
            // 创建月度帐单,查询起止日期间内的施工单
        } while (pageNo < totalPages);

    }
}
