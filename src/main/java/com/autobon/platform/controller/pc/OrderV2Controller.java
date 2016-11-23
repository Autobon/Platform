package com.autobon.platform.controller.pc;


import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.order.entity.ConstructionWaste;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderProduct;
import com.autobon.order.entity.WorkDetail;
import com.autobon.order.service.ConstructionWasteService;
import com.autobon.order.service.OrderProductService;
import com.autobon.order.service.OrderService;
import com.autobon.order.service.WorkDetailService;
import com.autobon.order.vo.*;
import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.LocationStatus;
import com.autobon.technician.service.LocationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    @RequestMapping(value = "/v2/{orderId}", method = RequestMethod.GET)
    public JsonResult getById(@PathVariable("orderId") int orderId){
        OrderShow orderShow = orderService.getByOrderId(orderId);
        List<WorkDetailShow> workDetailShowList = workDetailService.getByOrderId(orderId);
        List<ConstructionWasteShow> constructionWasteShows = constructionWasteService.getByOrderId(orderId);
        orderShow.setWorkDetailShows(workDetailShowList);
        orderShow.setConstructionWasteShows(constructionWasteShows);

        return new JsonResult(true, orderShow);
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
        order.setAgreedEndTime(agreedEndTime == null ? order.getAgreedEndTime() : new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(agreedEndTime));
        order.setAgreedStartTime(agreedStartTime == null ? order.getStartTime() : new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(agreedStartTime));

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
        workDetail.setPosition2("3,4");

        list.add(workDetail);
        workDetailService.balance(list);
    }

}
