package com.autobon.platform.controller.admin;

import com.autobon.order.entity.Bill;
import com.autobon.order.service.*;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    @Autowired OrderService orderService;
    @Autowired TechnicianService technicianService;
    @Autowired ConstructionService constructionService;
    @Autowired DetailedBillService detailedBillService;
    @Autowired DetailedOrderService detailedOrderService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search(
            @RequestParam(value = "month", required = false) String sMonth,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws ParseException {
        if (sMonth != null && Pattern.matches("\\d{4}-\\d{2}", sMonth)) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "月份参数格式错误.正确格式如:2016-01");
        }

        Date month = null;
        Integer techId = null;
        if (sMonth != null) month = new SimpleDateFormat("yyyy-MM").parse(sMonth);
        if (phone != null) {
            Technician tech = technicianService.getByPhone(phone);
            if (tech != null) techId = tech.getId();
        }
        return new JsonMessage(true, "", "", new JsonPage<>(detailedBillService.find(month, paid, techId, page, pageSize)));
    }

    @RequestMapping(value = "/{billId:\\d+}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("billId") int billId) {
        return new JsonMessage(true, "", "", detailedBillService.get(billId));
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
        return new JsonMessage(true, "", "", new JsonPage<>(detailedOrderService.findBetweenByTechId(
                bill.getTechId(), start, end, page, pageSize)));
    }

    @RequestMapping(value = "/{billId:\\d+}/payoff", method = RequestMethod.POST)
    public JsonMessage setPaid(@PathVariable("billId") int billId) {
        Bill bill = billService.get(billId);
        if (bill.isPaid()) return new JsonMessage(false, "ALREADY_PAID_BILL", "订单已支付");

        Date start = bill.getBillMonth();
        Date end = Date.from(start.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().plusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        constructionService.batchPayoff(bill.getTechId(), start, end);
        bill.setPaid(true);
        billService.save(bill);

        return new JsonMessage(true);
    }

    // TODO 在正式发布前删除此接口
    // 立即生成本月帐单
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public JsonMessage generate() {
        Date from = Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        int totalPages, pageNo = 1, billCount = 0;

        do {
            Page<Technician> page = technicianService.findAll(pageNo++, 20);
            totalPages = page.getTotalPages();
            for (Technician t : page.getContent()) {
                Float pay = constructionService.sumPayment(t.getId(), from, to);
                if (pay == null) continue;
                Bill bill = new Bill(t.getId(), from);
                bill.setSum(pay);
                bill.setCount(constructionService.settlePayment(t.getId(), from, to));
                billService.save(bill);
                billCount++;
            }
        } while (pageNo < totalPages);
        return new JsonMessage(true, "", "" + billCount);
    }

    // TODO 在正式发布前删除此接口及Service,repository相关方法
    // 清除所有帐单记录
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public JsonMessage clear() {
        billService.clear();
        return new JsonMessage(true);
    }
}
