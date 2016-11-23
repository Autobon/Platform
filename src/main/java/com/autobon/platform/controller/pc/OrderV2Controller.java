package com.autobon.platform.controller.pc;


import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderProduct;
import com.autobon.order.entity.WorkDetail;
import com.autobon.order.service.*;
import com.autobon.order.vo.*;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.LocationStatus;
import com.autobon.technician.service.LocationStatusService;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    @Autowired
    TechnicianService technicianService;
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

    @RequestMapping(value = "/{orderId}", method = RequestMethod.PUT)
    public JsonMessage update(
            @PathVariable("orderId") int orderId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "statusCode", required = false) int statusCode,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "remark", required = false)String remark,
            @RequestParam(value = "positionLon", required = false)String positionLon,
            @RequestParam(value = "positionLat", required = false)String positionLat,
            @RequestBody List<WorkDetail> workDetailShowList) {
        JsonMessage msg = new JsonMessage(true);
        ArrayList<String> messages = new ArrayList<>();
        Order order = orderService.getbyOrderId(orderId);

        if(order == null){
            msg.setError("INVALID_ID");
            messages.add("没有此订单");
        }

        order.setType(type == null ? order.getType():type);
        order.setStatusCode(Integer.valueOf(statusCode) == null ? order.getStatusCode():statusCode);
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


        msg.setData(order);
     return msg;
    }

    @Autowired
    LocationStatusService locationStatusService;

    @Autowired
    CoopAccountService coopAccountService;

    @Autowired
    CooperatorService cooperatorService;


    @Autowired
    ConstructionWasteService constructionWasteService;


    @RequestMapping(value = "/v2/{orderId:\\d+}", method = RequestMethod.GET)
    public JsonMessage getById(@PathVariable("orderId") int orderId){
        OrderShow orderShow = orderService.getByOrderId(orderId);
        List<WorkDetailShow> workDetailShowList = workDetailService.getByOrderId(orderId);
        List<ConstructionWasteShow> constructionWasteShows = constructionWasteService.getByOrderId(orderId);
        orderShow.setWorkDetailShows(workDetailShowList);
        orderShow.setConstructionWasteShows(constructionWasteShows);

        return new JsonMessage(true, "","",orderShow);
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


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(){

        List<WorkDetail> list = new ArrayList<>();

        WorkDetail  workDetail = new WorkDetail();
        workDetail.setOrderId(1);
        workDetail.setProject1(1);
        workDetail.setPosition1("1,2");
        workDetail.setProject2(2);
        workDetail.setPosition1("3,4");

        list.add(workDetail);
        workDetailService.balance(list);
    }

}
