package com.autobon.platform.controller.pc;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderProduct;
import com.autobon.order.service.OrderProductService;
import com.autobon.order.service.OrderService;
import com.autobon.order.vo.CoopTechnicianLocation;
import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.LocationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

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
    CoopAccountService coopAccountService;

    @Autowired
    CooperatorService cooperatorService;

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

}
