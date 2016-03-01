package com.autobon.platform.controller.order;

import com.autobon.getui.PushService;
import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.platform.utils.JsonMessage;
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

        if (order.getStatus() >= 1 ) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单已进入工作模式或已有人接单");
        } else if (order.getMainTechId() == partner.getId()) {
            return new JsonMessage(false, "ILLEGAL_PARAMS", "主技师和合作技师不能为同一人");
        } else if (partner == null) {
            return new JsonMessage(false, "ILLEGAL_PARAMS", "系统中没有邀请的技师");
        }

        order.setSecondTechId(partnerId);
        order.setEnumStatus(Order.EnumStatus.INVITATION_NOT_ACCEPTED);
        orderService.save(order);

        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "INVITE_PARTNER");
        map.put("order", order);
        map.put("owner", technician);
        map.put("partner", partner);
        String json = new ObjectMapper().writeValueAsString(map);
        boolean result = pushService.pushToSingle(partner.getPushId(),
                technician.getName() + "向你发起订单合作邀请",
                json, 24*3600);

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
        } else if (order.getEnumStatus() == Order.EnumStatus.INVITATION_ACCEPTED) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已接受邀请");
        } else if (order.getEnumStatus() == Order.EnumStatus.INVITATION_REJECT) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已拒绝邀请");
        } else if (order.getStatus() > 1 ) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单已开始或已结束");
        }

        if (accepted) {
            order.setEnumStatus(Order.EnumStatus.INVITATION_ACCEPTED);
            orderService.save(order);
            Technician mainTech = technicianService.get(order.getMainTechId());
            HashMap<String, Object> map = new HashMap<>();
            map.put("action", "INVITATION_ACCEPT");
            map.put("order", order);
            map.put("owner", mainTech);
            map.put("partner", technician);
            boolean result = pushService.pushToSingle(mainTech.getPushId(),
                    technician.getName() + "已接受你的邀请",
                    new ObjectMapper().writeValueAsString(map), 24*3600);
            if (!result) log.info("推送邀请已接受消息失败");
            return new JsonMessage(true);
        } else {
            order.setEnumStatus(Order.EnumStatus.INVITATION_REJECT);
            orderService.save(order);
            return new JsonMessage(true);
        }
    }
}
