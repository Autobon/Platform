package com.autobon.platform.admin.controller;

import org.springframework.web.bind.annotation.*;

/**
 * Created by liz on 2016/2/18.
 */
@RestController
@RequestMapping("/api")
public class StaffController {

//    private static final org.slf4j.Logger log = LoggerFactory.getLogger(StaffController.class);
//
//    private StaffService staffService = null;
//    @Autowired
//    public void setStaffService(StaffService staffService){this.staffService = staffService;}
//
//
//    @RequestMapping(value = "/staff/login", method = RequestMethod.POST)
//    public JsonMessage login(@RequestBody StaffValidate staffValidate,HttpServletResponse response) {
//        int flag = staffService.login(staffValidate.getUserName(), staffValidate.getPassword());
//
//        if(flag == 0){
//            String token = staffService.generateToken(staffValidate.getUserName());
//            StaffShow staffShow = staffService.convertByUsername(staffValidate.getUserName());
//            Cookie cookie = new Cookie("token_staff",token);
//            int seconds = 60*60*24;  //1天的秒数
//            cookie.setMaxAge(seconds);  //cookie默认保存1天
//            cookie.setPath("/"); //设置路径，这个路径即该工程下都可以访问该cookie  // 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
//            response.addCookie(cookie);
//            staffService.saveToken(token, staffShow, seconds);
//
//            return  new JsonMessage(true, "", "登录成功", staffShow);
//        }
//        if(flag == 1){
//            return new JsonMessage(false, "用户名不存在");
//        }
//        if(flag == 2){
//            return new JsonMessage(false, "密码错误");
//        }else{
//            return new JsonMessage(false, "登陆失败");
//        }
//    }
//
//
//
//    /**
//     * 通过token注销
//     * @param request http请求
//     * @param token 登陆令牌
//     * @return LoginResult对象
//     */
//    @RequestMapping(value="/staff/logout", method = RequestMethod.DELETE)
//    public JsonMessage logout(HttpServletRequest request,HttpServletResponse response,@CookieValue(value="token_staff",required = false) String token){
//
//        int flag = staffService.logout(token);
//
//        if(flag == 0){
//            Cookie cookie_token = new Cookie("token_staff",null);
//            cookie_token.setMaxAge(0);
//            cookie_token.setPath("/");
//            response.addCookie(cookie_token);     // 清除cookie
//
//            return new JsonMessage(true,"注销成功");
//        }
//        return new JsonMessage(false,"注销失败");
//    }


}
