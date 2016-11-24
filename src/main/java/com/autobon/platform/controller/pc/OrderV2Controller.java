package com.autobon.platform.controller.pc;


import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.order.entity.ConstructionWaste;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderProduct;
import com.autobon.order.entity.WorkDetail;
import com.autobon.order.service.*;
import com.autobon.order.vo.*;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.LocationStatusService;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wh on 2016/11/15.
 */

@RestController
@RequestMapping("/api/web/admin/v2/order")
public class OrderV2Controller {

    @Autowired
    OrderService orderService;
    @Autowired
    WorkDetailService workDetailService;
    @Autowired
    ConstructionService constructionService;
    @Autowired
    OrderProductService orderProductService;
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
        Integer statusCode = orderStatus != null ? orderStatus.getStatusCode() : null;

        if (!"orderTime".equals(sort) && !"id".equals(sort)) return new JsonMessage(false, "ILLEGAL_SORT_PARAM" , "sort参数只能为id或orderTime");
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
                        types, statusCode, sort, Sort.Direction.DESC, page, pageSize)));
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.POST)
    public JsonMessage update(
            @PathVariable("orderId") int orderId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "statusCode", required = false) Integer statusCode,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "remark", required = false)String remark,
            @RequestParam(value = "positionLon", required = false)String positionLon,
            @RequestParam(value = "positionLat", required = false)String positionLat,
            @RequestBody List<WorkDetail> workDetailShowList,
            @RequestBody List<ConstructionWaste> constructionWasteList) {
        JsonMessage msg = new JsonMessage(true);
        ArrayList<String> messages = new ArrayList<>();
        Order order = orderService.getbyOrderId(orderId);

        if(order == null){
            msg.setError("INVALID_ID");
            messages.add("没有此订单");
        }

        order.setType(type == null ? order.getType():type);
        order.setStatusCode(statusCode == null ? order.getStatusCode():statusCode.intValue());
        order.setContactPhone(phone == null ? order.getContactPhone():phone);
        order.setRemark(remark == null ? order.getRemark():remark);
        order.setPositionLon(positionLon == null ? order.getPositionLon():positionLon);
        order.setPositionLat(positionLat == null ? order.getPositionLat():positionLat);
        orderService.save(order);
        for(WorkDetail workDetailShow : workDetailShowList){
            WorkDetail curworkDetail = workDetailService.getByOderIdAndTechId(workDetailShow.getOrderId(),workDetailShow.getTechId());
            curworkDetail.setProject1(workDetailShow.getProject1() == null? curworkDetail.getProject1(): workDetailShow.getProject1());
            curworkDetail.setPosition1(workDetailShow.getPosition1() == null? curworkDetail.getPosition1(): workDetailShow.getPosition1());
            curworkDetail.setProject2(workDetailShow.getProject2() == null? curworkDetail.getProject2(): workDetailShow.getProject2());
            curworkDetail.setPosition2(workDetailShow.getPosition2() == null? curworkDetail.getPosition2(): workDetailShow.getPosition2());
            workDetailService.save(curworkDetail);
        }
        for(ConstructionWaste constructionWasteShow : constructionWasteList){
            ConstructionWaste curconstructionWaste = constructionWasteService.getByOrderIdAndTechId(constructionWasteShow.getOrderId(),constructionWasteShow.getTechId());
            curconstructionWaste.setProject(Integer.valueOf(constructionWasteShow.getProject()) == null ?curconstructionWaste.getProject(): constructionWasteShow.getProject());
            curconstructionWaste.setPosition(Integer.valueOf(constructionWasteShow.getPosition()) == null ?curconstructionWaste.getPosition(): constructionWasteShow.getPosition());
            curconstructionWaste.setTotal(Integer.valueOf(constructionWasteShow.getTotal()) == null ?curconstructionWaste.getTotal(): constructionWasteShow.getTotal());
            constructionWasteService.save(curconstructionWaste);
        }


        msg.setData(order);
     return msg;
    }

    @Autowired
    LocationStatusService locationStatusService;

    @Autowired
    CooperatorService cooperatorService;


    @Autowired
    ConstructionWasteService constructionWasteService;

    @Autowired
    TechnicianService technicianService;


    @Autowired
    ProductService productService;

    @RequestMapping(value = "/v2/{orderId:\\d+}", method = RequestMethod.GET)
    public JsonMessage getById(@PathVariable("orderId") int orderId){
        OrderShow orderShow = orderService.getByOrderId(orderId);
        List<WorkDetailShow> workDetailShowList = workDetailService.getByOrderId(orderId);
        for(WorkDetailShow workDetailShow: workDetailShowList){
            if(workDetailShow.getTechId() == orderShow.getTechId()){
                workDetailShow.setIsMainTech(1);
            }
        }
        List<ConstructionWasteShow> constructionWasteShows = constructionWasteService.getByOrderId(orderId);
        orderShow.setWorkDetailShows(workDetailShowList);
        orderShow.setConstructionWasteShows(constructionWasteShows);

        return new JsonMessage(true, "","",orderShow);
    }


    /**
     * 修改订单
     * @param orderId
     * @param type
     * @param statusCode
     * @param remark
     * @param positionLon
     * @param positionLat
     * @param agreedStartTime
     * @param agreedEndTime
     * @param orderConstructionWasteShow
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/v2/{orderId}", method = RequestMethod.PUT)
    public JsonResult modifyOrder(@PathVariable("orderId") int orderId,
                                  @RequestParam(value = "type", required = false) String type,
                                  @RequestParam(value = "statusCode", required = false) Integer statusCode,
                                  @RequestParam(value = "remark", required = false)String remark,
                                  @RequestParam(value = "positionLon", required = false)String positionLon,
                                  @RequestParam(value = "positionLat", required = false)String positionLat,
                                  @RequestParam(value = "agreedStartTime", required = false)String agreedStartTime,
                                  @RequestParam(value = "agreedEndTime", required = false)String agreedEndTime,
                                  @RequestBody OrderConstructionWasteShow orderConstructionWasteShow)throws Exception{

        Order order = orderService.get(orderId);
        order.setType(type == null? order.getType():type);
        order.setStatusCode(statusCode == null ? order.getStatusCode() : statusCode);
        order.setRemark(remark == null ? order.getRemark() : remark);
        order.setPositionLat(positionLat == null ? order.getPositionLat() : positionLat);
        order.setPositionLon(positionLon == null ? order.getPositionLon() : positionLon);
        order.setAgreedEndTime(agreedEndTime == null ? order.getAgreedEndTime() : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(agreedEndTime));
        order.setAgreedStartTime(agreedStartTime == null ? order.getStartTime() : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(agreedStartTime));

        orderService.save(order);
        if(orderConstructionWasteShow != null){
            List<WorkDetail> workDetailList = orderConstructionWasteShow.getWorkDetailList();
            workDetailService.save(workDetailList);
            List<ConstructionWaste> constructionWasteList = orderConstructionWasteShow.getConstructionWasteList();
            constructionWasteService.save(constructionWasteList);

        }

        return new JsonResult(true, "修改成功");
    }






    /**
     * 通过订单编号获取施工项目及对应的施工部位
     * @param request
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/project/position/{orderId}", method = RequestMethod.GET)
    public JsonMessage getProject(HttpServletRequest request,
                                 @PathVariable("orderId") int orderId) {

        try{
            Order order = orderService.get(orderId);

            if (order == null  ) {
                return new JsonMessage(false,  "没有这个订单");
            }
            return new JsonMessage(true,"","",orderService.getProject(orderId));

        }catch (Exception e){
            return new JsonMessage(false, e.getMessage());
        }
    }


    /**
     *
     * @param orderId
     * @param orderProductList
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
     * @param orderId
     * @param orderProductList
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
     *
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
     * @param orderId
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
    public JsonResult getTechnician(@RequestParam("query") String query,
                                    @RequestParam(value = "page",  defaultValue = "1" )  int page,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    HttpServletRequest request){

        try {
            Technician tech = (Technician) request.getAttribute("user");
            if (tech == null) {
                return new JsonResult(false, "登陆过期");
            }
            Page<Technician> technicians;
            String query1 = "%" + query + "%";
            if (Pattern.matches("\\d+", query)) {
                technicians = technicianService.find(query1, null, page, pageSize);

            } else {
                technicians = technicianService.find(null, query1, page, pageSize);
            }

            return new JsonResult(true, new JsonPage<>(technicians));
        }catch (Exception e){
            return  new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 后台指派技师
     * @param orderId
     * @param techId
     * @return
     */
    @RequestMapping(value = "/{orderId}/technician/{techId}/assign", method = RequestMethod.POST)
    public JsonResult assign(@PathVariable("orderId") int orderId,
                             @PathVariable("techId") int techId){

        Order order = orderService.get(orderId);
        if (order == null ) {
            return new JsonResult(false,  "没有这个订单");
        }
        order.setMainTechId(techId);
        orderService.save(order);
        return  new JsonResult(true,  "指派成功");
    }


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
        workDetailService.balance(list);
    }

}
