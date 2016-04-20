package com.autobon.platform.controller.admin;

import com.autobon.getui.PushService;
import com.autobon.order.entity.Comment;
import com.autobon.order.entity.DetailedOrder;
import com.autobon.order.entity.Order;
import com.autobon.order.service.CommentService;
import com.autobon.order.service.DetailedOrderService;
import com.autobon.order.service.OrderService;
import com.autobon.platform.listener.Event;
import com.autobon.platform.listener.OrderEventListener;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.VerifyCode;
import com.autobon.staff.entity.Staff;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechStatService;
import com.autobon.technician.service.TechnicianService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController("adminOrderController")
@RequestMapping("/api/web/admin/order")
public class OrderController {
    private static Logger log = LoggerFactory.getLogger(OrderController.class);
    @Value("${com.autobon.gm-path}") String gmPath;
    @Value("${com.autobon.uploadPath}") String uploadPath;
    @Autowired OrderService orderService;
    @Autowired CommentService commentService;
    @Autowired TechnicianService technicianService;
    @Autowired TechStatService techStatService;
    @Autowired DetailedOrderService detailedOrderService;
    @Autowired ApplicationEventPublisher publisher;
    @Autowired @Qualifier("PushServiceA") PushService pushServiceA;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search(
            @RequestParam(value = "orderNum", required = false) String orderNum,
            @RequestParam(value = "orderCreator", required = false) String orderCreator,
            @RequestParam(value = "orderType", required = false) Integer orderType,
            @RequestParam(value = "orderStatus", required = false) Order.Status orderStatus,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        String creatorName = null;
        String contactPhone = null;
        List<Integer> types = null;
        Integer statusCode = orderStatus.getStatusCode();

        if (!"orderTime".equals(sort) && "id".equals(sort)) return new JsonMessage(false, "ILLEGAL_SORT_PARAM" , "sort参数只能为id或orderTime");

        if (orderCreator != null) {
            if (Pattern.matches("\\d{11}", orderCreator)) {
                contactPhone = orderCreator;
            } else {
                creatorName = orderCreator;
            }
        }

        if (orderType != null) {
            types = Arrays.asList(orderType);
        }

        return new JsonMessage(true, "", "",
                new JsonPage<>(orderService.find(orderNum, creatorName, contactPhone,
                        types, statusCode, sort, page, pageSize)));
    }

