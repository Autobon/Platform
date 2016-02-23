package com.autobon.platform.controller.order;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderShow;
import com.autobon.order.service.OrderService;
import com.autobon.platform.utils.JsonMessage;
import com.autobon.platform.utils.VerifyCode;
import com.autobon.technician.entity.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @RequestMapping(value = "/order/orderList", method = RequestMethod.GET)
    public JsonMessage orderList() throws Exception{
        JsonMessage jsonMessage = new JsonMessage(true,"orderList");
        List<Order> orderList = orderService.getOrderList();
        jsonMessage.setData(orderList);
        return jsonMessage;

    }

    /**
     * 获取所有的订单列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/order/all", method = RequestMethod.GET)
    public JsonMessage findAllOrders() throws Exception{
        JsonMessage jsonMessage = new JsonMessage(true,"allOrders");
        Page<Order> orderList = orderService.findAllOrders();
        jsonMessage.setData(orderList);
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
    @RequestMapping(value = "/order/findbykeys", method = RequestMethod.GET)
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
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    public JsonMessage addOrder() throws Exception{
        JsonMessage jsonMessage = new JsonMessage(true);

        return jsonMessage;
    }

    /**
     * 修改订单
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/order/update", method = RequestMethod.PUT)
    public JsonMessage upfataOrder() throws Exception{
        JsonMessage jsonMessage = new JsonMessage(true);

        return jsonMessage;
    }

    /**
     * 上传订单照片,获取
     * @param request
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/orderpic", method = RequestMethod.POST)
    public JsonMessage uploadOrderPic(HttpServletRequest request,
                                     @RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", NO_UPLOAD_FILE);

        JsonMessage msg = new JsonMessage(true);
        String path = "/uploads/orderpic";
        File dir = new File(request.getServletContext().getRealPath(path));
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;

        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        msg.setData("path + \"/\" + filename");
        return msg;
    }


}
