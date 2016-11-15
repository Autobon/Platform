package com.autobon.platform.controller.pc;

import com.autobon.order.entity.Product;
import com.autobon.order.service.ConstructionProjectService;
import com.autobon.shared.ImportExcelUtil;
import com.autobon.shared.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
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

    @RequestMapping(value = "/project/upload", method = RequestMethod.POST)
    public JsonResult uploadProject(HttpServletRequest request)throws Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        System.out.println("通过传统方式form表单提交方式导入excel文件！");

        InputStream in =null;
        List<List<Object>> listob = null;
        MultipartFile file = multipartRequest.getFile("upfile");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        in.close();

        Map<String, Integer> projectMap = constructionProjectService.getProject1();
        Map<String, Integer> positionMap = constructionProjectService.getPosition1();

        //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出
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
            product.setWarranty(Integer.valueOf(String.valueOf(lo.get(9) == null? 0:String.valueOf(lo.get(9)))));

            System.out.println(product.toString());


        }
        return new JsonResult(true, "上传成功");
    }


    @RequestMapping(value = "/project/download", method = RequestMethod.GET)
    public JsonResult downLoad(){

        return null;
    }
}
