package com.autobon.platform.controller.admin;

import com.autobon.getui.PushService;
import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.staff.entity.Staff;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController("adminOrderController")
@RequestMapping("/api/web/admin/order")
public class OrderController {
    private static Logger log = LoggerFactory.getLogger(OrderController.class);
    @Value("${com.autobon.gm-path}") String gmPath;
    @Autowired OrderService orderService;
    @Autowired @Qualifier("PushServiceA")
    PushService pushServiceA;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return new JsonMessage(true, "", "",
                new JsonPage<>(orderService.findAll(page, pageSize)));
    }

    @RequestMapping(value = "/{orderId:[\\d]+}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("orderId") int orderId) {
        Order order = orderService.get(orderId);
        if (order != null) return new JsonMessage(true, "", "", order);
        else return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个订单");
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonMessage createOrder(HttpServletRequest request,
            @RequestParam("orderType")   int orderType,
            @RequestParam("orderTime")   String orderTime,
            @RequestParam("positionLon") String positionLon,
            @RequestParam("positionLat") String positionLat,
            @RequestParam("contactPhone") String contactPhone,
            @RequestParam(value = "photo", defaultValue = "") String photo,
            @RequestParam(value = "remark", required = false) String remark) throws Exception {
        Staff staff = (Staff) request.getAttribute("user");
        if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", orderTime))
            return new JsonMessage(false, "ILLEGAL_PARAM", "订单时间格式不对, 正确格式: 2016-02-10 09:23");

        Order order = new Order();
        order.setCreatorType(2);
        order.setCreatorId(staff.getId());
        order.setCreatorName(staff.getName());
        order.setContactPhone(contactPhone);
        order.setPositionLon(positionLon);
        order.setPositionLat(positionLat);
        order.setOrderType(orderType);
        order.setOrderTime(Date.from(
                LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse(orderTime))
                .atZone(ZoneId.systemDefault()).toInstant()));
        order.setRemark(remark);
        order.setPhoto(photo);
        orderService.save(order);

        String msgTitle = "你收到新订单推送消息";
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "NEW_ORDER");
        map.put("order", order);
        map.put("title", msgTitle);
        boolean result = pushServiceA.pushToApp(msgTitle, new ObjectMapper().writeValueAsString(map), 0);
        if (!result) log.info("订单: " + order.getOrderNum() + "的推送消息发送失败");
        return new JsonMessage(true, "", "", order);
    }


}
