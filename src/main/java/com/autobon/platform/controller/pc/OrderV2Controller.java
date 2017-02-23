package com.autobon.platform.controller.pc;



import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderProduct;
import com.autobon.order.entity.OrderView;
import com.autobon.order.entity.WorkDetail;
import com.autobon.order.service.*;
import com.autobon.order.vo.*;
import com.autobon.shared.JsonPage;
import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.LocationStatusService;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by wh on 2016/11/15.
 */
@RestController
@RequestMapping("/api/web/admin/order")
public class OrderV2Controller {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderProductService orderProductService;

    @Autowired
    LocationStatusService locationStatusService;

    @Autowired
    CooperatorService cooperatorService;

    @Autowired
    WorkDetailService workDetailService;

    @Autowired
    ConstructionWasteService constructionWasteService;

    @Autowired
    TechnicianService technicianService;

    @Autowired
    ConstructionProjectService constructionProjectService;

    @Autowired
    OrderViewService orderViewService;

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/{orderId}", method = RequestMethod.GET)
    public JsonResult getById(@PathVariable("orderId") int orderId){
        OrderView orderShow = orderViewService.findById(orderId);
        List<WorkDetailShow> workDetailShowList = workDetailService.getByOrderId(orderId);
        if(workDetailShowList != null) {
            for (WorkDetailShow workDetailShow : workDetailShowList) {
                if (workDetailShow.getTechId() == orderShow.getTechId()) {
                    workDetailShow.setIsMainTech(1);
                }
            }
            List<ConstructionWasteShow> constructionWasteShows = constructionWasteService.getByOrderId(orderId);
            orderShow.setWorkDetailShows(workDetailShowList);
            orderShow.setConstructionWasteShows(constructionWasteShows);

        }
        return new JsonResult(true, orderShow);
    }


    /**
     * 查询订单产品列表
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/{orderId}/product", method = RequestMethod.GET)
    public JsonResult getProduct(@PathVariable("orderId") int orderId){

        return new JsonResult(true, orderProductService.findByOrderId(orderId));
    }


    /**
     * 修改订单
     * @param orderId 订单ID
     * @param type 施工项目
     * @param statusCode 订单状态
     * @param remark 备注
     * @param positionLon 纬度
     * @param positionLat 经度
     * @param agreedStartTime 预约开工时间
     * @param agreedEndTime 最晚交车时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/v2/{orderId}", method = RequestMethod.POST)
    public JsonResult modifyOrder(@PathVariable("orderId") int orderId,
                                  @RequestParam(value = "type", required = false) String type,
                                  @RequestParam(value = "status", required = false) Order.Status statusCode,
                                  @RequestParam(value = "techId", required = false) Integer techId,
                                  @RequestParam(value = "remark", required = false)String remark,
                                  @RequestParam(value = "positionLon", required = false)String positionLon,
                                  @RequestParam(value = "positionLat", required = false)String positionLat,
                                  @RequestParam(value = "agreedStartTime", required = false)Long  agreedStartTime,
                                  @RequestParam(value = "agreedEndTime", required = false)Long  agreedEndTime)throws Exception{

        Order order = orderService.get(orderId);
        if(order == null){
            return new JsonResult(false,"订单不存在");
        }

        if(statusCode != null) {
            order.setStatusCode(statusCode.getStatusCode());
        }

        if(agreedStartTime!=null){
            Date date = new Date(agreedStartTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateStr = sdf.format(date);
            order.setAgreedStartTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr));
        }

        if (agreedEndTime != null) {
            Date date = new Date(agreedEndTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateStr = sdf.format(date);
            order.setAgreedEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr));
        }

        if(techId != null){
            if(order.getStatusCode()<= Order.Status.NEWLY_CREATED.getStatusCode()){
                order.setStatusCode(Order.Status.TAKEN_UP.getStatusCode());
            }
            order.setMainTechId(techId);
        }

        order.setType(type == null ? order.getType() : type);
     //   order.setStatusCode(statusCode == null ? order.getStatusCode() : statusCode);
        order.setRemark(remark == null ? order.getRemark() : remark);
        order.setPositionLat(positionLat == null ? order.getPositionLat() : positionLat);
        order.setPositionLon(positionLon == null ? order.getPositionLon() : positionLon);
     //   order.setAgreedEndTime(agreedEndTime == null ? order.getAgreedEndTime() : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(agreedEndTime));
   //     order.setAgreedStartTime(agreedStartTime == null ? order.getStartTime() : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(agreedStartTime));
        orderService.save(order);
        return new JsonResult(true, order);
    }






    /**
     * 通过订单编号获取施工项目及对应的施工部位
     * @param request
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/project/position/{orderId}", method = RequestMethod.GET)
    public JsonResult getProject(HttpServletRequest request,
                                 @PathVariable("orderId") int orderId) {

        try{
            Order order = orderService.get(orderId);

            if (order == null  ) {
                return new JsonResult(false,  "没有这个订单");
            }
            return new JsonResult(true, orderService.getProject(orderId));

        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 通过 施工项目查看对应的施工部位
     * @param request
     * @param projectId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/project/{projectId}/position", method = RequestMethod.GET)
    public JsonResult getPosition(HttpServletRequest request,
                                 @PathVariable("projectId") int projectId) {

        try{
            return new JsonResult(true, constructionProjectService.findByProject(projectId));
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 保存订单产品
     * @param orderId 订单ID
     * @param orderProductList 产品列表
     * @return
     */
    @RequestMapping(value = "/project/product/{orderId}", method = RequestMethod.POST)
    public JsonResult saveOrderProduct( @PathVariable("orderId") int orderId,
                                        @RequestBody List<OrderProduct> orderProductList){

        Order order = orderService.get(orderId);

        if (order == null  ) {
            return new JsonResult(false,  "没有这个订单");
        }

        if(orderProductList != null && orderProductList.size()>0){
            orderProductService.batchInsert(orderProductList);
            return new JsonResult(true,"保存成功");
        }

        return new JsonResult(false,"没有填写产品项目");
    }






