package com.autobon.platform.controller.technician.order;

import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.WorkItem;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.OrderService;
import com.autobon.order.service.WorkItemService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.VerifyCode;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by dave on 16/3/1.
 */
@RestController
@RequestMapping("/api/mobile/technician/construct")
public class ConstructionController {
    @Autowired OrderService orderService;
    @Autowired ConstructionService constructionService;
    @Autowired WorkItemService workItemService;
    @Autowired TechStatService techStatService;
    @Value("${com.autobon.uploadPath}") String uploadPath;

    // 开始工作
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public JsonMessage startWork(HttpServletRequest request,
            @RequestParam("orderId") int orderId,
            @RequestParam(value = "ignoreInvitation", defaultValue = "false") boolean ignoreInvitation) {
        Technician t = (Technician) request.getAttribute("user");
        Order o = orderService.get(orderId);
        if (o == null || (t.getId() != o.getMainTechId() && t.getId() != o.getSecondTechId())) {
            return new JsonMessage(false, "NO_ORDER", "你没有这个订单或主技师已放弃邀请");
        } else if (o.getStatus() == Order.Status.CANCELED) {
            return new JsonMessage(false, "ORDER_CANCELED", "订单已取消");
        } else if (o.getStatusCode() >= Order.Status.FINISHED.getStatusCode()) {
            return new JsonMessage(false, "ORDER_ENDED", "订单已施工完成");
        } else if (o.getSecondTechId() == t.getId() && o.getStatus() == Order.Status.SEND_INVITATION) {
            return new JsonMessage(false, "NOT_ACCEPTED_INVITATION", "你还没有接受邀请");
        } else if (o.getMainTechId() == t.getId() && o.getStatus() == Order.Status.SEND_INVITATION && !ignoreInvitation) {
            return new JsonMessage(false, "INVITATION_NOT_FINISH", "你邀请的合作人还未接受或拒绝邀请");
        } else if (constructionService.getByTechIdAndOrderId(t.getId(), orderId) != null) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已开始工作,请不要重复操作");
        }

        // 忽略邀请时,将第二责任人置空
        if (o.getStatus() == Order.Status.SEND_INVITATION && ignoreInvitation) {
            o.setSecondTechId(0);
        }
        o.setStatus(Order.Status.IN_PROGRESS); // 任一技师开始工作时,订单状态进入IN_PROGRESS状态; 订单所有技师完成工作时,订单结束
        orderService.save(o);

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

