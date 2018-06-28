package com.autobon.platform.controller.admin;

import com.autobon.shared.JsonMessage;
import com.autobon.staff.entity.FunctionCategory;
import com.autobon.staff.entity.Menu;
import com.autobon.staff.entity.Staff;
import com.autobon.staff.service.FunctionCategoryService;
import com.autobon.staff.service.MenuService;
import com.autobon.staff.service.RoleService;
import com.autobon.staff.vo.MenuShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wh on 2018/6/22.
 */
@RestController
@RequestMapping("/api/web/admin")
public class RoleController {


    @Autowired
    FunctionCategoryService functionCategoryService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleService roleService;

    @RequestMapping(value = "/function/category/menu/{menuId}", method = RequestMethod.GET)
    public JsonMessage menu(@PathVariable("menuId") int menuId) {

        return new JsonMessage(true, "", "", functionCategoryService.findByMenuId(menuId));
    }

    @RequestMapping(value = "/function/category/menu", method = RequestMethod.GET)
    public JsonMessage menu1() {

        List<MenuShow> menuShowList  = new ArrayList<>();

        List<Menu> menus = menuService.findMenus();
        for(Menu menu : menus){
            List<FunctionCategory> functionCategories = functionCategoryService.findByMenuId(menu.getId());
            menuShowList.add(new MenuShow(menu, functionCategories));
        }

        return new JsonMessage(true, "", "",menuShowList);
    }


    @RequestMapping(value = "/staff/role", method = RequestMethod.GET)
    public JsonMessage role(HttpServletRequest request) {

        Staff staff = (Staff) request.getAttribute("user");
        
        return new JsonMessage(true, "", "", roleService.findByUserId(staff.getId()));
    }



}
