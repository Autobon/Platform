package com.autobon.platform.controller.merchandiser;

import com.autobon.merchandiser.entity.Merchandiser;
import com.autobon.merchandiser.service.MerchandiserCooperatorService;
import com.autobon.merchandiser.service.MerchandiserService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by wh on 2018/6/6.
 */
@RestController("webMerchandiserController")
@RequestMapping("/api/web/merchandiser")
public class MerchandiserController {


    @Autowired
    MerchandiserService merchandiserService;
    @Autowired
    MerchandiserCooperatorService merchandiserCooperatorService;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonMessage list( @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "status", required = false) Merchandiser.Status status,
                             @RequestParam(value = "page", defaultValue = "1" )  int page,
                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize){

        return new JsonMessage(true, "", "", new JsonPage<>(merchandiserService.find(
                phone, name, status, page, pageSize)));
    }



    @RequestMapping(value = "/cooperator/list", method = RequestMethod.GET)
    public JsonResult getCooperator(@RequestParam("id") int id) {



        return new JsonResult(true, merchandiserCooperatorService.findByMerchandiserId(id));

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
        public JsonMessage create(@RequestParam("phone") String phone,
                              @RequestParam("password") String password,
                              @RequestParam("name") String name,
                              @RequestParam("gender") String gender){


        if(merchandiserService.findByPhone(phone) != null){

            return new JsonMessage(false, "注册手机号已存在");
        }


        Merchandiser merchandiser = new Merchandiser();

        merchandiser.setPhone(phone);
        merchandiser.setPassword(Merchandiser.encryptPassword(password));
        merchandiser.setName(name);
        merchandiser.setGender(gender);
        merchandiser.setStatus(Merchandiser.Status.VERIFIED);
        merchandiser.setCreateAt(new Date());

        merchandiserService.save(merchandiser);
        return new JsonMessage(true, "新增成功");
    }

    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public JsonMessage update(@RequestParam("id") int id,
 //                             @RequestParam("phone") String phone,
                              @RequestParam("password") String password,
                              @RequestParam("name") String name,
                              @RequestParam("gender") String gender){


        Merchandiser merchandiser = merchandiserService.get(id);

        if(merchandiser == null){
            return new JsonMessage(false, "跟单员不存在");
        }

        if(name != null){
            merchandiser.setName(name);
        }

        if(password != null){
            merchandiser.setPassword(Merchandiser.encryptPassword(password));
        }

        if(gender != null){
            merchandiser.setGender(gender);
        }

        merchandiserService.save(merchandiser);

        return new JsonMessage(true, "修改成功");
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JsonMessage delete(@RequestParam("id") int id){

        merchandiserService.delete(id);
        return new JsonMessage(true, "删除成功");
    }


    @RequestMapping(value = "/ban", method = RequestMethod.PUT)
    public JsonMessage ban(@RequestParam("id") int id){

        Merchandiser merchandiser = merchandiserService.get(id);

        if(merchandiser == null){
            return new JsonMessage(false, "跟单员不存在");
        }

        merchandiser.setStatus(Merchandiser.Status.BANNED);

        merchandiserService.save(merchandiser);
        return new JsonMessage(true, "禁用成功");
    }

    @RequestMapping(value = "/unban", method = RequestMethod.PUT)
    public JsonMessage unban(@RequestParam("id") int id){

        Merchandiser merchandiser = merchandiserService.get(id);

        if(merchandiser == null){
            return new JsonMessage(false, "跟单员不存在");
        }

        merchandiser.setStatus(Merchandiser.Status.VERIFIED);

        merchandiserService.save(merchandiser);
        return new JsonMessage(true, "解除禁用成功");
    }


    @RequestMapping(value = "/cooperator", method = RequestMethod.POST)
    public JsonMessage createCooperator(@RequestParam("mid")  int mid,
                              @RequestParam("cid")  int cid){


        merchandiserService.createMerchandiserCooperator(mid, cid);
        return new JsonMessage(true, "新增成功");
    }

    @RequestMapping(value = "/cooperator", method = RequestMethod.DELETE)
    public JsonMessage deleteCooperator(@RequestParam("mid")  int mid,
                                        @RequestParam("cid")  int cid){



        merchandiserService.deleteMerchandiserCooperator(mid, cid);
        return new JsonMessage(true, "删除成功");
    }
}
