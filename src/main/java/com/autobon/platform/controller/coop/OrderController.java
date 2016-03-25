package com.autobon.platform.controller.coop;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.getui.PushService;
import com.autobon.order.entity.Comment;
import com.autobon.order.entity.Order;
import com.autobon.order.service.DetailedOrderService;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.TechStat;
import com.autobon.order.service.CommentService;
import com.autobon.order.service.OrderService;
import com.autobon.technician.service.TechStatService;
import com.autobon.shared.JsonMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/3.
 */
@RestController("coopOrderController")
@RequestMapping("/api/mobile/coop/order")
public class OrderController {
    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired @Qualifier("PushServiceA")
    PushService pushServiceA;

    @Autowired OrderService orderService;
    @Autowired CommentService commentService;
    @Autowired TechStatService techStatService;
    @Autowired
    DetailedOrderService detailedOrderService;
    @Autowired
    private CooperatorService cooperatorService;

    @PersistenceContext
    private EntityManager entityManager;


    @RequestMapping(value="/comment",method = RequestMethod.POST)
    public JsonMessage comment(@RequestParam("orderId") int orderId,
                               @RequestParam("star") int star,
                               @RequestParam(value = "arriveOnTime",defaultValue = "false") boolean arriveOnTime,
                               @RequestParam(value = "completeOnTime",defaultValue = "false") boolean completeOnTime,
                               @RequestParam(value = "professional",defaultValue = "false") boolean professional,
                               @RequestParam(value = "dressNeatly",defaultValue = "false") boolean dressNeatly,
                               @RequestParam(value = "carProtect",defaultValue = "false") boolean carProtect,
                               @RequestParam(value = "goodAttitude",defaultValue = "false") boolean goodAttitude,
                               @RequestParam("advice") String advice){

        JsonMessage jsonMessage = new JsonMessage(true,"comment");
        Order order = orderService.get(orderId);
        if(order.getStatus() == Order.Status.FINISHED){

        int mainTechId = order.getMainTechId();
        int secondTechId = order.getSecondTechId();
        if(mainTechId == 0){
            return new JsonMessage(false,"此订单未指定技师");
        }

        Comment comment = new Comment();
        comment.setTechId(mainTechId);
        comment.setOrderId(orderId);
        comment.setStar(star);
        comment.setArriveOnTime(arriveOnTime);
        comment.setCompleteOnTime(completeOnTime);
        comment.setProfessional(professional);
        comment.setDressNeatly(dressNeatly);
        comment.setCarProtect(carProtect);
        comment.setGoodAttitude(goodAttitude);
        comment.setAdvice(advice);
        commentService.save(comment);

        order.setStatus(Order.Status.COMMENTED);
        orderService.save(order);

        // 写入技师星级统计
        TechStat mainStat = techStatService.getByTechId(mainTechId);
        if (mainStat == null) {
            mainStat = new TechStat();
            mainStat.setTechId(mainTechId);
        }
        mainStat.setStarRate(commentService.calcStarRateByTechId(mainTechId,
                Date.from(LocalDate.now().minusMonths(12).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
        techStatService.save(mainStat);

        //根据原形只对主责任人进行评价
/*        if(secondTechId != 0){
            entityManager.detach(comment);
            comment.setId(0);
            comment.setTechId(secondTechId);
            commentService.save(comment);

            // 写入技师星级统计
            TechStat secondStat = techStatService.getByTechId(secondTechId);
            if (secondStat == null) {
                secondStat = new TechStat();
                secondStat.setTechId(secondTechId);
            }
            secondStat.setStarRate(commentService.calcStarRateByTechId(secondTechId,
                    Date.from(LocalDate.now().minusMonths(12).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
            techStatService.save(secondStat);
        }*/

        return jsonMessage;

        }else{
            return new JsonMessage(false,"订单未完成或已评论");
        }
    }

    @RequestMapping(value="/createOrder",method = RequestMethod.POST)
    public JsonMessage createOrder(HttpServletRequest request,
                                @RequestParam("photo") String photo,
                                @RequestParam("remark") String remark,
                                @RequestParam("orderTime") String orderTime,
                                @RequestParam("orderType") int orderType) throws Exception {

        //Cooperator cooperator = (Cooperator) request.getAttribute("user");
        //int statuCode = cooperator.getStatusCode();

        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int coopId = coopAccount.getCooperatorId();
        Cooperator cooperator = cooperatorService.get(coopId);
        int statuCode = coopAccount.getStatusCode();

        if(statuCode !=1){
            return new JsonMessage(false, "ILLEGAL_PARAM", "商户未通过验证");
        }

        if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", orderTime))
            return new JsonMessage(false, "ILLEGAL_PARAM", "订单时间格式不对, 正确格式: 2016-02-10 09:23");

        Order order = new Order();
        order.setCreatorType(1);
        order.setCreatorId(coopAccount.getId());
        order.setCreatorName(coopAccount.getName());
        order.setPhoto(photo);
        order.setRemark(remark);
        order.setOrderTime(Date.from(
                LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse(orderTime))
                        .atZone(ZoneId.systemDefault()).toInstant()));
        order.setOrderType(orderType);
        order.setPositionLon(cooperator.getLongitude());
        order.setPositionLat(cooperator.getLatitude());
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

    @RequestMapping(value="/listUnfinished",method = RequestMethod.POST)
    public JsonMessage listUnfinished(HttpServletRequest request,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        //Cooperator cooperator = (Cooperator) request.getAttribute("user");
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(detailedOrderService.findUnfinishedByCoopId(coopAccount.getId(), page, pageSize)));

    }

    @RequestMapping(value="/listFinished",method = RequestMethod.POST)
    public JsonMessage listFinished(HttpServletRequest request,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        //Cooperator cooperator = (Cooperator) request.getAttribute("user");
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(detailedOrderService.findFinishedByCoopId(coopAccount.getId(), page, pageSize)));

    }

    @RequestMapping(value="/listUncomment",method = RequestMethod.POST)
    public JsonMessage listUncomment(HttpServletRequest request,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        //Cooperator cooperator = (Cooperator) request.getAttribute("user");
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                new JsonPage<>(detailedOrderService.findUncommentByCoopId(coopAccount.getId(), page, pageSize)));

    }


}