    @RequestMapping(value = "/{orderNum:\\d+.*}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("orderNum") String orderNum) {
        DetailedOrder order = detailedOrderService.getByOrderNum(orderNum);
        if (order != null) return new JsonMessage(true, "", "", order);
        else return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个订单");
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonMessage createOrder(
            HttpServletRequest request,
            @RequestParam("orderType") int orderType,
            @RequestParam("orderTime") String orderTime,
            @RequestParam("positionLon") String positionLon,
            @RequestParam("positionLat") String positionLat,
            @RequestParam("contactPhone") String contactPhone,
            @RequestParam("contact") String contact,
            @RequestParam("photo") String photo,
            @RequestParam(value = "remark", required = false) String remark) throws Exception {
        Staff staff = (Staff) request.getAttribute("user");
        if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", orderTime))
            return new JsonMessage(false, "ILLEGAL_PARAM", "订单时间格式不对, 正确格式: 2016-02-10 09:23");

        Order order = new Order();
        order.setCreatorType(2);
        order.setCreatorId(staff.getId());
        order.setCreatorName(contact);
        order.setContactPhone(contactPhone);
        order.setPositionLon(positionLon);
        order.setPositionLat(positionLat);
        order.setOrderType(orderType);
        order.setOrderTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(orderTime));
        order.setRemark(remark);
        order.setPhoto(photo);
        orderService.save(order);

        publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.CREATED));
        return new JsonMessage(true, "", "", order);
    }

    @RequestMapping(value = "/photo", method = RequestMethod.POST)
    public JsonMessage uploadPhoto(HttpServletRequest request,
                                   @RequestParam("file") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");

        String path = "/uploads/order";
        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
        if (!dir.exists()) dir.mkdirs();

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + VerifyCode.generateVerifyCode(6) + extension;

        InputStream in;
        if (file == null || file.isEmpty()) return new JsonMessage(false, "没有选择上传文件");
        in = file.getInputStream();

        ConvertCmd cmd = new ConvertCmd(true);
        cmd.setSearchPath(gmPath);
        cmd.setInputProvider(new Pipe(in, null));
        IMOperation operation = new IMOperation();
        operation.addImage("-");
        operation.resize(1200, 1200, ">");
        operation.addImage(dir.getAbsolutePath() + File.separator + filename);
        cmd.run(operation);

        return new JsonMessage(true, "", "", path + "/" + filename);
    }

    @RequestMapping(value = "/assign", method = RequestMethod.POST)
    public JsonMessage assign(
            @RequestParam("orderId") int orderId,
            @RequestParam("techId") int techId) throws IOException {
        Order order = orderService.get(orderId);
        if (order == null) return new JsonMessage(false, "NO_SUCH_ORDER", "订单不存在");
        if (order.getStatus() != Order.Status.NEWLY_CREATED) return new JsonMessage(false, "UNASSIGNABLE_ORDER", "订单已有人接单或已取消");

        Technician tech = technicianService.get(techId);
        if (tech == null) return new JsonMessage(false, "NO_SUCH_TECH", "技师不存在");
        if (tech.getStatus() != Technician.Status.VERIFIED) return new JsonMessage(false, "TECH_NOT_VERIFIED", "技师未认证,不可接单");
        if (tech.getSkill() == null || !Arrays.stream(tech.getSkill().split(",")).anyMatch(i -> i.equals("" + order.getOrderType())))
            return new JsonMessage(false, "TECH_SKILL_NOT_SUFFICIANT", tech.getName() + "没有此订单类型的技能认证");

        order.setMainTechId(techId);
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

        // 推送派单消息
        String msgTitle = "你收到派单消息";
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "ASSIGN_ORDER");
        map.put("order", order);
        map.put("title", msgTitle);
        boolean result = pushServiceA.pushToSingle(tech.getPushId(), msgTitle, new ObjectMapper().writeValueAsString(map), 72*3600);
        if (!result) log.error("订单: " + order.getOrderNum() + "的派单消息发送失败");
        return new JsonMessage(true, "", "", order);
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public JsonMessage comment(
            @RequestParam("orderId") int orderId,
            @RequestParam("star") int star,
            @RequestParam("advice") String advice,
            @RequestParam(value = "arriveOnTime", defaultValue = "false") boolean arriveOnTime,
            @RequestParam(value = "completeOnTime", defaultValue = "false") boolean completeOnTime,
            @RequestParam(value = "professional", defaultValue = "false") boolean professional,
            @RequestParam(value = "dressNeatly", defaultValue = "false") boolean dressNeatly,
            @RequestParam(value = "carProtect", defaultValue = "false") boolean carProtect,
            @RequestParam(value = "goodAttitude", defaultValue = "false") boolean goodAttitude) {
        Order order = orderService.get(orderId);
        if (order.getCreatorType() != 2) {
            return new JsonMessage(false, "NOT_PLATFORM_ORDER", "非平台下单,不可评论");
        } else if (order.getStatus() != Order.Status.FINISHED) {
            if (order.getStatusCode() < Order.Status.FINISHED.getStatusCode()) {
                return new JsonMessage(false, "NOT_FINISHED_ORDER", "订单尚未完成");
            } else if (order.getStatus() == Order.Status.COMMENTED) {
                return new JsonMessage(false, "COMMENTED_ORDER", "订单已评论");
            } else {
                return new JsonMessage(false, "NOT_COMMENTABLE", "订单不可评论, 订单未取消或已超时");
            }
        }

        Comment comment = new Comment();
        comment.setTechId(order.getMainTechId());
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
        TechStat techStat = techStatService.getByTechId(order.getMainTechId());
        if (techStat == null) {
            techStat = new TechStat();
            techStat.setTechId(order.getMainTechId());
        }
        int commentCount = commentService.countByTechId(order.getMainTechId());
        float starRate = commentService.calcStarRateByTechId(order.getMainTechId(),
                Date.from(LocalDate.now().minusMonths(12).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        if (commentCount < 100) starRate = ((100 - commentCount) * 3f + commentCount * starRate) / 100f;
        techStat.setStarRate(starRate);
        techStatService.save(techStat);

        return new JsonMessage(true, "", "", comment);
    }
}
