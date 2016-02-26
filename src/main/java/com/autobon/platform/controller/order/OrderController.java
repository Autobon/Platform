package com.autobon.platform.controller.order;

import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.repository.ConstructionRepository;
import com.autobon.order.entity.OrderShow;
import com.autobon.order.service.OrderService;
import com.autobon.order.util.OrderUtil;
import com.autobon.platform.utils.DateUtil;
import com.autobon.platform.utils.JsonMessage;
import com.autobon.platform.utils.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yuh on 2016/2/22.
 */
@RestController
@RequestMapping("/api")
public class OrderController {
    public final static String NO_UPLOAD_FILE = "没有要上传文件";
    private OrderService orderService = null;
    @Autowired
    public void setOrderService(OrderService orderService){this.orderService = orderService;}

    @Value("${com.autobon.upload.orderpic.path}")
    private String orderpic_path;

    @Autowired
    private ConstructionRepository constructionRepository;

    @RequestMapping(value = "/order/orderList", method = RequestMethod.GET)
    public JsonMessage orderList() throws Exception{
        JsonMessage jsonMessage = new JsonMessage(true,"orderList");
        List<Order> orderList = orderService.getOrderList();
        jsonMessage.setData(orderList);
        return jsonMessage;

    }


    @RequestMapping(value = "/order/getLocation", method = RequestMethod.GET)
    public JsonMessage getLocation(
            @RequestParam("orderId") int orderId){
        JsonMessage jsonMessage = new JsonMessage(true,"location");
        Order order  = orderService.getLocation(orderId);

        jsonMessage.setData(order);
        return  jsonMessage;
    }

    @RequestMapping(value = "/order/signIn", method = RequestMethod.POST)
    public JsonMessage signIn(
            @RequestParam("rtpositionLon") String rtpositionLon,
            @RequestParam("rtpositionLat") String rtpositionLat,
            @RequestParam("technicianId") int technicianId,
            @RequestParam("orderId") int orderId){
        JsonMessage jsonMessage = new JsonMessage(true,"signIn");
        Construction construction = new Construction();
        construction.setOrderId(orderId);
        construction.setTechnicianId(technicianId);
        construction.setRtpositionLon(rtpositionLon);
        construction.setRtpositionLat(rtpositionLat);
        construction.setSigninTime(new Date());
        constructionRepository.save(construction);
        return jsonMessage;
    }

    /**
     * 根据条件查找订单列表
     * @param orderNum 订单编号
     * @param orderType 订单类型
     * @param status 订单状态
     * @param customerId 下单客户
     * @param currentPage 当前页
     * @param pageSize 分页数
     * @param orderByProperty 按照什么字段排序
     * @param ascOrDesc 排序方式
     * @return 订单列表
     * @throws Exception
     */
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public JsonMessage findOrderByKeys(HttpServletResponse response,
                                       @RequestParam(value = "orderNum",required = false)String orderNum,
                                       @RequestParam(value = "orderType",required = false) Integer orderType,
                                       @RequestParam(value = "status",required = false) Integer status,
                                       @RequestParam(value = "customerId",required = false) Integer customerId,
                                       @RequestParam(value = "currentPage",required = false) Integer currentPage,
                                       @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                       @RequestParam(value = "orderByProperty",required = false) String orderByProperty,
                                       @RequestParam(value = "ascOrDesc",required = false) Integer ascOrDesc) throws Exception{
        JsonMessage jsonMessage = new JsonMessage(true);
        Page<OrderShow> orderShowPage = orderService.findByKeys(orderNum, orderType, status, customerId, currentPage, pageSize, orderByProperty, ascOrDesc);
        List<OrderShow> orderShowList = orderShowPage.getContent();
        if(!orderShowList.isEmpty()){
            response.addHeader("page",String.valueOf(orderShowPage.getNumber()+1));
            response.addHeader("page-count",String.valueOf(orderShowPage.getTotalPages()));
        }
        jsonMessage.setData(orderShowList);
        return jsonMessage;
    }

    /**
     * 添加订单
     * @return JsonMessage
     * @throws Exception
     */
    @RequestMapping(value = "/order", headers = "content-type=multipart/form-data", method = RequestMethod.POST)
    public JsonMessage addOrder(MultipartHttpServletRequest request) throws Exception{
        //生成订单编号 时间戳&用户ID&四位随机数
        String timeStamp = DateUtil.generateTimeStamp();
        String random = VerifyCode.generateRandomNumber(4);
        StringBuffer orderNum = new StringBuffer(timeStamp);
        OrderShow orderShow = new OrderShow();
        orderNum = orderNum.append(orderShow.getCustomerId()).append(random);
        orderShow.setOrderNum(orderNum.toString());

        orderShow.setOrderType(Integer.valueOf(request.getParameter("orderType")));
        orderShow.setAddTime(new Date());
        if(request.getParameter("orderTime") != null)
        orderShow.setOrderTime(DateUtil.string2Date(request.getParameter("orderTime")));
        orderShow.setRemark(request.getParameter("remark"));
        if(request.getParameter("customerType") != null)
        orderShow.setCustomerType(Integer.valueOf(request.getParameter("customerType")));
        if(request.getParameter("customerId") != null)
        orderShow.setCustomerId(Integer.valueOf(request.getParameter("customerId")));

        //上传图片
        Iterator<String> itr=request.getFileNames();
        MultipartFile file=request.getFile(itr.next());
        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", NO_UPLOAD_FILE);
        File dir = new File(orderpic_path);
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;
        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        orderShow.setPhoto("api/downOrderPic?pic="+filename);
        Order order = orderService.addOrder(orderShow);
        if(order != null){
            return  new JsonMessage(true, "订单添加成功",OrderUtil.order2OrderShow(order));
        }else{
            return  new JsonMessage(false, "订单添加失败");
        }
    }

    /**
     * 修改订单
     * @param orderShow
     * @return JsonMessage
     * @throws Exception
     */
    @RequestMapping(value = "/order/modify", method = RequestMethod.PUT)
    public JsonMessage updateOrder(@RequestBody OrderShow orderShow) throws Exception{
        Order order = orderService.updateOrder(orderShow);
        if(order != null){
            return  new JsonMessage(true, "订单修改成功",OrderUtil.order2OrderShow(order));
        }else{
            return  new JsonMessage(false, "订单修改失败");
        }
    }

    /**
     * 绑定技师
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/order/{orderId}/technician/{techId}" ,method = RequestMethod.PUT)
    public JsonMessage bindTechnician(@PathVariable("orderId") int orderId, @PathVariable("techId") int techId) throws Exception{
        JsonMessage jsonMessage = new JsonMessage(true);

        return jsonMessage;
    }

    /**
     * 上传订单照片
     * @param request
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/orderpic",headers = "content-type=multipart/form-data", method = RequestMethod.POST)
    public JsonMessage uploadOrderPic(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {

        Iterator<String> itr=request.getFileNames();
        MultipartFile file=request.getFile(itr.next());
        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", NO_UPLOAD_FILE);
        JsonMessage msg = new JsonMessage(true);
        File dir = new File(orderpic_path);
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;
        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        msg.setData(orderpic_path + "\"/\" "+ filename);
        return msg;
    }



}
