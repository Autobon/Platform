package com.autobon.platform.controller;

import com.autobon.shared.JsonMessage;
import com.autobon.shared.RedisCache;
import com.autobon.shared.SmsSender;
import com.autobon.shared.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by dave on 16/2/12.
 */
@RestController
@RequestMapping("/api/mobile")
public class AccountController {
    @Autowired SmsSender smsSender;
    @Autowired RedisCache redisCache;
    @Value("${com.autobon.env:PROD}")
    private String env;


    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response, OutputStream out) throws IOException {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if (cookies != null) {
            cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("JSESSIONID")).findFirst().orElse(null);
        }
        if (cookie == null) {
            cookie = new Cookie("JSESSIONID", UUID.randomUUID().toString());
            cookie.setMaxAge(-1); // 浏览器关闭即失效
            response.addCookie(cookie);
        }
        String code = VerifyCode.generateVerifyCode(6);
        if (env.equals("TEST")) code = "123456";
        redisCache.set("verifyCode:" + cookie.getValue(), code, 5*60);
        VerifyCode.writeVerifyCodeImage(250, 40, out, code);
    }

    @RequestMapping(value = "/verifySms", method = RequestMethod.GET)
    public JsonMessage getVerifySms(@RequestParam("phone") String phone) throws IOException {
        String code = VerifyCode.generateRandomNumber(6);
        if (env.equals("TEST")) {
            code = "123456";
        } else {
            smsSender.send(phone, "【车邻邦】你的验证码是：" + code + ", 请不要把验证码泄露给其他人。");
        }
        redisCache.set("verifySms:" + phone, code, 5*60);
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public JsonMessage logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Cookie cookie = new Cookie("autoken", null);
        cookie.setMaxAge(0); // 立即删除
        response.addCookie(cookie);
        return new JsonMessage(true);
    }


}
