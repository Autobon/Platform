package com.autobon.platform.controller.order;

import com.autobon.getui.PushService;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderInvitation;
import com.autobon.order.repository.OrderRepository;
import com.autobon.order.service.OrderService;
import com.autobon.platform.utils.JsonMessage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dave on 16/2/28.
 * 订单邀请合伙人相关操作
 */
@RestController
@RequestMapping("/api/mobile/technician/order")
public class InvitationController {
    @Autowired private TechnicianService technicianService;
    @Autowired private OrderService orderService;
    @Autowired private PushService pushService;
    @Autowired private OrderRepository orderRepository; // TODO 不要直接使用Repository

    @RequestMapping(value = "/{orderId}/invitePartner/", method = RequestMethod.GET)
    public JsonMessage invitePartner(HttpServletRequest request,
            @RequestParam("partnerId") int partnerId,
            @PathVariable("orderId")   int orderId) throws IOException {
        Technician technician = (Technician) request.getAttribute("user");
        Technician partner = technicianService.get(partnerId);
        Order order = orderRepository.findOne(orderId);

        if (order.getStatus() >= 1 ) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单已进入工作模式或已有人接单");
        } else if (order.getMainTechId() == technician.getId()) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "主技师和合作技师不能为同一人");
        }
        if (partner == null) return new JsonMessage(false, "ILLEGAL_PARAMS", "系统中没有指定的技师");

        OrderInvitation invitation = new OrderInvitation();
        invitation.setOrder(order);
        invitation.setMainTech(technician);
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "INVITE_PARTNER");
        map.put("invitation", invitation);
        boolean result = pushService.pushToSingle(partner.getPushId(), technician.getName() + "向你发起订单合作邀请",
                new ObjectMapper().writeValueAsString(map), 2*3600);

        if (result) return new JsonMessage(true, "已发送邀请消息");
        else return new JsonMessage(false, "推送邀请消息失败");
    }
}
