package com.autobon.platform.controller.staff;

import com.autobon.shared.JsonMessage;
import com.autobon.staff.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dave on 16/3/1.
 */
@RestController
@RequestMapping("/api/staff")
public class StaffAccountController {
    @Autowired StaffService staffService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonMessage register(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {



        return null;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public JsonMessage login(HttpServletRequest request,
             @RequestParam("username") String username,
             @RequestParam("password") String password) {

        return null;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public JsonMessage changePassword(HttpServletRequest request,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {

        return null;
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public JsonMessage resetPassword(HttpServletRequest request,
             @RequestParam(value = "phone", required = false) String phone,
             @RequestParam(value = "email", required = false) String email) {

        return null;
    }


    @RequestMapping(value = "/changeEmail", method = RequestMethod.POST)
    public JsonMessage changeEmail(HttpServletRequest request,
            @RequestParam("email") String email) {

        return null;
    }

    @RequestMapping(value = "/changePhone", method = RequestMethod.POST)
    public JsonMessage changePhone(HttpServletRequest request,
                                   @RequestParam("phone") String phone) {

        return null;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public JsonMessage updateProfile(HttpServletRequest request,
             @RequestParam(value = "name", required = false) String name,
             @RequestParam(value = "username", required = false) String username) {

        return null;
    }
}
