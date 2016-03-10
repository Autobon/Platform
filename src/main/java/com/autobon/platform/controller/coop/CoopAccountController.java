package com.autobon.platform.controller.coop;

import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by dave on 16/3/2.
 */
@RestController
@RequestMapping("/api/mobile/coop")
public class CoopAccountController {

    @Autowired
    RedisCache redisCache;

    @Autowired
    private CooperatorService cooperatorService;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonMessage register(
            @RequestParam("shortname") String shortname,
            @RequestParam("contactPhone")     String contactPhone,
            @RequestParam("password")  String password,
            @RequestParam("verifySms") String verifySms) {

        JsonMessage msg = new JsonMessage(true);
        ArrayList<String> messages = new ArrayList<>();

        if(cooperatorService.getByShortname(shortname)!=null){
            msg.setError("OCCUPIED_ID");
            messages.add("企业简称已被注册");
        }

        if (!Pattern.matches("^\\d{11}$", contactPhone)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("手机号格式错误");
        } else if (cooperatorService.getByContactPhone(contactPhone) != null) {
            msg.setError("OCCUPIED_ID");
            messages.add("手机号已被注册");
        }

        if (password.length() < 6) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("密码至少6位");
        }
        String code = redisCache.get("verifySms:" + contactPhone);
        if (!verifySms.equals(code)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("验证码错误");
        }

        if (messages.size() > 0) {
            msg.setResult(false);
            msg.setMessage(messages.stream().collect(Collectors.joining(",")));
        } else {
            Cooperator cooperator = new Cooperator();
            cooperator.setShortname(shortname);
            cooperator.setPhone(contactPhone);
            cooperator.setPassword(cooperator.encryptPassword(password));
            cooperatorService.save(cooperator);
            msg.setData(cooperator);
        }
        return msg;
    }

}
