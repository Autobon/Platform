package com.autobon.platform.controller.admin;

import com.autobon.shared.JsonMessage;
import com.autobon.staff.entity.Staff;
import com.autobon.staff.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController
@RequestMapping("/api/web/admin")
public class StaffAccountController {
    @Autowired StaffService staffService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonMessage register(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        if (!Pattern.matches("^[A-Za-z][0-9A-Za-z_]{5,}$", username)) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "用户名至少6个字符, 只能以字母开头, 只能使用数字和字母及下划线");
        } else if (staffService.findByUsername(username) != null) {
            return new JsonMessage(false, "OCCUPIED_USERNAME", "用户名已被占用");
        } else if (staffService.findByEmail(email) != null) {
            return new JsonMessage(false, "OCCUPIED_EMAIL", "邮箱已被占用");
        } else if (password.length() < 6) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "密码至少6个字符");
        }

        Staff staff = new Staff();
        staff.setUsername(username);
        staff.setPassword(Staff.encryptPassword(password));
        staff.setEmail(email);
        staffService.save(staff);
        return new JsonMessage(true, "", "", staff);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public JsonMessage login(HttpServletResponse response,
             @RequestParam("username") String username,
             @RequestParam("password") String password) {
        Staff staff = null;
        if (Pattern.matches("[0-9]{11}", username)) {
            staff = staffService.findByPhone(username);
        } else if (Pattern.matches("[\\w.\\-]+@([\\w\\-]+\\.)+[a-zA-Z]+", username)) {
            staff = staffService.findByEmail(username);
        } else {
            staff = staffService.findByUsername(username);
        }

        if (staff == null) return new JsonMessage(false, "NO_SUCH_USER", "用户不存在");
        else if (!staff.getPassword().equals(Staff.encryptPassword(password))) {
            return new JsonMessage(false, "PASSWORD_MISMATCH", "密码错误");
        } else {
            response.addCookie(new Cookie("autoken", Staff.makeToken(staff.getId())));
            return new JsonMessage(true, "", "", staff);
        }
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public JsonMessage changePassword(HttpServletRequest request,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {

        // TODO 修改密码
        return null;
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public JsonMessage resetPassword(HttpServletRequest request,
             @RequestParam(value = "phone", required = false) String phone,
             @RequestParam(value = "email", required = false) String email) {

        // TODO 重置密码
        return null;
    }


    @RequestMapping(value = "/changeEmail", method = RequestMethod.POST)
    public JsonMessage changeEmail(HttpServletRequest request,
            @RequestParam("email") String email) {

        // TODO 修改邮箱
        return null;
    }

    @RequestMapping(value = "/changePhone", method = RequestMethod.POST)
    public JsonMessage changePhone(HttpServletRequest request,
                                   @RequestParam("phone") String phone) {

        // TODO 修改手机号
        return null;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public JsonMessage updateProfile(HttpServletRequest request,
             @RequestParam(value = "name", required = false) String name,
             @RequestParam(value = "username", required = false) String username) {

        // TODO 更新个人信息
        return null;
    }
}
