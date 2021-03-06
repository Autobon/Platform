package com.autobon.platform.controller.coop;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.order.entity.Comment;
import com.autobon.order.entity.Order;
import com.autobon.order.service.CommentService;
import com.autobon.order.service.DetailedOrderService;
import com.autobon.order.service.OrderService;
import com.autobon.platform.listener.Event;
import com.autobon.platform.listener.OrderEventListener;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.VerifyCode;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechStatService;
import com.autobon.technician.service.TechnicianService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/3.
 */
@RestController("coopOrderController")
@RequestMapping("/api/mobile/coop/order")
public class OrderController {
    private static Logger log = LoggerFactory.getLogger(OrderController.class);
    @Value("${com.autobon.uploadPath}") String uploadPath;
    @Autowired OrderService orderService;
    @Autowired CommentService commentService;
    @Autowired TechStatService techStatService;
    @Autowired DetailedOrderService detailedOrderService;
    @Autowired CooperatorService cooperatorService;
    @Autowired ApplicationEventPublisher publisher;
    @Autowired TechnicianService technicianService;

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public JsonMessage comment(@RequestParam("orderId") int orderId,
            @RequestParam("star") int star,
            @RequestParam(value = "arriveOnTime", defaultValue = "false") boolean arriveOnTime,
            @RequestParam(value = "completeOnTime", defaultValue = "false") boolean completeOnTime,
            @RequestParam(value = "professional", defaultValue = "false") boolean professional,
            @RequestParam(value = "dressNeatly", defaultValue = "false") boolean dressNeatly,
            @RequestParam(value = "carProtect", defaultValue = "false") boolean carProtect,
            @RequestParam(value = "goodAttitude", defaultValue = "false") boolean goodAttitude,
            @RequestParam("advice") String advice) {

        JsonMessage jsonMessage = new JsonMessage(true, "comment");
        Order order = orderService.get(orderId);
        if (order.getStatus() == Order.Status.FINISHED) {

            int mainTechId = order.getMainTechId();
            if (mainTechId == 0) {
                return new JsonMessage(false, "此订单未指定技师");
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
            int commentCount = commentService.countByTechId(mainTechId);
            float starRate = commentService.calcStarRateByTechId(mainTechId,
                    Date.from(LocalDate.now().minusMonths(12).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            if (commentCount < 100) starRate = ((100 - commentCount) * 3f + commentCount * starRate) / 100f;
            mainStat.setStarRate(starRate);
            techStatService.save(mainStat);

            return jsonMessage;

        } else {
            return new JsonMessage(false, "订单未完成或已评论");
        }
    }

    @RequestMapping(value="/createOrder",method = RequestMethod.POST)
    public JsonMessage createOrder(HttpServletRequest request,
            @RequestParam("photo") String photo,
            @RequestParam("remark") String remark,
            @RequestParam("orderTime") String orderTime,
            @RequestParam("orderType") int orderType,
            @RequestParam(value = "pushToAll", defaultValue = "true") boolean pushToAll) throws Exception {

        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int coopId = coopAccount.getCooperatorId();
        Cooperator cooperator = cooperatorService.get(coopId);
        int statuCode = cooperator.getStatusCode();
        if(statuCode !=1) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "商户未通过验证");
        }

        if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", orderTime))
            return new JsonMessage(false, "ILLEGAL_PARAM", "订单时间格式不对, 正确格式: 2016-02-10 09:23");

        Order order = new Order();
        order.setCreatorId(coopAccount.getId());
        order.setCoopId(coopId);
        order.setCreatorName(coopAccount.getShortname());
        order.setPhoto(photo);
        order.setRemark(remark);
        order.setOrderTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(orderTime));
        order.setOrderType(orderType);
        order.setPositionLon(cooperator.getLongitude());
        order.setPositionLat(cooperator.getLatitude());
        order.setContactPhone(coopAccount.getPhone());
        if (!pushToAll) order.setStatus(Order.Status.CREATED_TO_APPOINT);
        orderService.save(order);
        cooperator.setOrderNum(cooperator.getOrderNum() + 1);
        cooperatorService.save(cooperator);

        publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.CREATED));
        return new JsonMessage(true, "", "", order);
    }

    @RequestMapping(value = "/appoint", method = RequestMethod.POST)
    public JsonMessage appointOrder(@RequestParam("orderId") int orderId, @RequestParam("techId") int techId) {
        Order order = orderService.get(orderId);
        Technician tech = technicianService.get(techId);
        if (order.getStatus() != Order.Status.CREATED_TO_APPOINT)
            return new JsonMessage(false, "NOT_ASSIGNABLE_ORDER", "订单不可指定技师");
        if (tech.getSkill() == null || !Arrays.stream(tech.getSkill().split(",")).anyMatch(i -> i.equals("" + order.getOrderType())))
            return new JsonMessage(false, "TECH_SKILL_NOT_SUFFICIANT", "技师技能不支持订单类型");

        order.setMainTechId(techId);
        order.setTakenTime(new Date());
        order.setStatus(Order.Status.TAKEN_UP);
        orderService.save(order);
        publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.APPOINTED));
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/{orderId:\\d+}/cancel", method = RequestMethod.POST)
    public JsonMessage cancelOrder(HttpServletRequest request, @PathVariable("orderId") int orderId) {
        CoopAccount account = (CoopAccount) request.getAttribute("user");
        Order order = orderService.get(orderId);
        if (order == null || order.getCreatorId() != account.getId() || order.getCoopId() != account.getCooperatorId()) {
            return new JsonMessage(false, "NO_SUCH_RECORD", "你没有此定单");
        }

//        if (order.getMainTechId() == 0 || new Date(order.getAddTime().getTime() + 30*60*1000).after(new Date())
//                || new Date().before(new Date(order.getOrderType() - 2*3600*1000))) {
        if (order.getStatusCode() < Order.Status.SIGNED_IN.getStatusCode()) {
            order.setStatus(Order.Status.CANCELED);
            orderService.save(order);
            publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.CANCELED));
            return new JsonMessage(true);
        } else {
            return new JsonMessage(false, "OFFEND_ORDER_CANCEL_RULE", "已开始或结束施工订单, 不能撤销");
        }
    }

    @RequestMapping(value = "/uploadPhoto",method = RequestMethod.POST)
    public JsonMessage uploadPhoto(@RequestParam("file") MultipartFile file) throws  Exception{
        String path ="/uploads/order/photo";
        if (file == null || file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");
        JsonMessage msg = new JsonMessage(true);
        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;

        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        msg.setData(path + "/" + filename);
        return msg;
    }

    @RequestMapping(value="/listUnfinished",method = RequestMethod.POST)
    public JsonMessage listUnfinished(HttpServletRequest request,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        Boolean isMain = coopAccount.isMain();
        if(isMain){
            return new JsonMessage(true, "", "",
                    new JsonPage<>(detailedOrderService.findUnfinishedByCoopId(coopAccount.getCooperatorId(), page, pageSize)));
        }else {
            return new JsonMessage(true, "", "",
                    new JsonPage<>(detailedOrderService.findUnfinishedByCoopAccountId(coopAccount.getId(), page, pageSize)));
        }
    }

    @RequestMapping(value="/listFinished",method = RequestMethod.POST)
    public JsonMessage listFinished(HttpServletRequest request,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        Boolean isMain = coopAccount.isMain();
        if(isMain){
            return new JsonMessage(true, "", "",
                    new JsonPage<>(detailedOrderService.findFinishedByCoopId(coopAccount.getCooperatorId(), page, pageSize)));
        }else {
            return new JsonMessage(true, "", "",
                    new JsonPage<>(detailedOrderService.findFinishedByCoopAccountId(coopAccount.getId(), page, pageSize)));
        }
    }

    @RequestMapping(value="/listUncomment",method = RequestMethod.POST)
    public JsonMessage listUncomment(HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        Boolean isMain = coopAccount.isMain();
        if(isMain){
            return new JsonMessage(true, "", "",
                    new JsonPage<>(detailedOrderService.findUncommentByCoopId(coopAccount.getCooperatorId(), page, pageSize)));
        }else {
            return new JsonMessage(true, "", "",
                    new JsonPage<>(detailedOrderService.findUncommentByCoopAccountId(coopAccount.getId(), page, pageSize)));
        }
    }

    @RequestMapping(value="/orderCount",method = RequestMethod.POST)
    public JsonMessage orderCount(HttpServletRequest request){
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int coopId = coopAccount.getCooperatorId();
        int coopAccountId = coopAccount.getId();
        boolean isMain = coopAccount.isMain();

        if(isMain){
            Cooperator cooperator = cooperatorService.get(coopId);
            int orderNum = cooperator.getOrderNum();
            return new JsonMessage(true,"","",orderNum);
        }else{
            return new JsonMessage(true, "", "", orderService.countOfCoopAccount(coopAccountId));
        }

    }
}