    /**
     *
     * 修改订单产品
     * @param orderId 订单ID
     * @param orderProductList 订单产品列表
     * @return
     */
    @RequestMapping(value = "/project/product/{orderId}", method = RequestMethod.PUT)
    public JsonResult modifyOrderProduct(@PathVariable("orderId") int orderId,
                                        @RequestBody List<OrderProduct> orderProductList){

        Order order = orderService.get(orderId);

        if (order == null  ) {
            return new JsonResult(false,  "没有这个订单");
        }

        if(orderProductList != null && orderProductList.size()>0){
            orderProductService.save(orderProductList);
            return new JsonResult(true,"保存成功");
        }

        return new JsonResult(false,"没有填写产品项目");
    }


    /**
     * 通过订单编号或者施工项目及施工部位
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/project/product/{orderId}", method = RequestMethod.GET)
    public JsonResult getOrderProduct(@PathVariable("orderId") int orderId){

        Order order = orderService.get(orderId);

        if (order == null  ) {
            return new JsonResult(false,  "没有这个订单");
        }


        return new JsonResult(true,orderProductService.get(orderId));
    }


    /**
     * 后台指派技师 地图展示
     * 第一次加载数据时，无需传入经纬度 取商户坐标为中心点，需要传入 千米数
     * 拖动地图控件时将中心点 传入该API
     * @param orderId 订单ID
     * @param longitude
     * @param latitude
     * @param kilometre
     * @return
     */
    @RequestMapping(value = "/technician/assign/{orderId}", method = RequestMethod.GET)
    public JsonResult assign(@PathVariable("orderId") int orderId,
                             @RequestParam(value = "longitude",required = false) String longitude,
                             @RequestParam(value ="latitude",required = false) String latitude,
                             @RequestParam(value = "kilometre",required = false) int kilometre){

        Order order = orderService.get(orderId);

        if (order == null  ) {
            return new JsonResult(false,  "没有这个订单");
        }
        Cooperator cooperator =  cooperatorService.get(order.getCoopId());
        if(cooperator == null){
            return new JsonResult(false,  "商户不存在");
        }
        CoopTechnicianLocation coopTechnicianLocation = new CoopTechnicianLocation();
        coopTechnicianLocation.setCoopLatitude(cooperator.getLatitude());
        if(longitude==null && latitude == null){
            coopTechnicianLocation.setCoopLongitude(cooperator.getLongitude());
            coopTechnicianLocation.setLocalStatuses(locationStatusService.getTechByDistance(coopTechnicianLocation.getCoopLatitude(), coopTechnicianLocation.getCoopLongitude(), kilometre));
        }else {
            coopTechnicianLocation.setCoopLongitude(cooperator.getLongitude());
            coopTechnicianLocation.setLocalStatuses(locationStatusService.getTechByDistance(latitude, longitude, kilometre));

        }
        return  new JsonResult(true,  coopTechnicianLocation);
    }


