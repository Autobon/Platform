package com.autobon.platform.controller.pc;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.Product;
import com.autobon.order.service.ConstructionProjectService;
import com.autobon.order.service.OrderService;
import com.autobon.order.service.ProductService;
import com.autobon.order.vo.OrderProductShow;
import com.autobon.shared.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/{orderId}/product", method = RequestMethod.GET)
    public JsonResult productList(@PathVariable("orderId") int orderId){

        Order order = orderService.get(orderId);
        if(order == null){
            return new JsonResult(false,"订单不存在");
        }

        String type = order.getType();
        String[] typeStr = type.split(",");
        List<Integer> projectList = new ArrayList<>();
        for(String projectId: typeStr){
            projectList.add(Integer.valueOf(projectId));
        }

        List<OrderProductShow> orderProductShows = new ArrayList<>();
        Map<Integer, String> projectMap = constructionProjectService.getProject();
        Map<Integer, String> positionMap = constructionProjectService.getPosition();
        List<Product> products = productService.getByType(projectList);
        for(Integer projectId: projectList){
            OrderProductShow orderProductShow = new OrderProductShow();
            orderProductShow.setProject(projectId);
            List<Product> products1  = new ArrayList<>();
            for(Product product: products){
                if(product.getType() == projectId){
                    products1.add(product);
                }
            }

            orderProductShow.setProductList(products1);
            orderProductShows.add(orderProductShow);
        }


        return new JsonResult(true ,orderProductShows);
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
