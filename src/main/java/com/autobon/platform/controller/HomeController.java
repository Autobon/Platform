package com.autobon.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liz on 2016/2/19.
 */
@Controller
public class HomeController {
    /**
     * 静态页面处理程序
     * @return 返回HTML静态页面
     */
    @RequestMapping("/web-builder/**")
    public String forwardIndex() {
        return "forward:/index.html";
    }

    /**
     * 静态页面处理程序
     * 登录页面
     * @return 返回HTML静态页面
     */
    @RequestMapping("/login/**")
    public String forwardLogin() {
        return "forward:/login.html";
    }
}
