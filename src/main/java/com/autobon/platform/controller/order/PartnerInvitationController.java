package com.autobon.platform.controller.order;

import com.autobon.getui.PushService;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.PartnerInvitation;
import com.autobon.order.service.PartnerInvitationService;
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
 * 订单邀请合伙人相关操作
 */
@RestController
@RequestMapping("/api/mobile/technician/order")
public class PartnerInvitationController {
    private static Logger log = LoggerFactory.getLogger(PartnerInvitationController.class);

    @Autowired private TechnicianService technicianService;
    @Autowired private OrderService orderService;
    @Autowired private PartnerInvitationService invitationService;
    @Autowired private PushService pushService;

    /**
     * 发起合伙邀请
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
        } else if (order.getMainTechId() == technician.getId()) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "主技师和合作技师不能为同一人");
        } else if (partner == null) {
            return new JsonMessage(false, "ILLEGAL_PARAMS", "系统中没有指定的技师");
        }

        PartnerInvitation invitation = new PartnerInvitation();
        invitation.setOrder(order);
        invitation.setMainTech(technician);
        invitation.setInvitedTech(partner);
        invitationService.save(invitation);
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "INVITE_PARTNER");
        map.put("invitation", invitation);
        boolean result = pushService.pushToSingle(partner.getPushId(),
                technician.getName() + "向你发起订单合作邀请",
                new ObjectMapper().writeValueAsString(map), 2*3600);

        if (!result) log.info("推送邀请消息失败");
        return new JsonMessage(true);
    }

    /**
     * 接受或拒绝邀请
     * @param request
     * @param accepted
     * @param invitationId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/invitation/{invitationId}", method = RequestMethod.POST)
    public JsonMessage acceptInvitation(HttpServletRequest request,
            @RequestParam("accepted") boolean accepted,
            @PathVariable("invitationId") int invitationId) throws IOException {
        Technician technician = (Technician) request.getAttribute("user");
        PartnerInvitation invitation = invitationService.get(invitationId);

        if (invitation == null || invitation.getInvitedTech().getId() != technician.getId()) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "你没有这个邀请");
        } else if (invitation.getStatus() == PartnerInvitation.Status.ACCEPTED) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已接受邀请");
        } else if (invitation.getStatus() == PartnerInvitation.Status.REJECTED) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已拒绝邀请");
        } else if (invitation.getStatus() == PartnerInvitation.Status.EXPIRED) {
            return new JsonMessage(false, "EXPIRED_OPERATION", "已有其它人接单或邀请已失效");
        }

        if (accepted) {
            invitationService.expireOrderInvitaions(invitation.getOrder().getId());
            invitation.setStats(PartnerInvitation.Status.ACCEPTED);
            invitationService.save(invitation);
            HashMap<String, Object> map = new HashMap<>();
            map.put("action", "INVITATION_ACCEPT");
            map.put("invitation", invitation);
            boolean result = pushService.pushToSingle(invitation.getMainTech().getPushId(),
                    technician.getName() + "已接受你的邀请",
                    new ObjectMapper().writeValueAsString(map), 2*3600);
            if (!result) log.info("推送邀请已接受消息失败");
            return new JsonMessage(true);
        } else {
            invitation.setStats(PartnerInvitation.Status.ACCEPTED);
            invitationService.save(invitation);
            return new JsonMessage(true);
        }
    }
}
