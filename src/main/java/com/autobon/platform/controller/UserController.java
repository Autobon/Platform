package com.autobon.platform.controller;

import com.autobon.platform.utils.JsonMessage;
import com.autobon.technician.entity.Technician;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liz on 2016/2/18.
 */
@RestController
@RequestMapping("/api/staff")
public class UserController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonMessage login(HttpServletResponse response,
                                       @RequestParam("account")    String account,
                                       @RequestParam("password") String password) {

        JsonMessage ret = new JsonMessage(true);

        return ret;
    }
}
