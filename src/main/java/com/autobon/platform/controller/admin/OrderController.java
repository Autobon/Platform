package com.autobon.platform.controller.admin;

import com.autobon.getui.PushService;
import com.autobon.order.entity.Comment;
import com.autobon.order.entity.DetailedOrder;
import com.autobon.order.entity.Order;
import com.autobon.order.service.CommentService;
import com.autobon.order.service.DetailedOrderService;
import com.autobon.order.service.OrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.VerifyCode;
import com.autobon.staff.entity.Staff;
import com.autobon.staff.service.StaffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController("adminOrderController")
@RequestMapping("/api/web/admin/order")
public class OrderController {
    private static Logger log = LoggerFactory.getLogger(OrderController.class);
    @Value("${com.autobon.gm-path}") String gmPath;
    @Autowired OrderService orderService;
    @Autowired DetailedOrderService detailedOrderService;
    @Autowired @Qualifier("PushServiceA")
    PushService pushServiceA;
    @Autowired CommentService commentService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search(
            @RequestParam(value = "orderNum", required = false) String orderNum,
            @RequestParam(value = "orderCreator", required = false) String orderCreator,
            @RequestParam(value = "orderType", required = false) Integer orderType,
            @RequestParam(value = "orderStatus", required = false) String orderStatus,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        String creatorName = null;
        String contactPhone = null;
        Integer statusCode = null;

        if (orderCreator != null) {
            if (Pattern.matches("[\\d\\-]+", orderCreator)) {
                contactPhone = orderCreator;
            } else {
                creatorName = orderCreator;
            }
        }

        if (orderStatus != null) {
            try {
                Order.Status s = Order.Status.valueOf(orderStatus);
                statusCode = s.getStatusCode();
            } catch (Exception e) {}
        }

        return new JsonMessage(true, "", "",
                new JsonPage<>(orderService.find(orderNum, creatorName, contactPhone,
                        orderType, statusCode, page, pageSize)));
    }

    @RequestMapping(value = "/{orderNum:\\d+.*}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("orderNum") String orderNum) {
        DetailedOrder order = detailedOrderService.getByOrderNum(orderNum);
        if (order != null) return new JsonMessage(true, "", "", order);
        else return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个订单");
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonMessage createOrder(HttpServletRequest request,
            @RequestParam("orderType")   int orderType,
            @RequestParam("orderTime")   String orderTime,
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
        order.setOrderTime(Date.from(
                LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse(orderTime))
                .atZone(ZoneId.systemDefault()).toInstant()));
        order.setRemark(remark);
        order.setPhoto(photo);
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

    @RequestMapping(value = "/photo", method = RequestMethod.POST)
    public JsonMessage uploadPhoto(HttpServletRequest request,
            @RequestParam("file") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");

        String path = "/uploads/order";
        File dir = new File(request.getServletContext().getRealPath(path));
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

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public JsonMessage comment(HttpServletRequest request,
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
        return new JsonMessage(true, "", "", comment);
    }
}
