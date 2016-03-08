package com.autobon.platform.controller.technician.order;

import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.OrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.VerifyCode;
import com.autobon.technician.entity.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController
@RequestMapping("/api/mobile/technician/construct")
public class ConstructionController {
    @Autowired OrderService orderService;
    @Autowired ConstructionService constructionService;

    // 开始工作
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public JsonMessage startWork(HttpServletRequest request,
            @RequestParam("orderId") int orderId,
            @RequestParam(value = "ignoreInvitation", defaultValue = "false") boolean ignoreInvitation) {
        Technician t = (Technician) request.getAttribute("user");
        Order o = orderService.get(orderId);
        if (o == null || (t.getId() != o.getMainTechId() && t.getId() != o.getSecondTechId())) {
            return new JsonMessage(false, "NO_ORDER", "你没有这个订单");
        } else if (o.getStatus() == Order.Status.CANCELED) {
            return new JsonMessage(false, "ORDER_CANCELED", "订单已取消");
        } else if (o.getStatusCode() >= Order.Status.FINISHED.getStatusCode()) {
            return new JsonMessage(false, "ORDER_ENDED", "订单已施工完成");
        } else if (o.getStatus() == Order.Status.SEND_INVITATION && !ignoreInvitation) {
            return new JsonMessage(false, "INVITATION_NOT_FINISH", "你邀请的合作人还未接受或拒绝邀请");
        } else if (o.getStatus() != Order.Status.IN_PROGRESS) { // 当第二个技师开始工作时,订单状态已进入IN_PROGRESS状态
            o.setStatus(Order.Status.IN_PROGRESS);
            orderService.save(o);
        } else if (constructionService.getByTechIdAndOrderId(t.getId(), orderId) != null) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已开始工作,请不要重复操作");
        }

        // 拒绝邀请或忽略邀请时,将第二责任人置空
        if (o.getStatus() == Order.Status.INVITATION_REJECTED ||
                (o.getStatus() == Order.Status.SEND_INVITATION && ignoreInvitation)) {
            o.setSecondTechId(0);
            orderService.save(o);
        }

        Construction construction = new Construction();
        construction.setOrderId(orderId);
        construction.setTechId(t.getId());
        construction.setStartTime(new Date());
        construction = constructionService.save(construction);
        return new JsonMessage(true, "", "", construction);
    }

    // 工作签到
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public JsonMessage signIn(HttpServletRequest request,
            @RequestParam("positionLon") String positionLon,
            @RequestParam("positionLat") String positionLat,
            @RequestParam("orderId")     int orderId) {
        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.get(orderId);
        Construction cons = constructionService.getByTechIdAndOrderId(tech.getId(), orderId);

        if (order.getStatus() == Order.Status.CANCELED) {
            return new JsonMessage(false, "ORDER_CANCELED", "订单已取消");
        } else if (order.getStatus() != Order.Status.IN_PROGRESS) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单还未开始工作或已结束工作");
        } else if (cons == null) {
            return new JsonMessage(false, "NO_CONSTRUCTION", "系统没有你的施工单, 请先点选\"开始工作\"");
        } else if (cons.getSigninTime() != null) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已签到, 请不要重复操作");
        }

        cons.setPositionLon(positionLon);
        cons.setPositionLat(positionLat);
        cons.setSigninTime(new Date());
        constructionService.save(cons);
        return new JsonMessage(true);
    }

    // 上传施工照片
    @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
    public JsonMessage uploadPhoto(HttpServletRequest request,
            @RequestParam("file")     MultipartFile file) throws IOException {
        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");

        String path = "/uploads/order";
        File dir = new File(request.getServletContext().getRealPath(path));
        if (!dir.exists()) dir.mkdirs();
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = "c-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
                + "-" + VerifyCode.generateVerifyCode(8) + extension;
        file.transferTo(new File(dir.getAbsoluteFile() + File.separator + filename));

        return new JsonMessage(true, "", "", path + "/" + filename);
    }

    // 提交施工前照片
    @RequestMapping(value = "/beforePhoto", method = RequestMethod.POST)
    public JsonMessage setBeforePhoto(HttpServletRequest request,
            @RequestParam("orderId") int orderId,
            @RequestParam("urls") String urls) {
        if (!Pattern.matches("^([^,\\s]+)(,[^,\\s]+)*$", urls)) {
            return new JsonMessage(false, "URLS_PATTERN_MISMATCH", "图片地址格式错误, 请查阅urls参数说明");
        } else if (urls.split(",").length > 3) {
            return new JsonMessage(false, "PHOTO_LIMIT_EXCEED", "图片数量超出限制, 最多3张");
        }

        Technician tech = (Technician) request.getAttribute("user");
        Construction cons = constructionService.getByTechIdAndOrderId(tech.getId(), orderId);
        if (cons == null) {
            return new JsonMessage(false, "NO_CONSTRUCTION", "系统没有你的施工单");
        } else if (cons.getSigninTime() == null) {
            return new JsonMessage(false, "CONSTRUCTION_NOT_SIGNIN", "签到前不可上传照片");
        } else if (cons.getEndTime() != null) {
            return new JsonMessage(false, "CONSTRUCTION_ENDED", "你已完成施工,不可再次上传照片");
        }

        cons.setBeforePhotos(urls);
        constructionService.save(cons);
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public JsonMessage finish(HttpServletRequest request,
            @RequestParam("orderId")   int orderId,
            @RequestParam("carSeat")   int carSeat,
            @RequestParam("workItems") String workItems) {
        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.get(orderId);
        if (order == null || (tech.getId() != order.getMainTechId() && tech.getId() != order.getSecondTechId())) {
            return new JsonMessage(false, "NO_SUCH_ORDER", "你没有这个订单");
        } else if (order.getStatus() != Order.Status.IN_PROGRESS) {
            return new JsonMessage(false, "ORDER_NOT_IN_PROGRESS", "非施工中订单, 不允许上传照片");
        }

        return null;
    }
}
