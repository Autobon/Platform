package com.autobon.platform.controller.order;

import com.autobon.order.entity.Comment;
import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderShow;
import com.autobon.order.service.CommentService;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.OrderService;
import com.autobon.order.service.WorkItemService;
import com.autobon.order.util.OrderUtil;
import com.autobon.shared.JsonMessage;
import com.autobon.technician.entity.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuh on 2016/2/22.
 */
@RestController
@RequestMapping("/api/mobile/technician/order")
public class OrderController {
    @Autowired OrderService orderService;
    @Autowired ConstructionService constructionService;
    @Autowired WorkItemService workItemService;
    @Autowired
    CommentService commentService;

    // 获取主要责任人订单列表
    @RequestMapping(value = "/listMain", method = RequestMethod.GET)
    public JsonMessage listMain(HttpServletRequest request,
             @RequestParam(value = "page", defaultValue = "1") int page,
             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician technician = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                orderService.findByMainTechId(technician.getId(), page, pageSize));
    }

    // 获取次要责任人订单列表
    @RequestMapping(value = "/listSecond", method = RequestMethod.GET)
    public JsonMessage listSecond(HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Technician technician = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "",
                orderService.findBySecondTechId(technician.getId(), page, pageSize));
    }

    // 获取订单信息
    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public JsonMessage show(HttpServletRequest request,
            @PathVariable("orderId") int orderId) {
        return new JsonMessage(true, "", "", orderService.findOrder(orderId));
    }

    // 抢单
    @RequestMapping(value = "/takeup", method = RequestMethod.POST)
    public JsonMessage takeUpOrder(HttpServletRequest request,
            @RequestParam("orderId") int orderId) {
        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.findOrder(orderId);
        if (order.getStatus() != Order.Status.NEWLY_CREATED)
            return new JsonMessage(false, "ILLEGAL_OPERATION", "已有人接单");

        order.setMainTechId(tech.getId());
        order.setStatus(Order.Status.TAKEN_UP);
        orderService.save(order);
        return new JsonMessage(true, "", "", order);
    }

    // 开始工作
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public JsonMessage startWork(HttpServletRequest request,
            @RequestParam("orderId") int orderId,
            @RequestParam(value = "ignoreInvitation", defaultValue = "false") boolean ignoreInvitation) {
        Technician t = (Technician) request.getAttribute("user");
        Order o = orderService.findOrder(orderId);
        if (o == null || (t.getId() != o.getMainTechId() && t.getId() != o.getSecondTechId())) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "你没有这个订单");
        } else if (o.getStatusCode() >= Order.Status.FINISHED.getStatusCode()) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单已施工完成");
        } else if (o.getStatus() == Order.Status.SEND_INVITATION && !ignoreInvitation) {
            return new JsonMessage(false, "INVITATION_NOT_FINISH", "你邀请的合作人还未接受或拒绝邀请");
        } else if (o.getStatus() != Order.Status.IN_PROGRESS) { // 当第二个技师开始工作时,订单状态已进入IN_PROGRESS状态
            o.setStatus(Order.Status.IN_PROGRESS);
            orderService.save(o);
        }

        Construction construction = new Construction();
        construction.setOrderId(orderId);
        construction.setTechnicianId(t.getId());
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
        Order order = orderService.findOrder(orderId);
        List<Construction> list = constructionService.findByOrderIdAndTechnicianId(orderId, tech.getId());
        if (list.size() < 1)
            return new JsonMessage(false, "ILLEGAL_OPERATION", "系统没有你的施工单, 请先点选\"开始工作\"");
        if (order.getStatus() != Order.Status.IN_PROGRESS)
            return new JsonMessage(false, "ILLEGAL_OPERATION", "订单还未开始工作或已结束工作");

        Construction construction = list.get(0);
        construction.setPositionLon(positionLon);
        construction.setPositionLat(positionLat);
        construction.setSigninTime(new Date());
        constructionService.save(construction);
        return new JsonMessage(true);
    }

    // TODO 继续清理下面的代码

    /**
     * 根据条件查找订单列表
     *
     * @param orderNum        订单编号
     * @param orderType       订单类型
     * @param status          订单状态
     * @param customerId      下单客户
     * @param currentPage     当前页
     * @param pageSize        分页数
     * @param orderByProperty 按照什么字段排序
     * @param ascOrDesc       排序方式
     * @return 订单列表
     * @throws Exception
     */
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public JsonMessage findOrderByKeys(HttpServletResponse response,
                                       @RequestParam(value = "orderNum", required = false) String orderNum,
                                       @RequestParam(value = "orderType", required = false) Integer orderType,
                                       @RequestParam(value = "status", required = false) Integer status,
                                       @RequestParam(value = "customerId", required = false) Integer customerId,
                                       @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                       @RequestParam(value = "orderByProperty", required = false) String orderByProperty,
                                       @RequestParam(value = "ascOrDesc", required = false) Integer ascOrDesc) throws Exception {
        JsonMessage jsonMessage = new JsonMessage(true);
        Page<OrderShow> orderShowPage = orderService.findByKeys(orderNum, orderType, status, customerId, currentPage, pageSize, orderByProperty, ascOrDesc);
        List<OrderShow> orderShowList = orderShowPage.getContent();
        if (!orderShowList.isEmpty()) {
            response.addHeader("page", String.valueOf(orderShowPage.getNumber() + 1));
            response.addHeader("page-count", String.valueOf(orderShowPage.getTotalPages()));
        }
        jsonMessage.setData(orderShowList);
        return jsonMessage;
    }

    /**
     * 添加订单
     *
     * @return JsonMessage
     * @throws Exception
     */
    @RequestMapping(value = "/order", headers = "content-type=multipart/form-data", method = RequestMethod.POST)
    public JsonMessage addOrder(MultipartHttpServletRequest request) throws Exception {
        //生成订单编号 时间戳&用户ID&四位随机数
//        String timeStamp = DateUtil.generateTimeStamp();
//        String random = VerifyCode.generateRandomNumber(4);
//        StringBuffer orderNum = new StringBuffer(timeStamp);
//        OrderShow orderShow = new OrderShow();
//        orderNum = orderNum.append(orderShow.getCustomerId()).append(random);
//        orderShow.setOrderNum(orderNum.toString());
//
//        orderShow.setOrderType(Integer.valueOf(request.getParameter("orderType")));
//        orderShow.setAddTime(new Date());
//        if (request.getParameter("orderTime") != null)
//            orderShow.setOrderTime(DateUtil.string2Date(request.getParameter("orderTime")));
//        orderShow.setRemark(request.getParameter("remark"));
//        if (request.getParameter("customerType") != null)
//            orderShow.setCustomerType(Integer.valueOf(request.getParameter("customerType")));
//        if (request.getParameter("customerId") != null)
//            orderShow.setCustomerId(Integer.valueOf(request.getParameter("customerId")));
//
//        //上传图片
//        Iterator<String> itr = request.getFileNames();
//        MultipartFile file = request.getFile(itr.next());
//        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有要上传文件");
//        String path = "/uploads/order/pic";
//        File dir = new File(request.getServletContext().getRealPath(path));
//        String originalFilename = file.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
//        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
//                + (VerifyCode.generateRandomNumber(6)) + extension;
//        if (!dir.exists()) dir.mkdirs();
//        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
//        orderShow.setPhoto(path + File.separator + filename);
//        Order order = orderService.addOrder(orderShow);
//
//        if(order != null){
//            return  new JsonMessage(true, "", "订单添加成功",OrderUtil.order2OrderShow(order));
//        }else{
            return  new JsonMessage(false, "订单添加失败");
//        }
    }

    /**
     * 修改订单
     *
     * @param orderShow
     * @return JsonMessage
     * @throws Exception
     */
    @RequestMapping(value = "/order/modify", method = RequestMethod.PUT)
    public JsonMessage updateOrder(@RequestBody OrderShow orderShow) throws Exception {
        Order order = orderService.updateOrder(orderShow);

        if(order != null){
            return  new JsonMessage(true, "", "订单修改成功",OrderUtil.order2OrderShow(order));
        }else{
            return  new JsonMessage(false, "订单修改失败");
        }
    }

    /**
     * 绑定技师
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/order/{orderId}/technician/{techId}", method = RequestMethod.PUT)
    public JsonMessage bindTechnician(@PathVariable("orderId") int orderId, @PathVariable("techId") int techId) throws Exception {
        JsonMessage jsonMessage = new JsonMessage(true);

        return jsonMessage;
    }

    /**
     * 上传订单照片
     *
     * @param request
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/orderpic", headers = "content-type=multipart/form-data", method = RequestMethod.POST)
    public JsonMessage uploadOrderPic(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {

//        Iterator<String> itr = request.getFileNames();
//        MultipartFile file = request.getFile(itr.next());
//        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", NO_UPLOAD_FILE);
//        JsonMessage msg = new JsonMessage(true);
//        File dir = new File(orderpic_path);
//        String originalFilename = file.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
//        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
//                + (VerifyCode.generateRandomNumber(6)) + extension;
//        if (!dir.exists()) dir.mkdirs();
//        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
//        msg.setData(orderpic_path + "\"/\" " + filename);
//        return msg;
        return null;
    }

    @RequestMapping(value="/mobile/order/getWorkList",method = RequestMethod.POST)
    public JsonMessage getWorkList(@RequestParam("orderId") int orderId){
        JsonMessage jsonMessage = new JsonMessage(true, "getWorkList");
        Order order = orderService.findOrder(orderId);
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
            Map<String,Object> dataMap = new HashMap<String,Object>();
            //查询工作项列表，主技师已选列表
            List<Construction> constructionList = constructionService.findByOrderIdAndTechnicianId(orderId, mainTechId);
            if(constructionList.size() == 1){
                Construction construction = constructionList.get(0);
                String workLoad = construction.getWorkItems();
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
        Order order = orderService.findOrder(orderId);
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

    @RequestMapping(value="/comment",method = RequestMethod.POST)
    public JsonMessage comment(@RequestParam("orderId") int orderId,
                               @RequestParam("star") int star,
                               @RequestParam("arriveOnTime") int arriveOnTime,
                               @RequestParam("completeOnTime") int completeOnTime,
                               @RequestParam("professional") int professional,
                               @RequestParam("dressNeatly") int dressNeatly,
                               @RequestParam("carProtect") int carProtect,
                               @RequestParam("goodAttitude") int goodAttitude,
                               @RequestParam("advice") String advice){

        JsonMessage jsonMessage = new JsonMessage(true,"comment");
        Order order = orderService.findOrder(orderId);
        int mainTechId = order.getMainTechId();
        int secondTechId = order.getSecondTechId();
        if(mainTechId == 0){
            return new JsonMessage(false,"此订单未指定技师");
        }else{
            Comment comment = new Comment();
            comment.setTechnicianId(mainTechId);
            comment.setOrderId(orderId);
            comment.setStar(star);
            comment.setArriveOnTime(arriveOnTime);
            comment.setCompleteOnTime(completeOnTime);
            comment.setProfessional(professional);
            comment.setDressNeatly(dressNeatly);
            comment.setCarProtect(carProtect);
            comment.setGoodAttitude(goodAttitude);
            comment.setAdvice(advice);
            commentService.saveComment(comment);
        }
        if(secondTechId != 0){
            Comment comment = new Comment();
            comment.setTechnicianId(secondTechId);
            comment.setOrderId(orderId);
            comment.setStar(star);
            comment.setArriveOnTime(arriveOnTime);
            comment.setCompleteOnTime(completeOnTime);
            comment.setProfessional(professional);
            comment.setDressNeatly(dressNeatly);
            comment.setCarProtect(carProtect);
            comment.setGoodAttitude(goodAttitude);
            comment.setAdvice(advice);
            commentService.saveComment(comment);
        }

        return jsonMessage;
    }

}
