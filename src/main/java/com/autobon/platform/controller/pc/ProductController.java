package com.autobon.platform.controller.pc;

import com.autobon.order.entity.Product;
import com.autobon.order.service.ProductService;
import com.autobon.shared.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by wh on 2016/11/17.
 */

@RestController
@RequestMapping("/api/web/admin/order")
public class ProductController {

    @Autowired
    ProductService productService;


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
     * @param product
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public JsonResult add(@RequestBody Product product){

        return new JsonResult(true, productService.save(product));
    }

    /**
     * 修改产品
     * @param pid
     * @param product
     * @return
     */
    @RequestMapping(value = "/product/{pid}", method = RequestMethod.PUT)
    public JsonResult modify(@PathVariable("pid") int pid,
                             @RequestBody Product product){
        product.setId(pid);
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
