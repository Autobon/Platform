package com.autobon.platform.controller.admin;

import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.staff.entity.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController("adminOrderController")
@RequestMapping("/api/web/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return new JsonMessage(true, "", "",
                new JsonPage<>(orderService.findAll(page, pageSize)));
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("orderId") int orderId) {
        Order order = orderService.findOrder(orderId);
        if (order != null) return new JsonMessage(true, "", "", order);
        else return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个订单");
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonMessage addOrder(HttpServletRequest request,
            @RequestParam("orderType") int orderType,
            @RequestParam("orderTime") String orderTime,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "remark", required = false) String remark) {
        Staff staff = (Staff) request.getAttribute("user");
        if (!Pattern.matches("^\\d{4}-\\d{2}\\d{2} \\d{2}:\\d{2}$", orderTime))
            return new JsonMessage(false, "ILLEGAL_PARAM", "订单时间格式不对, 正确格式: 2016-02-10 09:23");

        Order order = new Order();
        order.setCreatorType(2);
        order.setCreatorId(staff.getId());
        order.setCreatorName(staff.getName());
        order.setOrderNum(Order.generateOrderNum());
        order.setOrderType(orderType);
        order.setOrderTime(Date.from(
                LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse(orderTime))
                .atZone(ZoneId.systemDefault()).toInstant()));
        order.setRemark(remark);

        return null;
    }
}
