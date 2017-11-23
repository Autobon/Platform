package com.autobon.platform.controller.admin;

import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.RedisCache;
import com.autobon.shared.VerifyCode;
import com.autobon.staff.entity.*;
import com.autobon.staff.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController
@RequestMapping("/api/web/admin")
public class StaffAccountController {
    @Autowired StaffService staffService;
    @Autowired RedisCache redisCache;
    @Autowired RoleStaffService roleStaffService;
    @Autowired RoleService roleService;
    @Autowired MenuService menuService;
    @Autowired RoleMenuService roleMenuService;

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
    public JsonMessage login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        Staff staff;
        if (Pattern.matches("[0-9]{11}", username)) {
            staff = staffService.findByPhone(username);
        } else if (Pattern.matches("[\\w.\\-]+@([\\w\\-]+\\.)+[a-zA-Z]+", username)) {
            staff = staffService.findByEmail(username);
        } else {
            staff = staffService.findByUsername(username);
        }

        if (staff == null) {
            return new JsonMessage(false, "NO_SUCH_USER", "用户不存在");
        } else if (!staff.getPassword().equals(Staff.encryptPassword(password))) {
            return new JsonMessage(false, "PASSWORD_MISMATCH", "密码错误");
        } else {
            String sessionId = VerifyCode.generateVerifyCode(6);
            redisCache.set("SSESSION:" + staff.getId() + "@" + sessionId, "" + staff.getId(), 3600);
            staff.setLastLoginAt(new Date());
            staff.setLastLoginIp(request.getRemoteAddr());
            staffService.save(staff);

            Cookie c = new Cookie("autoken", Staff.makeToken(staff.getId()) + "@" + sessionId);
            c.setPath("/");
            c.setHttpOnly(true);
            response.addCookie(c);

            StaffMenu staffMenu = roleStaffService.findMenuByStaffId(staff.getId());
            Cookie c0 = new Cookie("ro", String.valueOf(staff.getId()));
            c0.setPath("/");
            response.addCookie(c0);

            return new JsonMessage(true, "", "", staffMenu);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public JsonMessage logout(
            HttpServletRequest request) {
        Staff staff = (Staff) request.getAttribute("user");

        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if (cookies != null) {
            cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("autoken")).findFirst().orElse(null);
        }
        if (cookie != null) {
            String[] arr = cookie.getValue().split("@");
            if (arr.length > 1) {
                redisCache.delete("SSESSION:" + staff.getId() + "@" + arr[1]);
            }
        }

        SecurityContextHolder.clearContext();
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public JsonMessage changePassword(
            HttpServletRequest request,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {
        Staff staff = (Staff) request.getAttribute("user");

        if (oldPassword.equals(newPassword)) {
            return new JsonMessage(false, "SAME_PASSWORD", "新旧密码不能相同");
        } else if (!staff.getPassword().equals(Staff.encryptPassword(oldPassword))) {
            return new JsonMessage(false, "PASSWORD_MISMATCH", "密码错误");
        } else {
            staff.setPassword(Staff.encryptPassword(newPassword));
            staffService.save(staff);
            return new JsonMessage(true);
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public JsonMessage resetPassword(
            HttpServletRequest request,
            @RequestParam("phone") String phone,
            @RequestParam("verifyCode") String verifyCode) {

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
    public JsonMessage changePhone(
            HttpServletRequest request,
            @RequestParam("phone") String phone,
            @RequestParam("verifyCode") String verifyCode) {

        // TODO 修改手机号
        return null;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public JsonMessage updateProfile(
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "username", required = false) String username) {

        // TODO 更新个人信息
        return null;
    }

    /**
     * 查询用户列表
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/staff", method = RequestMethod.GET)
    public JsonMessage searchStaff(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        return new JsonMessage(true, "", "", new JsonPage<>(staffService.findStaffs(name, page, pageSize)));
    }

    /**
     * 新增用户
     * @param rid
     * @param username
     * @param email
     * @param name
     * @param phone
     * @param password
     * @return
     */
    @Transactional
    @RequestMapping(value = "/staff/{rid}/add", method = RequestMethod.POST)
    public JsonMessage register(@PathVariable("rid") int rid,
                                @RequestParam("username") String username,
                                @RequestParam("email") String email,
                                @RequestParam("name") String name,
                                @RequestParam("phone") String phone,
                                @RequestParam("password") String password) {

        if (!Pattern.matches("^[A-Za-z][0-9A-Za-z_]{5,}$", username)) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "用户名至少6个字符, 只能以字母开头, 只能使用数字和字母及下划线");
        } else if (staffService.findByUsername(username) != null) {
            return new JsonMessage(false, "OCCUPIED_USERNAME", "用户名已被占用");
        } else if (staffService.findByPhone(phone) != null) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "手机号已被占用");
        } else if (!Pattern.matches("^[0-9]{11}$", phone)) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "请输入正确的11位手机号");
        } else if (staffService.findByEmail(email) != null) {
            return new JsonMessage(false, "OCCUPIED_EMAIL", "邮箱已被占用");
        } else if (password.length() < 6) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "密码至少6个字符");
        }

        Staff staff = new Staff();
        staff.setUsername(username);
        staff.setPassword(Staff.encryptPassword(password));
        staff.setEmail(email);
        staff.setName(name);
        staff.setPhone(phone);
        Staff res = staffService.save(staff);

        RoleStaff roleStaff = new RoleStaff();
        roleStaff.setRoleId(rid);
        roleStaff.setStaffId(res.getId());
        roleStaffService.save(roleStaff);
        return new JsonMessage(true, "", "", staff);
    }

    /**
     * 修改用户
     * @param rid
     * @param username
     * @param email
     * @param name
     * @param phone
     * @return
     */
    @Transactional
    @RequestMapping(value = "/staff/{rid}/add/{id}", method = RequestMethod.POST)
    public JsonMessage register(@PathVariable("rid") int rid, @PathVariable("id") int id,
                                @RequestParam("username") String username,
                                @RequestParam("email") String email,
                                @RequestParam("name") String name,
                                @RequestParam("phone") String phone) {
        Staff staff = staffService.get(id);
        if (staff == null) {
            return new JsonMessage(false, "", "用户不存在");
        }
        if(id == 1 || staff.getUsername().equals("admin")){
            return new JsonMessage(false, "", "不能修改admin！");
        }
        if (!Pattern.matches("^[A-Za-z][0-9A-Za-z_]{5,}$", username)) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "用户名至少6个字符, 只能以字母开头, 只能使用数字和字母及下划线");
        } else if (staffService.findByUsername(username) != null && !username.equals(staff.getUsername())) {
            return new JsonMessage(false, "OCCUPIED_USERNAME", "用户名已被占用");
        } else if (staffService.findByPhone(phone) != null && !phone.equals(staff.getPhone())) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "手机号已被占用");
        } else if (!Pattern.matches("^[0-9]{11}$", phone)) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "请输入正确的11位手机号");
        } else if (staffService.findByEmail(email) != null && !email.equals(staff.getEmail())) {
            return new JsonMessage(false, "OCCUPIED_EMAIL", "邮箱已被占用");
        }

        staff.setUsername(username == null ? staff.getUsername() : username);
        staff.setEmail(email == null ? staff.getEmail() : email);
        staff.setName(name == null ? staff.getName() : name);
        staff.setPhone(phone == null ? staff.getPhone() : phone);
        Staff res = staffService.save(staff);

        RoleStaff roleStaff = roleStaffService.findByStaffId(id);
        if(roleStaff == null){
            roleStaff = new RoleStaff();
        }
        roleStaff.setRoleId(rid);
        roleStaff.setStaffId(res.getId());
        roleStaffService.save(roleStaff);
        return new JsonMessage(true, "", "", staff);
    }

    /**
     * 查询单个用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/staff/{id}", method = RequestMethod.GET)
    public JsonMessage searchOneStaff(@PathVariable("id") int id){
        Staff staff = staffService.get(id);
        StaffView staffView = new StaffView(staff);
        RoleStaff roleStaff = roleStaffService.findByStaffId(id);
        if(roleStaff != null){
            staffView.setRoleId(roleStaff.getRoleId());
        }
        return new JsonMessage(true, "", "", staffView);
    }

    /**
     * 查询单个用户和菜单
     * @param id
     * @return
     */
    @RequestMapping(value = "/staff/{id}/menu", method = RequestMethod.GET)
    public JsonMessage searchOneStaffMenu(@PathVariable("id") int id){
        return new JsonMessage(true, "", "", roleStaffService.findMenuByStaffId(id));
    }

    /**
     * 删除单个用户
     * @param id
     * @return
     */
    @Transactional
    @RequestMapping(value = "/staff/{id}", method = RequestMethod.DELETE)
    public JsonMessage deleteOneStaff(@PathVariable("id") int id) throws Exception {
        Staff staff = staffService.get(id);
        if(staff == null){

            return new JsonMessage(false, "", "用户不存在");
        }
        if(id == 1 || staff.getUsername().equals("admin")){
            return new JsonMessage(false, "", "不能删除admin！");
        }
        staffService.deleteStaff(id);
        RoleStaff roleStaff = roleStaffService.findByStaffId(staff.getId());
        if(roleStaff != null){
            roleStaffService.deleteOne(roleStaff.getId());
        }
        return new JsonMessage(true, "", "", "删除成功");
    }

    /**
     * 查询角色列表
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public JsonMessage searchRole(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        return new JsonMessage(true, "", "", new JsonPage<>(roleService.findRoles(name, page, pageSize)));
    }

    /**
     * 查询单个
     * @param id
     * @return
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public JsonMessage searchOne(@PathVariable("id") int id){
        return new JsonMessage(true, "", "", roleService.findById(id));
    }

    /**
     * 新增
     * @param name
     * @param menus
     * @param remark
     * @return
     */
    @Transactional
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public JsonMessage addRole(@RequestParam("name") String name,
                               @RequestParam(value = "menus", required = false) String menus,
                               @RequestParam(value = "remark", required = false) String remark){
        Role role = roleService.findByName(name);
        if(role != null){
            return new JsonMessage(false, "", "角色名称已存在");
        }
        Role r = new Role();
        r.setName(name);
        r.setRemark(remark);
        Role res = roleService.save(r);
        if(res == null){
            return new JsonMessage(false, "", "新增失败");
        }
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setMenuId(menus);
        roleMenu.setRoleId(res.getId());
        RoleMenu res0 = roleMenuService.save(roleMenu);
        if(res0 == null){
            return new JsonMessage(false, "", "新增失败");
        }
        return new JsonMessage(true, "", "", res);
    }


    /**
     * 修改
     * @param id
     * @param name
     * @param menus
     * @param remark
     * @return
     */
    @Transactional
    @RequestMapping(value = "/role/{id}", method = RequestMethod.POST)
    public JsonMessage updateRole(@PathVariable("id") int id,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "menus", required = false) String menus,
                               @RequestParam(value = "remark", required = false) String remark){

        Role role = roleService.findById(id);
        if(role == null){
            return new JsonMessage(false, "", "角色不存在");
        }
        if(id == 1 || role.getName().equals("超级管理员")){
            return new JsonMessage(false, "", "不能修改超级管理员！");
        }
        Role r = roleService.findByName(name);
        if(r != null && !name.equals(role.getName())){
            return new JsonMessage(false, "", "角色名称已存在");
        }

        role.setName(name == null ? role.getName() : name);
        role.setRemark(remark == null ? role.getRemark() : remark);
        Role res = roleService.save(role);
        if(res == null){
            return new JsonMessage(false, "", "修改失败");
        }
        RoleMenu roleMenu = roleMenuService.findByRid(res.getId());
        if(roleMenu == null){
            roleMenu = new RoleMenu();
        }
        roleMenu.setMenuId(menus);
        roleMenu.setRoleId(res.getId());
        RoleMenu res0 = roleMenuService.save(roleMenu);
        if(res0 == null){
            return new JsonMessage(false, "", "修改失败");
        }
        return new JsonMessage(true, "", "", res);
    }

    /**
     * 删除单个
     * @param id
     * @return
     */
    @Transactional
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public JsonMessage deleteOne(@PathVariable("id") int id) throws Exception {
        Role role = roleService.findById(id);
        if(role == null){

            return new JsonMessage(false, "", "角色不存在");
        }
        if(id == 1 || role.getName().equals("超级管理员")){
            return new JsonMessage(false, "", "不能删除超级管理员！");
        }
        roleService.delete(id);
        RoleMenu roleMenu = roleMenuService.findByRid(role.getId());
        if(roleMenu != null){
            roleMenuService.delete(roleMenu.getId());
        }
        return new JsonMessage(true, "", "", "删除成功");
    }

    /**
     * 查询所有菜单
     *
     * @return
     */
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public JsonMessage searchMenu() {

        return new JsonMessage(true, "", "", menuService.findMenus());
    }

    /**
     * 查询所有角色的菜单
     *
     * @return
     */
    @RequestMapping(value = "/role/{id}/menu", method = RequestMethod.GET)
    public JsonMessage getRoleMenu(@PathVariable("id") int id) {

        return new JsonMessage(true, "", "", roleMenuService.findByRid(id));
    }
}
