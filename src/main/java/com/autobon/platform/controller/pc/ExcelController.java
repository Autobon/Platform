package com.autobon.platform.controller.pc;

import com.autobon.order.entity.Product;
import com.autobon.order.service.ConstructionProjectService;
import com.autobon.order.service.ProductService;
import com.autobon.shared.ImportExcelUtil;
import com.autobon.shared.JsonResult;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wh on 2016/11/14.
 */
@RestController
@RequestMapping("/api/web/admin/order")
public class ExcelController {


    @Autowired
    ConstructionProjectService constructionProjectService;

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/project/upload", method = RequestMethod.POST)
    public JsonResult uploadProject(HttpServletRequest request)throws Exception{

    //    productService.deleteAll();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        InputStream in =null;
        List<List<Object>> listob = null;
        MultipartFile file = multipartRequest.getFile("upfile");
        if(file.isEmpty()){
            throw new Exception("文件为空");
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        in.close();

        Map<String, Integer> projectMap = constructionProjectService.getProject1();
        Map<String, Integer> positionMap = constructionProjectService.getPosition1();

        List<Product> list = new ArrayList<>();

        for (int i = 0; i < listob.size(); i++) {
            List<Object> lo = listob.get(i);
            Product product = new Product();
            product.setType(projectMap.get(String.valueOf(lo.get(0)).trim()));
            product.setBrand(String.valueOf(lo.get(1)));
            product.setCode(String.valueOf(lo.get(2)));
            product.setModel(String.valueOf(lo.get(3)));
            product.setConstructionPosition(positionMap.get(String.valueOf(lo.get(4)).trim()));
            product.setWorkingHours(Integer.valueOf(String.valueOf(lo.get(5) == null ? 0 : String.valueOf(lo.get(5)))));
            product.setConstructionCommission(Integer.valueOf(String.valueOf(lo.get(6) == null ? 0 : String.valueOf(lo.get(6)))));
            product.setStarLevel(Integer.valueOf(String.valueOf(lo.get(7) == null ? 0 : String.valueOf(lo.get(7)))));
            product.setScrapCost(Integer.valueOf(String.valueOf(lo.get(8) == null ? 0 : String.valueOf(lo.get(8)))));
            product.setWarranty(Integer.valueOf(String.valueOf(lo.get(9) == null ? 0 : String.valueOf(lo.get(9)))));
            list.add(product);
            System.out.println(product.toString());


        }

        productService.batchInsert(list);
        return new JsonResult(true, "文件上传成功");
    }


    @RequestMapping(value = "/project/download", method = RequestMethod.GET)
    public JsonResult downLoad(HttpServletResponse response){


        return null;
    }



}
