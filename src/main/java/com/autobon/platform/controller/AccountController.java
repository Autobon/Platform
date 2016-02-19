package com.autobon.platform.controller;

import com.autobon.platform.utils.JsonMessage;
import com.autobon.platform.utils.RedisCache;
import com.autobon.platform.utils.SmsSender;
import com.autobon.platform.utils.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

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

    /***
     * 在提交含有验证码图片的表单时,为了对验证码正确性进行验证,请一并传入获取验证码图片时的seed参数.为了不使用sessionId的折衷方法.
     * @param seed 由客户端生成的用于标识自己的随机字符串,建议使用如下形式: 代表时间戳长整数 + ':' + 12位随机码
     * @param out
     * @throws IOException
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public void getVerifyCode(@RequestParam("seed") String seed, OutputStream out) throws IOException {
        String code = VerifyCode.generateVerifyCode(6);
        if (env.equals("TEST")) code = "123456";
        redisCache.set(seed.getBytes(), code.getBytes(), 5*60);
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
        redisCache.set(("verifySms:" + phone).getBytes(), code.getBytes(), 5*60);
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
