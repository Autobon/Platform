package com.autobon.platform.controller;

import com.autobon.order.entity.WorkItem;
import com.autobon.order.service.WorkItemService;
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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by yuh on 2016/2/18.
 */
@RestController
@RequestMapping("/api/pub")
public class PubController {

    @Autowired WorkItemService workItemService;
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
            smsSender.sendVerifyCode(phone, code);
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

    @RequestMapping(value = "/technician/skills", method = RequestMethod.GET)
    public JsonMessage getSkills() {
        return getOrderTypes();
    }

    @RequestMapping(value = "/orderTypes", method = RequestMethod.GET)
    public JsonMessage getOrderTypes() {
        return new JsonMessage(true, "", "",
                workItemService.getOrderTypes().stream().map(t -> {
                    HashMap<String, Object> m = new HashMap<>();
                    m.put("id", t.getOrderType());
                    m.put("name", t.getOrderTypeName());
                    return m;
                }).collect(Collectors.toList()));
    }

    @RequestMapping(value="/technician/workItems", method = RequestMethod.GET)
    public JsonMessage getWorkItems(
            @RequestParam("orderType") int orderType,
            @RequestParam(value = "carSeat", defaultValue = "0")   int carSeat) {
        List<WorkItem> list = null;
        if (carSeat == 0) list = workItemService.findByOrderType(orderType);
        else list = workItemService.findByOrderTypeAndCarSeat(orderType, carSeat);
        return new JsonMessage(true, "", "", list.stream().map(i -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", i.getId());
                    map.put("seat", i.getCarSeat());
                    map.put("name", i.getWorkName());
                    return map;
                }).collect(Collectors.toList()));
    }

}
