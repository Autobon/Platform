package com.autobon.platform.controller.admin;

import com.autobon.shared.JsonMessage;
import com.autobon.staff.service.FunctionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wh on 2018/6/22.
 */
@RestController
@RequestMapping("/api/web/admin")
public class RoleController {


    @Autowired
    FunctionCategoryService functionCategoryService;

    @RequestMapping(value = "/function/category/menu/{menuId}", method = RequestMethod.GET)
    public JsonMessage logout(@PathVariable("menuId") int menuId) {

        return new JsonMessage(true, "", "", functionCategoryService.findByMenuId(menuId));
    }

}
