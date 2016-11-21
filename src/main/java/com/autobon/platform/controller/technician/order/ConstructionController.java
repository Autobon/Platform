package com.autobon.platform.controller.technician.order;

import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.WorkItem;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.OrderService;
import com.autobon.order.service.WorkItemService;
import com.autobon.order.vo.ConstructionShow;
import com.autobon.platform.listener.Event;
import com.autobon.platform.listener.OrderEventListener;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonResult;
import com.autobon.shared.VerifyCode;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
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
    @Autowired CoopAccountService coopAccountService;
    @Value("${com.autobon.uploadPath}") String uploadPath;
    @Autowired ApplicationEventPublisher publisher;


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
            return new JsonMessage(false, "ORDER_ENDED", "订单已结束");
        } else if (o.getSecondTechId() == t.getId() && o.getStatus() == Order.Status.SEND_INVITATION) {
            return new JsonMessage(false, "NOT_ACCEPTED_INVITATION", "你还没有接受邀请");
        } else if (o.getMainTechId() == t.getId() && o.getStatus() == Order.Status.SEND_INVITATION && !ignoreInvitation) {
            return new JsonMessage(false, "INVITATION_NOT_FINISH", "你邀请的合作人还未接受或拒绝邀请");
        }

        Construction cons = constructionService.getByTechIdAndOrderId(t.getId(), orderId);
        if (cons!= null) {
            return new JsonMessage(true, "", "", cons);
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
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单还未开始或已结束");
        } else if (cons == null) {
            return new JsonMessage(false, "NO_CONSTRUCTION", "系统没有你的施工单, 请先点选\"开始工作\"");
        } else if (cons.getSigninTime() != null) {
            return new JsonMessage(false, "REPEATED_OPERATION", "你已签到, 请不要重复操作");
        }

        if (order.getStatusCode() < Order.Status.SIGNED_IN.getStatusCode()) {
            order.setStatus(Order.Status.SIGNED_IN);
            orderService.save(order);
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
        } else if (urls.split(",").length > 9) {
            return new JsonMessage(false, "PHOTO_LIMIT_EXCEED", "图片数量超出限制, 最多9张");
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
            @RequestParam(value = "percent", defaultValue = "0") float percent) throws IOException {

        if (!Pattern.matches("^([^,\\s]+)(,[^,\\s]+){2,8}$", afterPhotos)) {
            return new JsonMessage(false, "AFTER_PHOTOS_PATTERN_MISMATCH",
                    "afterPhotos参数格式错误, 施工完成后照片应至少3张, 至多9张");
        } else if (!"".equals(workItems) && !Pattern.matches("^(\\d+)(,\\d+)*$", workItems)) {
            return new JsonMessage(false, "WORK_ITEMS_PATTERN_MISMATCH", "workItems参数格式错误");
        } else if (percent < 0 || percent > 1) {
            return new JsonMessage(false, "PERCENT_VALUE_INVALID", "百分比值应在0-1之间");
        }

        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.get(orderId);
        if (order == null) {
            return new JsonMessage(false, "NO_SUCH_ORDER", "没有这个订单");
        } else if (order.getStatus() != Order.Status.SIGNED_IN) {
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
            publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.FINISHED));
        }

        return msg;
    }




    /**
     * 车邻帮二期
     * 订单进入施工环节中
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/start", method = RequestMethod.POST)
    public JsonResult start(@RequestParam("orderId") int orderId,
                            HttpServletRequest request)throws Exception{

        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }
        Order order = orderService.get(orderId);
        if (order == null || (tech.getId() != order.getMainTechId())) {
            return new JsonResult(false, "你没有这个订单");
        }
        if (order.getStatus() == Order.Status.CANCELED) {
            return new JsonResult(false,  "订单已取消");
        }
        if (order.getStatusCode() >= Order.Status.FINISHED.getStatusCode()) {
            return new JsonResult(false, "订单已结束");
        }

        order.setStatus(Order.Status.IN_PROGRESS); // 订单状态进入IN_PROGRESS状态; 订单所有技师完成工作时,订单结束
        orderService.save(order);
        return new JsonResult(true, order);
    }


    /**
     * 车邻帮二期
     * 订单签到
     * @param request
     * @param positionLon
     * @param positionLat
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/signIn", method = RequestMethod.POST)
    public JsonResult sign(@RequestParam("positionLon") String positionLon,
                           @RequestParam("positionLat") String positionLat,
                           @RequestParam("orderId") int orderId,
                           HttpServletRequest request)throws Exception {

        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }
        Order order = orderService.get(orderId);

        if (order == null || order.getMainTechId() !=tech.getId() ) {
            return new JsonResult(false,  "没有这个订单");
        }
        if (order.getStatus() == Order.Status.CANCELED) {
            return new JsonResult(false, "订单已取消");
        }
        if (order.getStatus() != Order.Status.IN_PROGRESS) {
            return new JsonResult(false, "订单还未开始或已结束");
        }
        if (order.getSignTime() != null) {
            return new JsonResult(false, "你已签到, 请不要重复操作");
        }
        if (order.getStatusCode() >= Order.Status.SIGNED_IN.getStatusCode()) {
            return new JsonResult(false, "你已签到, 请不要重复操作");
        }
        order.setStatus(Order.Status.SIGNED_IN);
        order.setSignTime(new Date());
        orderService.save(order);
        return new JsonResult(true,"签到成功");
    }




    /**
     * 车邻帮二期
     * 开始工作
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/working", method = RequestMethod.POST)
    public JsonResult construction(@RequestParam("orderId") int orderId,
                            HttpServletRequest request)throws Exception{

        Technician t = (Technician) request.getAttribute("user");
        Order order = orderService.get(orderId);
        if (order == null || (t.getId() != order.getMainTechId())) {
            return new JsonResult(false, "你没有这个订单");
        }
        if (order.getStatus() == Order.Status.CANCELED) {
            return new JsonResult(false,  "订单已取消");
        }
        if (order.getStatusCode() != Order.Status.SIGNED_IN.getStatusCode()) {
            return new JsonResult(false, "签到以后才能施工");
        }

        if (order.getStatusCode() >= Order.Status.FINISHED.getStatusCode()) {
            return new JsonResult(false, "订单已结束");
        }

        order.setStatus(Order.Status.AT_WORK); // 订单状态进入IN_PROGRESS状态; 订单所有技师完成工作时,订单结束
        order.setStartTime(new Date());
        orderService.save(order);
        return new JsonResult(true, order);
    }



    /**
     * 车邻帮二期
     * 上传施工照片
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/v2/uploadPhoto", method = RequestMethod.POST)
    public JsonResult upload(@RequestParam("file")MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return new JsonResult(false, "没有上传文件");
        }
        String path = "/uploads/order";
        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
        if (!dir.exists()) dir.mkdirs();
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = "c-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
                + "-" + VerifyCode.generateVerifyCode(8) + extension;
        file.transferTo(new File(dir.getAbsoluteFile() + File.separator + filename));
        return new JsonResult(true,  path + "/" + filename);
    }


    /**
     * 车邻帮二期
     * 通过订单编号获取施工项目及对应的施工部位
     * @param request
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/v2/project/position/{orderId}", method = RequestMethod.GET)
    public JsonResult getProject(HttpServletRequest request,
                               @PathVariable("orderId") int orderId) throws IOException {
        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }
        Order order = orderService.get(orderId);

        if (order == null || order.getMainTechId() !=tech.getId() ) {
            return new JsonResult(false,  "没有这个订单");
        }


        return new JsonResult(true, orderService.getProject(orderId));
    }

    /**
     * 车邻帮二期
     * 通过订单编号获取施工项目及对应的施工部位
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/v2/project/position", method = RequestMethod.GET)
    public JsonResult getAllProject(HttpServletRequest request) throws IOException {
        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }

        return new JsonResult(true, orderService.getAllProject());
    }



        /**
         * 车邻帮二期
         * 提供施工前图片
         * @param request
         * @param orderId
         * @param urls
         * @return
         */
    @RequestMapping(value = "/v2/beforePhoto", method = RequestMethod.POST)
    public JsonResult uploadBeforePhoto(HttpServletRequest request,
                                        @RequestParam("orderId") int orderId,
                                        @RequestParam("urls") String urls)throws Exception{

        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }
        Order order = orderService.get(orderId);

        if (order == null || order.getMainTechId() !=tech.getId() ) {
            return new JsonResult(false,  "没有这个订单");
        }
        if (!Pattern.matches("^([^,\\s]+)(,[^,\\s]+)*$", urls)) {
            return new JsonResult(false,  "图片地址格式错误, 请查阅urls参数说明");
        }
        if (urls.split(",").length > 9) {
            return new JsonResult(false,  "图片数量超出限制, 最多9张");
        }

        if (order.getStatusCode() != Order.Status.SIGNED_IN.getStatusCode()) {
            return new JsonResult(false, "签到以后才能施工");
        }

        if (order.getSignTime() == null) {
            return new JsonResult(false, "签到前不可上传照片");
        }
        if (order.getEndTime() != null) {
            return new JsonResult(false, "你已完成施工,不可再次上传照片");
        }
        order.setStatus(Order.Status.AT_WORK); // 进入工作状态
        order.setStartTime(new Date());
        order.setBeforePhotos(urls);
        orderService.save(order);
        return new JsonResult(true,"上传施工前照片成功");
    }


    /**
     * 车邻帮二期
     * 完成工作
     * @param constructionShow
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/v2/finish", method = RequestMethod.POST)
    public JsonResult finish(@RequestBody ConstructionShow constructionShow,
                             HttpServletRequest request) throws IOException {

        if (!Pattern.matches("^([^,\\s]+)(,[^,\\s]+){2,8}$", constructionShow.getAfterPhotos())) {
            return new JsonResult(false, "afterPhotos参数格式错误, 施工完成后照片应至少3张, 至多9张");
        }

        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }
        Order order = orderService.get(constructionShow.getOrderId());
//        if (order == null || order.getMainTechId() !=tech.getId()) {
//            return new JsonResult(false, "没有这个订单");
//        }
//        if (order.getStatus() != Order.Status.AT_WORK) {
//            return new JsonResult(false,  "订单不在工作中，无法完成订单");
//        }
//        if (order.getStatus() != Order.Status.FINISHED) {
//            return new JsonResult(false,  "订单已经提交，不能再次提交");
//        }
//        if (order.getBeforePhotos() == null || "".equals(order.getBeforePhotos())) {
//            return new JsonResult(false,  "没有上传施工前照片");
//        }

        // 结束订单
        order.setStatus(Order.Status.FINISHED);
        order.setFinishTime(new Date());
        order.setAfterPhotos(constructionShow.getAfterPhotos());
        orderService.save(order, constructionShow);

        publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.FINISHED));


        //合作技师施工部位推送

        return new JsonResult(true, "施工完成");
    }

    @RequestMapping(value = "/v2/test", method = RequestMethod.GET)
    public JsonResult test(){
        return new JsonResult(true,"true");
    }

}