    /**
     * 查询技师
     * @param query 查询内容 纯数字则查询手机 反之查询姓名
     * @param page 页码
     * @param pageSize 页面大小
     * @return JsonResult对象
     */
    @RequestMapping(value = "/technician/assign",method = RequestMethod.GET)
    public JsonResult getTechnician(@RequestParam(value = "query", required = false) String query,
                                    @RequestParam(value = "page",  defaultValue = "1" )  int page,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize){

        try {

            Page<Technician> technicians;
            String query1;
            if(query != null) {
                 query1 = "%" + query + "%";
                if (Pattern.matches("\\d+", query)) {
                    technicians = technicianService.find(query1, null, page, pageSize);

                } else {
                    technicians = technicianService.find(null, query1, page, pageSize);
                }
            }else{
                technicians = technicianService.find(null, null, page, pageSize);
            }

            return new JsonResult(true, technicians);
        }catch (Exception e){
            return  new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 后台指派技师
     * @param orderId 订单ID
     * @param techId 技师ID
     * @return
     */
    @RequestMapping(value = "/{orderId}/technician/{techId}/assign", method = RequestMethod.POST)
    public JsonResult assign(@PathVariable("orderId") int orderId,
                             @PathVariable("techId") int techId){

        Order order = orderService.get(orderId);
        if (order == null ) {
            return new JsonResult(false,  "没有这个订单");
        }

        Technician technician  = technicianService.get(techId);
        if(technician!= null && technician.getWorkStatus()== 3){
            return new JsonResult(false,  "技师未认证或休息状态，不可指派");
        }

        order.setReassignmentStatus(2);
        if(order.getStatusCode() == -10){
            order.setStatusCode(Order.Status.TAKEN_UP.getStatusCode());
            order.setReassignmentStatus(0);
            order.setTakenTime(new Date());
        }

        order.setMainTechId(techId);

        orderService.save(order);
        return  new JsonResult(true,  order);
    }


    /**
     * 测试使用
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(){

        List<WorkDetail> list = new ArrayList<>();

        WorkDetail  workDetail = new WorkDetail();
        workDetail.setOrderId(1);
        workDetail.setProject1(1);
        workDetail.setPosition1("1,2");
        workDetail.setProject2(2);
        workDetail.setPosition2("3,4");

        list.add(workDetail);
        workDetailService.balance(1);
    }


    /**
     * 导出EXCEL
     * @param tech 技师电话或者名字
     * @param coopId 商户ID
     * @param startTime 订单开始时间
     * @param endTime 订单结束时间
     * @param status 订单状态
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value="/excel/download", method = RequestMethod.GET)
    public void download(   @RequestParam(value = "orderNum", required = false) String orderNum,
                            @RequestParam(value = "orderCreator", required = false) String orderCreator,
                            @RequestParam(value = "orderType", required = false) Integer orderType,
                            @RequestParam(value = "orderStatus", required = false) Order.Status orderStatus,
                            @RequestParam(value = "sort", defaultValue = "id") String sort,
                            @RequestParam(value = "tech", required = false) String tech,
                            @RequestParam(value = "coopId", required = false) String coopId,
                            @RequestParam(value = "startTime", required = false)Long  startTime,
                            @RequestParam(value = "endTime", required = false)Long  endTime,
                            @RequestParam(value = "status", required = false) Order.Status status,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException{
        String fileName="excel文件";
        //填充projects数据
        String columnNames[]={"ID","项目名","销售人","负责人","所用技术","备注"};//列名
        String keys[]    =     {"id","name","saler","principal","technology","remarks"};//map中的key
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面

        response.reset();

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=freeRideContract"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".xls");

    //    response.setContentType("application/vnd.ms-excel;charset=utf-8");
    //    response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }



}
