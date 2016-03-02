package com.autobon.platform.controller.order;

import com.autobon.getui.PushService;
import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dave on 16/2/28.
 * 订单邀请合作人相关操作
 */
@RestController
@RequestMapping("/api/mobile/technician/order")
public class PartnerInvitationController {
    private static Logger log = LoggerFactory.getLogger(PartnerInvitationController.class);

    @Autowired private TechnicianService technicianService;
    @Autowired private OrderService orderService;
    @Autowired private PushService pushService;

    /**
     * 发起合作邀请
     * @param request
     * @param partnerId
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/{orderId}/invite/{partnerId}", method = RequestMethod.POST)
    public JsonMessage invitePartner(HttpServletRequest request,
            @PathVariable("partnerId") int partnerId,
            @PathVariable("orderId")   int orderId) throws IOException {
        Technician technician = (Technician) request.getAttribute("user");
        Technician partner = technicianService.get(partnerId);
        Order order = orderService.findOrder(orderId);

        if (order.getStatusCode() >= Order.Status.IN_PROGRESS.getStatusCode() ) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单已进入工作模式");
        } else if (order.getStatus() == Order.Status.INVITATION_ACCEPTED) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单已有人接受合作邀请");
        } else if (order.getMainTechId() == partner.getId()) {
            return new JsonMessage(false, "ILLEGAL_PARAMS", "主技师和合作技师不能为同一人");
        } else if (partner == null) {
            return new JsonMessage(false, "ILLEGAL_PARAMS", "系统中没有邀请的技师");
        }

        order.setSecondTechId(partnerId);
        order.setStatus(Order.Status.SEND_INVITATION);
        orderService.save(order);

        HashMap<String, Object> map = new HashMap<>();
        String title = technician.getName() + "向你发起订单合作邀请";
        map.put("action", "INVITE_PARTNER");
        map.put("title", title);
        map.put("order", order);
        map.put("owner", technician);
        map.put("partner", partner);
        boolean result = pushService.pushToSingle(partner.getPushId(),
                            title,
                            new ObjectMapper().writeValueAsString(map),
                            24*3600);

        if (!result) log.info("推送邀请消息失败");
        return new JsonMessage(true);
    }

    /**
     * 接受或拒绝合作邀请
     * @param request
     * @param accepted
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/{orderId}/invitation", method = RequestMethod.POST)
    public JsonMessage acceptInvitation(HttpServletRequest request,
            @RequestParam("accepted") boolean accepted,
            @PathVariable("orderId") int orderId) throws IOException {
        Technician technician = (Technician) request.getAttribute("user");
        Order order = orderService.findOrder(orderId);

        if (order == null || order.getSecondTechId() != technician.getId()) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "你没有这个邀请, 或订单已改邀他人");
        } else if (order.getStatus() == Order.Status.INVITATION_ACCEPTED) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已接受邀请");
        } else if (order.getStatus() == Order.Status.INVITATION_REJECTED) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已拒绝邀请");
        } else if (order.getStatusCode() > Order.Status.IN_PROGRESS.getStatusCode() ) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单已开始工作或已结束");
        }

        Technician mainTech = technicianService.get(order.getMainTechId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("owner", mainTech);
        map.put("partner", technician);
        if (accepted) {
            order.setStatus(Order.Status.INVITATION_ACCEPTED);
            orderService.save(order);
            String title = technician.getName() + "已接受你的邀请";
            map.put("title", title);
            map.put("action", "INVITATION_ACCEPTED");
            boolean result = pushService.pushToSingle(mainTech.getPushId(),
                                title,
                                new ObjectMapper().writeValueAsString(map),
                                24*3600);
            if (!result) log.info("推送邀请已接受消息失败");
        } else {
            order.setStatus(Order.Status.INVITATION_REJECTED);
            order.setSecondTechId(0);
            orderService.save(order);
            String title = technician.getName() + "已拒绝你的邀请";
            map.put("title", title);
            map.put("action", "INVITATION_REJECTED");
            boolean result = pushService.pushToSingle(mainTech.getPushId(),
                                title,
                                new ObjectMapper().writeValueAsString(map),
                                24*3600);
            if (!result) log.info("推送邀请已接受消息失败");
        }

        return new JsonMessage(true);
    }
}
