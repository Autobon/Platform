package com.autobon.platform.controller.technician.order;

import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.WorkItem;
import com.autobon.order.service.CommentService;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.OrderService;
import com.autobon.order.service.WorkItemService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by yuh on 2016/2/22.
 */
@RestController
@RequestMapping("/api/mobile/technician/order")
public class OrderController {
    @Autowired OrderService orderService;
    @Autowired TechnicianService technicianService;
    @Autowired ConstructionService constructionService;
    @Autowired WorkItemService workItemService;
    @Autowired CommentService commentService;

    // 获取已完成的主要责任人订单列表
    @RequestMapping(value = "/listMain", method = RequestMethod.GET)
    public JsonMessage listMain(HttpServletRequest request,
             @RequestParam(value = "page", defaultValue = "1") int page,
             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician technician = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(orderService.findFinishedOrderByMainTechId(technician.getId(), page, pageSize)));
    }

    // 获取已完成的次要责任人订单列表
    @RequestMapping(value = "/listSecond", method = RequestMethod.GET)
    public JsonMessage listSecond(HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician technician = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(orderService.findFinishedOrderBySecondTechId(technician.getId(), page, pageSize)));
    }

    // 获取未完成的订单
    @RequestMapping(value = "/listUnfinished", method = RequestMethod.GET)
    public JsonMessage listUnfinished(HttpServletRequest request,
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician technician = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(orderService.findUnFinishedOrderByTechId(technician.getId(), page, pageSize)));
    }

    // 获取订单信息
    @RequestMapping(value = "/{orderId:[\\d]+}", method = RequestMethod.GET)
    public JsonMessage show(HttpServletRequest request,
            @PathVariable("orderId") int orderId) {
        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.get(orderId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("mainTech", order.getMainTechId() > 0 ? technicianService.get(order.getMainTechId()) : null);
        map.put("secondTech", order.getSecondTechId() > 0 ? technicianService.get(order.getSecondTechId()) : null);
        map.put("construction", order.getStatusCode() >= Order.Status.IN_PROGRESS.getStatusCode() ?
                    constructionService.getByTechIdAndOrderId(tech.getId(), order.getId()) : null);
        map.put("comment", order.getStatusCode() >= Order.Status.COMMENTED.getStatusCode() ?
                    commentService.getByOrderIdAndTechId(order.getId(), tech.getId()) : null);
        return new JsonMessage(true, "", "", map);
    }

    // 抢单
    @RequestMapping(value = "/takeup", method = RequestMethod.POST)
    public JsonMessage takeUpOrder(HttpServletRequest request,
            @RequestParam("orderId") int orderId) {
        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.get(orderId);
        if (order == null) {
            return new JsonMessage(false, "NO_SUCH_ORDER", "没有这个订单");
        } else if (tech.getStatus() != Technician.Status.VERIFIED) {
            return new JsonMessage(false, "NOT_VERIFIED", "你没有通过认证, 不能抢单");
        } else if (order.getStatus() == Order.Status.CANCELED) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单已取消");
        } else if (order.getStatus() != Order.Status.NEWLY_CREATED) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "已有人接单");
        } else if (tech.getSkill() == null || !Arrays.stream(tech.getSkill().split(",")).anyMatch(i -> i.equals("" + order.getOrderType()))) {
            String orderType = workItemService.getOrderTypes().stream()
                    .filter(t -> t.getOrderType() == order.getOrderType())
                    .findFirst().orElse(new WorkItem()).getOrderTypeName();
            return new JsonMessage(false, "TECH_SKILL_NOT_SUFFICIANT", "你当前的认证技能没有" + orderType);
        }

        order.setMainTechId(tech.getId());
        order.setStatus(Order.Status.TAKEN_UP);
        orderService.save(order);
        return new JsonMessage(true, "", "", order);
    }



    // TODO 继续清理下面的代码



    @RequestMapping(value="/mobile/order/getWorkList",method = RequestMethod.POST)
    public JsonMessage getWorkList(@RequestParam("orderId") int orderId){
        JsonMessage jsonMessage = new JsonMessage(true, "getWorkList");
        Order order = orderService.get(orderId);
        int orderType = order.getOrderType();
        Technician technician = (Technician) SecurityContextHolder.getContext().getAuthentication().getDetails();
        int technicianId = technician.getId();
        int mainTechId = order.getMainTechId();
        int secondTechId = order.getSecondTechId();
        if (technicianId == mainTechId) {
            //查询工作项列表
            List workList = workItemService.findByOrderType(orderType);
            jsonMessage.setData(workList);
        }
        if(technicianId == secondTechId){
            Map<String,Object> dataMap = new HashMap<>();
            //查询工作项列表，主技师已选列表
            Construction cons = constructionService.getByTechIdAndOrderId(mainTechId, orderId);
            if(cons != null){
                String workLoad = cons.getWorkItems();
                String[] workArray = workLoad.split(",");
                dataMap.put("mainTech",workArray);

                List workList = workItemService.findByOrderType(orderType);
                dataMap.put("workList",workList);
                jsonMessage.setData(dataMap);
            }else{
                return  new JsonMessage(false,"主技师还没有开始施工");
            }

        }
        return jsonMessage;
    }


    @RequestMapping(value = "/mobile/order/completeConstruction", method = RequestMethod.POST)
    private JsonMessage completeWork(@RequestParam("orderId") int orderId,
                                     @RequestParam("carType") int carType,
                                     @RequestParam(value = "workItems", required = false) String[] workItems,
                                     @RequestParam(value = "percent", required = false) Float percent) {
        JsonMessage jsonMessage = new JsonMessage(true, "completeWork");
        Order order = orderService.get(orderId);
//        Construction construction = constructionService.findById(constructionId);
//        int orderType = order.getOrderType();
//        Technician technician = (Technician) SecurityContextHolder.getContext().getAuthentication().getDetails();
//        int technicianId = technician.getId();
//        int mainTechId = order.getMainTechId();
//        int secondTechId = order.getSecondTechId();
//        if (technicianId == mainTechId) {
//            this.saveWork(orderType, construction, workArray, carType, workSize);
//
//        } else if (technicianId == secondTechId) {
//            //先要查询主技师是否提交
//            List<Construction> constructionList = constructionService.findByOrderIdAndTechnicianId(orderId, mainTechId);
//            if (constructionList.size() == 1) {
//                Date endTime = constructionList.get(0).getEndTime();
//                if(endTime == null){
//                    return new JsonMessage(false, "等待主技术提交完成");
//                }
//                this.saveWork(orderType, construction, workArray, carType, workSize);
//            }else{
//                return  new JsonMessage(false,"主技师还没有开始施工");
//            }
//
//        } else{
//            return new JsonMessage(false,"你没有此订单");
//        }
        return jsonMessage;
    }

    private void saveWork(int orderType, Construction construction, String[] workArray, int carType, int workSize) {
//        if (orderType == 1 || orderType == 2 || orderType == 3) {
//            //隔热膜
//            String workload = arrayUtil.arrayToString(workArray);
//            construction.setCarType(carType);
//            construction.setWorkload(workload);
//            construction.setEndTime(new Date());
//            constructionService.save(construction);
//
//        } else if (orderType == 4) {
//            construction.setWorkSize(workSize);
//            construction.setEndTime(new Date());
//            constructionService.save(construction);
//        }
    }
}