        if (order == null) {
            return new JsonMessage(false, "NO_ORDER", "没有这个订单");
        } else if (order.getStatus() == Order.Status.CANCELED) {
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
        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
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

    // 施工完成
    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public JsonMessage finish(HttpServletRequest request,
            @RequestParam("orderId") int orderId,
            @RequestParam("afterPhotos") String afterPhotos,
            @RequestParam(value = "carSeat", defaultValue = "0")  int carSeat,
            @RequestParam(value = "workItems", defaultValue = "") String workItems,
            @RequestParam(value = "percent", defaultValue = "0") float percent) {

        if (!Pattern.matches("^([^,\\s]+)(,[^,\\s]+){2,5}$", afterPhotos)) {
            return new JsonMessage(false, "AFTER_PHOTOS_PATTERN_MISMATCH",
                    "afterPhotos参数格式错误, 施工完成后照片应至少3张, 至多6张");
        } else if (!"".equals(workItems) && !Pattern.matches("^(\\d+)(,\\d+)*$", workItems)) {
            return new JsonMessage(false, "WORK_ITEMS_PATTERN_MISMATCH", "workItems参数格式错误");
        } else if (percent < 0 || percent > 1) {
            return new JsonMessage(false, "PERCENT_VALUE_INVALID", "百分比值应在0-1之间");
        }

        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.get(orderId);
        if (order == null) {
            return new JsonMessage(false, "NO_SUCH_ORDER", "没有这个订单");
        } else if (order.getStatus() != Order.Status.IN_PROGRESS) {
            return new JsonMessage(false, "ORDER_NOT_IN_PROGRESS", "订单未开始或已结束");
        }

        Construction cons = constructionService.getByTechIdAndOrderId(tech.getId(), order.getId());
        if (cons == null) {
            return new JsonMessage(false, "NO_CONSTRUCTION", "没有你的施工单");
        } else if (cons.getBeforePhotos() == null || "".equals(cons.getBeforePhotos())) {
            return new JsonMessage(false, "NO_BEFORE_PHOTO", "没有上传施工前照片");
        }

        ArrayList<String> thisItems = new ArrayList<>(Arrays.asList(workItems.split(",")));
        thisItems.removeIf(""::equals);
        List<WorkItem> workItemsList = workItemService.findByOrderTypeAndCarSeat(order.getOrderType(), carSeat);
        boolean usePercent = workItemService.findByOrderType(order.getOrderType()).size() == 1;
        // 非主技师应等待主技师先提交后再提交, 且不可重复主技师的工作项, 且按百分比算时, 与主技师百分比之和不可超过1
        if (order.getMainTechId() != tech.getId()) {
            Construction c = constructionService.getByTechIdAndOrderId(order.getMainTechId(), orderId);
            if (c == null || c.getEndTime() == null) {
                return new JsonMessage(false, "MAIN_TECH_NOT_COMMIT", "请等待主技师先提交完成");
            } else if (c.getCarSeat() != carSeat) {
                return new JsonMessage(false, "CAR_SEAT_MISMATCH", "主技师提交车型为" + c.getCarSeat() + "座, 请保持一致");
            }

            if (!usePercent && c.getWorkItems() != null && !"".equals(c.getWorkItems())) {
                List<String> mainItems = Arrays.asList(c.getWorkItems().split(","));
                List<String> conflicts = thisItems.stream()
                        .filter(mainItems::contains).collect(Collectors.toList());
                if (conflicts.size() > 0) {
                    String names = conflicts.stream().map(s -> workItemsList.stream()
                            .filter(i -> String.valueOf(i.getId()).equals(s))
                            .findFirst().get().getWorkName()).collect(Collectors.joining(","));
                    return new JsonMessage(false, "WORK_ITEM_CONFLICT",
                            "主技师已提交: \"" + names + "\", 请不要提交与主技师相同的工作项");
                }
            } else if (usePercent && c.getWorkPercent() + percent > 1) {
                return new JsonMessage(false, "PERCENT_SUM_EXCEED",
                        "工作量百分比与主技师之和超过100%, 主技师已完成" + (c.getWorkPercent()*100) + "%");
            }
        }

        JsonMessage msg = null;
        if ((!usePercent && "".equals(workItems)) || (usePercent && percent == 0f)) {// 没有任何工作
            cons.setPayment(0f);
            msg = new JsonMessage(true);
        } else if (!usePercent) { // 使用工作项
            List<WorkItem> thisWorkItems = thisItems.stream().map(i -> workItemsList.stream()
                    .filter(ii -> String.valueOf(ii.getId()).equals(i)).findFirst().orElse(null))
                    .collect(Collectors.toList());
            if (thisWorkItems.contains(null)) {
                String illegalItems = thisItems.stream().filter(i -> workItemsList.stream()
                        .filter(ii -> String.valueOf(ii.getId()).equals(i)).findFirst().orElse(null) == null)
                        .collect(Collectors.joining(", "));
                return new JsonMessage(false, "WORK_ITEM_NOT_IN_LIST", "无效工作项: " + illegalItems);
            } else {
                float pay = thisWorkItems.stream().map(WorkItem::getPrice).reduce(0f, (a, b) -> a + b);
                cons.setWorkItems(workItems);
                cons.setPayment(pay);
                msg = new JsonMessage(true);
            }
        } else { // 使用百分比
            cons.setWorkPercent(percent);
            cons.setPayment(workItemService.findByOrderType(order.getOrderType()).get(0).getPrice()*percent);
            msg = new JsonMessage(true);
        }

        if (!usePercent) cons.setCarSeat(carSeat);
        cons.setAfterPhotos(afterPhotos);
        cons.setEndTime(new Date());
        constructionService.save(cons);
        msg.setData(cons);

        // 结束订单
        if (order.getSecondTechId() == 0 || order.getSecondTechId() == tech.getId()) {
            order.setStatus(Order.Status.FINISHED);
            order.setFinishTime(new Date());
            orderService.save(order);
        }

        // 更新余额及未支付订单数
        TechStat stat = techStatService.getByTechId(tech.getId());
        if (stat == null) {
            stat = new TechStat();
            stat.setTechId(tech.getId());
        }
        stat.setUnpaidOrders(stat.getUnpaidOrders() + 1);
        stat.setBalance(stat.getBalance() + cons.getPayment());
        techStatService.save(stat);
        return msg;
    }
}
