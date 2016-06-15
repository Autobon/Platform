package com.autobon.platform.controller.technician.order;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.WorkItem;
import com.autobon.order.service.*;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechStatService;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yuh on 2016/2/22.
 */
@RestController
@RequestMapping("/api/mobile/technician/order")
public class OrderController {
    @Autowired OrderService orderService;
    @Autowired DetailedOrderService detailedOrderService;
    @Autowired TechnicianService technicianService;
    @Autowired ConstructionService constructionService;
    @Autowired WorkItemService workItemService;
    @Autowired CommentService commentService;
    @Autowired TechStatService techStatService;

    // 获取已完成的主要责任人订单列表
    @RequestMapping(value = "/listMain", method = RequestMethod.GET)
    public JsonMessage listMain(
            HttpServletRequest request,
             @RequestParam(value = "page", defaultValue = "1") int page,
             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician technician = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(detailedOrderService.findFinishedByMainTechId(technician.getId(), page, pageSize)));
    }

    // 获取已完成的次要责任人订单列表
    @RequestMapping(value = "/listSecond", method = RequestMethod.GET)
    public JsonMessage listSecond(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician technician = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(detailedOrderService.findFinishedBySecondTechId(technician.getId(), page, pageSize)));
    }

    // 获取未完成的订单
    @RequestMapping(value = "/listUnfinished", method = RequestMethod.GET)
    public JsonMessage listUnfinished(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician technician = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(detailedOrderService.findUnfinishedByTechId(technician.getId(), page, pageSize)));
    }

    // 获取可抢订单列表
    @RequestMapping(value = "/listNew", method = RequestMethod.GET)
    public JsonMessage listNew(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician technician = (Technician) request.getAttribute("user");
        String sSkill = technician.getSkill();
        if (sSkill == null || "".equals(sSkill)) {
            return new JsonMessage(true, "", "", new JsonPage<>());
        }
        List<Integer> skills = Arrays.stream(technician.getSkill().split(",")).map(Integer::parseInt).collect(Collectors.toList());

        return new JsonMessage(true, "", "", new JsonPage<>(orderService.find(null, null, null, skills,
                Order.Status.NEWLY_CREATED.getStatusCode(), "orderTime", Sort.Direction.ASC, page, pageSize)));
    }

    // 获取订单信息
    @RequestMapping(value = "/{orderId:\\d+}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("orderId") int orderId) {
        return new JsonMessage(true, "", "", detailedOrderService.get(orderId));
    }

    // 抢单
    @RequestMapping(value = "/takeup", method = RequestMethod.POST)
    public JsonMessage takeUpOrder(
            HttpServletRequest request,
            @RequestParam("orderId") int orderId) {
        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.get(orderId);
        if (order == null) {
            return new JsonMessage(false, "NO_SUCH_ORDER", "没有这个订单");
        } else if (tech.getStatus() != Technician.Status.VERIFIED) {
            return new JsonMessage(false, "NOT_VERIFIED", "你没有通过认证, 不能抢单");
        } else if (order.getStatus() == Order.Status.CANCELED) {
            return new JsonMessage(false, "ORDER_CANCELED", "订单已取消");
        } else if (order.getStatus() != Order.Status.NEWLY_CREATED) {
            return new JsonMessage(false, "ORDER_TAKEN_UP", "已有人接单");
        } else if (tech.getSkill() == null || !Arrays.stream(tech.getSkill().split(",")).anyMatch(i -> i.equals("" + order.getOrderType()))) {
            String orderType = workItemService.getOrderTypes().stream()
                    .filter(t -> t.getOrderType() == order.getOrderType())
                    .findFirst().orElse(new WorkItem()).getOrderTypeName();
            return new JsonMessage(false, "TECH_SKILL_NOT_SUFFICIANT", "你当前的认证技能没有" + orderType);
        }

        order.setMainTechId(tech.getId());
        order.setStatus(Order.Status.TAKEN_UP);
        orderService.save(order);

        // 更新订单总数
        TechStat stat = techStatService.getByTechId(tech.getId());
        if (stat == null) {
            stat = new TechStat();
            stat.setTechId(tech.getId());
        }
        stat.setTotalOrders(stat.getTotalOrders() + 1);
        techStatService.save(stat);
        return new JsonMessage(true, "", "", order);
    }

}
