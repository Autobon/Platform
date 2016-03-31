package com.autobon.platform.controller.admin;

import com.autobon.order.entity.Bill;
import com.autobon.order.service.BillService;
import com.autobon.order.service.DetailedOrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/31.
 */
@RestController("adminBillController")
@RequestMapping("/api/web/admin/bill")
public class BillController {
    @Autowired BillService billService;
    @Autowired DetailedOrderService orderService;
    @Autowired TechnicianService technicianService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search(
            @RequestParam("month") String sMonth,
            @RequestParam("paid") Boolean paid,
            @RequestParam("phone") String phone,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws ParseException {
        if (sMonth != null && Pattern.matches("\\d{4}-\\d{2}", sMonth)) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "月份参数格式错误.正确格式如:2016-01");
        }

        Date month = new SimpleDateFormat("yyyy-MM").parse(sMonth);
        Integer techId = null;
        if (phone != null) {
            Technician tech = technicianService.getByPhone(phone);
            if (tech != null) techId = tech.getId();
        }
        return new JsonMessage(true, "", "", billService.find(month, paid, techId, page, pageSize));
    }

    @RequestMapping(value = "/{billId:\\d+}/order", method = RequestMethod.GET)
    public JsonMessage getOrders(
            @PathVariable("billId") int billId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Bill bill = billService.get(billId);
        Date start = bill.getBillMonth();
        Date end = Date.from(start.toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate().plusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        return new JsonMessage(true, "", "", new JsonPage<>(orderService.findBetweenByTechId(
                    bill.getTechId(), start, end, page, pageSize)));
    }

    @RequestMapping(value = "/{billId:\\d+}/pay", method = RequestMethod.POST)
    public JsonMessage setPaid(@PathVariable("billId") int billId) {
        Bill bill = billService.get(billId);

        return new JsonMessage(true);
    }
}
