package com.autobon.platform.controller.admin;

import com.autobon.shared.JsonMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wh on 2018/6/22.
 */
@RestController
@RequestMapping("/api/web/admin")
public class RoleController {


    @RequestMapping(value = "/function/category/menu/{menuId}", method = RequestMethod.GET)
    public JsonMessage logout() {

        return null;
    }

}
