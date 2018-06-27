package com.autobon.platform.controller.pc;

import com.autobon.order.entity.ConstructionPosition;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderProduct;
import com.autobon.order.entity.Product;
import com.autobon.order.service.*;
import com.autobon.order.vo.OrderProductShow;
import com.autobon.order.vo.OrderProductSuper;
import com.autobon.order.vo.ProductShow;
import com.autobon.order.vo.ProjectShow;
import com.autobon.shared.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wh on 2016/11/17.
 */

@RestController
@RequestMapping("/api/web/admin/order")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    @Autowired
    ConstructionProjectService constructionProjectService;
    @Autowired
    OrderProductService orderProductService;
    @Autowired
    WorkDetailService workDetailService;


    /**
     * 保存产品
     * @param orderId
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/{orderId}/product", method = RequestMethod.POST)
    public JsonResult saveProduct(@PathVariable("orderId") int orderId,
                                  @RequestParam(value = "productIds", required = true) String productIds,
                                  @RequestParam(value = "vehicleModel", required = false) String vehicleModel,
                                  @RequestParam(value = "realOrderNum", required = false) String realOrderNum,
                                  @RequestParam(value = "license", required = false) String license,
                                  @RequestParam(value = "vin", required = false) String vin,
                                  @RequestParam(value = "customerName", required = false) String customerName ,
                                  @RequestParam(value = "customerPhone", required = false) String customerPhone ,
                                  @RequestParam(value = "turnover", required = false) BigDecimal turnover ,
                                  @RequestParam(value = "salesman", required = false) String salesman){

        Order order = orderService.get(orderId);
        if(order == null){
            return new JsonResult(false,"订单不存在");
        }


        if(order.getProductStatus() == 1){
            return new JsonResult(false,"已经录入过产品，不能重复补录");
        }

        order.setVehicleModel(vehicleModel == null ? order.getVehicleModel():vehicleModel);
        order.setRealOrderNum(realOrderNum == null ? order.getRealOrderNum() : realOrderNum);
        order.setLicense(license == null ? order.getLicense() : license);
        order.setVin(vin == null ? order.getVin():vin);

        order.setCustomerName(customerName == null ? order.getCustomerName(): customerName);
        order.setCustomerPhone(customerPhone == null ? order.getCustomerPhone(): customerPhone);
        order.setTurnover(turnover == null ? order.getTurnover(): turnover);
        order.setSalesman(salesman == null ? order.getSalesman() : salesman);




        List<Integer> productId = new ArrayList<>();
        if(productIds!=null&&productIds.length()>0) {
            String[] pids = productIds.split(",");
            for(String pid: pids){
                productId.add(Integer.valueOf(pid));
            }

            List<OrderProduct> orderProducts = new ArrayList<>();
            for (Integer pId : productId) {
                Product product = productService.get(pId);
                if(product!=null) {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setOrderId(orderId);
                    orderProduct.setConstructionProjectId(product.getType());
                    orderProduct.setConstructionPositionId(product.getConstructionPosition());
                    orderProduct.setConstructionCommission(product.getConstructionCommission());
                    orderProduct.setProductId(pId);
                    orderProduct.setScrapCost(product.getScrapCost());

                    orderProducts.add(orderProduct);
                }
            }
            orderProductService.deleteByOrderId(orderId);
            orderProductService.batchInsert(orderProducts);
            order.setProductStatus(1);
            orderService.save(order);

            if(order.getStatusCode()>= Order.Status.FINISHED.getStatusCode()){
                workDetailService.balance(orderId);
            }

            return new JsonResult(true, order);
        }else{
            return new JsonResult(false, "没有提交产品");
        }
    }



    /**
     * 通过订单编号加载所有施工项目 施工部位 及对应的产品
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/{orderId}/product", method = RequestMethod.GET)
    public JsonResult productList(@PathVariable("orderId") int orderId){

        Order order = orderService.get(orderId);
        if(order == null){
            return new JsonResult(false,"订单不存在");
        }


        List<OrderProduct> orderProducts = orderProductService.get(orderId);
        OrderProductSuper orderProductSuper = new OrderProductSuper();
        if(orderProducts != null&& orderProducts.size()>0){
            String type = order.getType();
            String[] typeStr = type.split(",");
            List<Integer> projectList = new ArrayList<>();
            for(String projectId: typeStr){
                projectList.add(Integer.valueOf(projectId));
            }


            orderProductSuper.setOrderId(orderId);
            orderProductSuper.setPhoto(order.getPhoto());
            orderProductSuper.setOrderNum(order.getOrderNum());
            List<ProjectShow> projectShows = new ArrayList<>();
            Map<Integer, String> projectMap = constructionProjectService.getProject();
            Map<Integer, String> positionMap = constructionProjectService.getPosition();
            List<Product> products = productService.getByTypes(projectList);
            for(Integer projectId: projectList){
                ProjectShow projectShow = new ProjectShow();
                projectShow.setProjectId(projectId);
                projectShow.setProjectName(projectMap.get(projectId));
                List<ConstructionPosition> list = constructionProjectService.findByProject2(projectId);
                List<OrderProductShow> orderProductShows = new ArrayList<>();
                for(ConstructionPosition constructionPosition: list) {
                    List<ProductShow> products1  = new ArrayList<>();
                    OrderProductShow orderProductShow = new OrderProductShow();
                    orderProductShow.setPositionId(constructionPosition.getId());
                    orderProductShow.setPositionName(positionMap.get(constructionPosition.getId()));
                    for(OrderProduct orderProduct: orderProducts) {
                        if(orderProduct.getConstructionProjectId() == projectId && orderProduct.getConstructionPositionId() == constructionPosition.getId()){
                            orderProductShow.setProductId(orderProduct.getProductId());
                        }

                    }
                    for (Product product : products) {
                        if (product.getType() == projectId && product.getConstructionPosition() == constructionPosition.getId()) {
                            products1.add(new ProductShow(product));
                        }
                    }
                    orderProductShow.setProductList(products1);
                    orderProductShows.add(orderProductShow);

                }
                projectShow.setProductShowList(orderProductShows);
                projectShows.add(projectShow);
                orderProductSuper.setProject(projectShows);


            }

        }else {

            String type = order.getType();
            String[] typeStr = type.split(",");
            List<Integer> projectList = new ArrayList<>();
            for (String projectId : typeStr) {
                projectList.add(Integer.valueOf(projectId));
            }
            orderProductSuper.setOrderId(orderId);
            orderProductSuper.setOrderNum(order.getOrderNum());
            List<ProjectShow> projectShows = new ArrayList<>();
            Map<Integer, String> projectMap = constructionProjectService.getProject();
            Map<Integer, String> positionMap = constructionProjectService.getPosition();
            List<Product> products = productService.getByTypes(projectList);
            for (Integer projectId : projectList) {
                ProjectShow projectShow = new ProjectShow();
                projectShow.setProjectId(projectId);
                projectShow.setProjectName(projectMap.get(projectId));
                List<ConstructionPosition> list = constructionProjectService.findByProject2(projectId);
                List<OrderProductShow> orderProductShows = new ArrayList<>();
                for (ConstructionPosition constructionPosition : list) {
                    List<ProductShow> products1 = new ArrayList<>();
                    OrderProductShow orderProductShow = new OrderProductShow();
                    orderProductShow.setPositionId(constructionPosition.getId());
                    orderProductShow.setPositionName(positionMap.get(constructionPosition.getId()));
                    for (Product product : products) {
                        if (product.getType() == projectId && product.getConstructionPosition() == constructionPosition.getId()) {
                            products1.add(new ProductShow(product));
                        }
                    }
                    orderProductShow.setProductList(products1);
                    orderProductShows.add(orderProductShow);

                }
                projectShow.setProductShowList(orderProductShows);
                projectShows.add(projectShow);
                orderProductSuper.setProject(projectShows);

            }
        }

        return new JsonResult(true ,orderProductSuper);
    }

    /**
     * 查询产品列表
     * @param type 施工项目
     * @param brand 品牌
     * @param code 编码
     * @param model 型号
     * @param constructionPosition 施工部位
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public JsonResult list( @RequestParam(value = "type", required=false) Integer type,
                            @RequestParam(value = "brand", required=false) String brand,
                            @RequestParam(value = "code", required=false) String code,
                            @RequestParam(value = "model", required= false) String model,
                            @RequestParam(value = "constructionPosition", required=false) Integer constructionPosition,
                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize){


        return new JsonResult(true, productService.find(type, brand, code, model, constructionPosition, page ,pageSize));
    }


    /***
     * 查询产品详情
     * @param pid 产品ID
     * @return
     */
    @RequestMapping(value = "/product/{pid}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("pid") int pid){
        Product product = productService.get(pid);

        if(product!= null){
            return new JsonResult(true,product);
        }

        return new JsonResult(false, "产品不存在");
    }


    /**
     * 新增产品
     * @param type 施工项目
     * @param brand 品牌
     * @param code 编码
     * @param model 型号
     * @param constructionPosition 施工部位
     * @param workingHours 工时
     * @param constructionCommission 施工提成
     * @param starLevel 星级要求
     * @param scrapCost 报废扣款
     * @param warranty 质保
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public JsonResult add(@RequestParam(value = "type", required=false) Integer type,
                          @RequestParam(value = "brand", required=false) String brand,
                          @RequestParam(value = "code", required=false) String code,
                          @RequestParam(value = "model", required= false) String model,
                          @RequestParam(value = "constructionPosition", required=false) Integer constructionPosition,
                          @RequestParam(value = "workingHours", required=false) Integer workingHours,
                          @RequestParam(value = "constructionCommission", required=false) Integer constructionCommission,
                          @RequestParam(value = "starLevel", required= false) Integer starLevel,
                          @RequestParam(value = "scrapCost", required=false) Integer scrapCost,
                          @RequestParam(value = "warranty", required=false) Integer warranty){
        Product product = new Product();
        product.setType(type);
        product.setBrand(brand);
        product.setCode(code);
        product.setModel(model);
        product.setConstructionPosition(constructionPosition);
        product.setWorkingHours(workingHours);
        product.setConstructionCommission(constructionCommission);
        product.setStarLevel(starLevel);
        product.setScrapCost(scrapCost);
        product.setWarranty(warranty);
        return new JsonResult(true, productService.save(product));
    }

    /**
     * 修改产品
     * @param pid
     * @param type 施工项目
     * @param brand 品牌
     * @param code 编码
     * @param model 型号
     * @param constructionPosition 施工部位
     * @param workingHours 工时
     * @param constructionCommission 施工提成
     * @param starLevel 星级要求
     * @param scrapCost 报废扣款
     * @param warranty 质保
     * @return
     */
    @RequestMapping(value = "/product/{pid}", method = RequestMethod.POST)
    public JsonResult modify(@PathVariable("pid") int pid,
                             @RequestParam(value = "type", required=false) Integer type,
                             @RequestParam(value = "brand", required=false) String brand,
                             @RequestParam(value = "code", required=false) String code,
                             @RequestParam(value = "model", required= false) String model,
                             @RequestParam(value = "constructionPosition", required=false) Integer constructionPosition,
                             @RequestParam(value = "workingHours", required=false) Integer workingHours,
                             @RequestParam(value = "constructionCommission", required=false) Integer constructionCommission,
                             @RequestParam(value = "starLevel", required= false) Integer starLevel,
                             @RequestParam(value = "scrapCost", required=false) Integer scrapCost,
                             @RequestParam(value = "warranty", required=false) Integer warranty){

        Product product = productService.get(pid);
        product.setType(type == null? product.getType(): type);
        product.setBrand(brand == null?product.getBrand(): brand);
        product.setCode(code == null? product.getCode(): code);
        product.setModel(model == null?product.getModel():model);
        product.setConstructionPosition(constructionPosition == null? product.getConstructionPosition(): constructionPosition);
        product.setWorkingHours(workingHours == null? product.getWorkingHours():workingHours);
        product.setConstructionCommission(constructionCommission == null?product.getConstructionCommission():constructionCommission);
        product.setStarLevel(starLevel == null?product.getStarLevel():starLevel);
        product.setScrapCost(scrapCost == null? product.getScrapCost(): scrapCost);
        product.setWarranty(warranty == null? product.getWarranty(): warranty);
        return new JsonResult(true, productService.save(product));
    }

    /**
     * 删除产品
     * @param pid
     * @return
     */
    @RequestMapping(value = "/product/{pid}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("pid") int pid){

        return new JsonResult(true, productService.delete(pid));
    }





}
