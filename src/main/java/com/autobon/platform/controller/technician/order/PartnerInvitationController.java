package com.autobon.platform.controller.technician.order;

import com.autobon.getui.PushService;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.WorkItem;
import com.autobon.order.service.OrderService;
import com.autobon.order.service.WorkItemService;
import com.autobon.shared.JsonMessage;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.DetailedTechnicianService;
import com.autobon.technician.service.TechStatService;
import com.autobon.technician.service.TechnicianService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
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
    @Autowired private WorkItemService workItemService;
    @Autowired private TechStatService techStatService;
    @Autowired private DetailedTechnicianService detailedTechnicianService;
    @Autowired @Qualifier("PushServiceA")
    private PushService pushService;

    /**
     * 发起合作邀请
     * @param request
     * @param partnerId
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/{orderId:\\d+}/invite/{partnerId:\\d+}", method = RequestMethod.POST)
    public JsonMessage invitePartner(HttpServletRequest request,
            @PathVariable("partnerId") int partnerId,
            @PathVariable("orderId")   int orderId) throws IOException {
        Technician technician = (Technician) request.getAttribute("user");
        Technician partner = technicianService.get(partnerId);
        Order order = orderService.get(orderId);

        if (order.getStatusCode() >= Order.Status.IN_PROGRESS.getStatusCode() ) {
            return new JsonMessage(false, "ORDER_STARTED", "订单已开始或结束");
        } else if (order.getStatus() == Order.Status.INVITATION_ACCEPTED) {
            return new JsonMessage(false, "INVITATION_ACCEPTED", "订单已有人接受合作邀请");
        } else if (partner == null) {
            return new JsonMessage(false, "NO_SUCH_TECH", "系统中没有邀请的技师");
        } else if (order.getMainTechId() == partner.getId()) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "主技师和合作技师不能为同一人");
        } else if (partner.getStatus() != Technician.Status.VERIFIED) {
            return new JsonMessage(false, "NOT_VERIFIED", "受邀技师没有通过认证");
        } else if (partner.getSkill() == null ||
                !Arrays.stream(partner.getSkill().split(",")).anyMatch(i -> i.equals("" + order.getOrderType()))) {
            String orderType = workItemService.getOrderTypes().stream()
                    .filter(t -> t.getOrderType() == order.getOrderType())
                    .findFirst().orElse(new WorkItem()).getOrderTypeName();
            return new JsonMessage(false, "TECH_SKILL_NOT_SUFFICIANT", partner.getName() + "的认证技能没有" + orderType);
        }

        order.setSecondTechId(partnerId);
        order.setStatus(Order.Status.SEND_INVITATION);
        orderService.save(order);

        HashMap<String, Object> map = new HashMap<>();
        String title = technician.getName() + "向你发起订单合作邀请";
        map.put("action", "INVITE_PARTNER");
        map.put("title", title);
        map.put("order", order.getId());
        map.put("owner", detailedTechnicianService.get(technician.getId()));
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
    @RequestMapping(value = "/{orderId:\\d+}/invitation", method = RequestMethod.POST)
    public JsonMessage acceptInvitation(HttpServletRequest request,
            @RequestParam("accepted") boolean accepted,
            @PathVariable("orderId") int orderId) throws IOException {
        Technician technician = (Technician) request.getAttribute("user");
        Order order = orderService.get(orderId);

        if (order == null || order.getSecondTechId() != technician.getId()) {
            return new JsonMessage(false, "NO_SUCH_INVITATION", "无效操作: 订单已改邀他人或订单已强制开始");
        } else if (order.getStatus() == Order.Status.INVITATION_ACCEPTED) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已接受邀请");
        } else if (order.getStatus() == Order.Status.INVITATION_REJECTED) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已拒绝邀请");
        } else if (order.getStatusCode() > Order.Status.IN_PROGRESS.getStatusCode() ) {
            return new JsonMessage(false, "ORDER_FINISHED", "订单已结束");
        }

        Technician mainTech = technicianService.get(order.getMainTechId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("owner", mainTech);
        map.put("partner", technician);
        if (accepted) {
            order.setStatus(Order.Status.INVITATION_ACCEPTED);
            orderService.save(order);
            // 更新订单总数
            TechStat stat = techStatService.getByTechId(technician.getId());
            if (stat == null) {
                stat = new TechStat();
                stat.setTechId(technician.getId());
            }
            stat.setTotalOrders(stat.getTotalOrders() + 1);
            techStatService.save(stat);

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
