package com.autobon.platform.controller.app;

import com.autobon.shared.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by wh on 2016/11/16.
 */
@RestController
@RequestMapping("/api/pub")
public class VerifyController {

    @Autowired
    SmsSender smsSender;
    @Autowired
    RedisCache redisCache;
    @Value("${com.autobon.env:PROD}")
    private String env;


    /**
     * 发送短信验证码
     * @param phone 手机号码
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/v2/verifySms", method = RequestMethod.POST)
    public JsonResult getVerifySms(@RequestParam("phone") String phone) throws IOException {
        String code = VerifyCode.generateRandomNumber(6);
//        if (env.equals("TEST")) {
//            code = "123456";
//        }
//        else {
//            smsSender.sendVerifyCode(phone, code);
//        }

        code = "123456";

        redisCache.set("verifySms:" + phone, code, 5 * 60);
        return new JsonResult(true);
    }


}
