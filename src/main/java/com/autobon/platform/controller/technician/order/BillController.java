package com.autobon.platform.controller.technician.order;

import com.autobon.order.entity.Bill;
import com.autobon.order.service.BillService;
import com.autobon.order.service.DetailedOrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by dave on 16/3/15.
 */
@RestController
@RequestMapping("/api/mobile/technician/bill")
public class BillController {
    @Autowired TechnicianService technicianService;
    @Autowired BillService billService;
    @Autowired DetailedOrderService detailedOrderService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage list(HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician tech = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(billService.findByTechId(tech.getId(), page, pageSize)));
    }

    @RequestMapping(value = "/{billId:\\d+}/order", method = RequestMethod.GET)
    public JsonMessage showOrders(HttpServletRequest request,
            @PathVariable(value = "billId") int billId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician tech = (Technician) request.getAttribute("user");
        Bill bill = billService.get(billId);

        if (bill == null || bill.getTechId() != tech.getId()) {
            return new JsonMessage(false, "ILLEGAL_BILL_ID", "你没有这个帐单");
        } else {
            Date from = Date.from(bill.getBillMonth().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime()
                    .minusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
            return new JsonMessage(true, "", "",
                    new JsonPage<>(detailedOrderService.findBetweenByTechId(
                            tech.getId(), from, bill.getBillMonth(), page, pageSize)));
        }
    }


}
