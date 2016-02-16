package com.autobon.mobile.controller;

import com.autobon.mobile.entity.Technician;
import com.autobon.mobile.service.TechnicianService;
import com.autobon.mobile.utils.JsonMessage;
import com.autobon.mobile.utils.SmsSender;
import com.autobon.mobile.utils.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by dave on 16/2/12.
 */
@RestController
@RequestMapping("/api/mobile")
public class AccountController {
    @Autowired private TechnicianService technicianService;
    @Autowired private SmsSender smsSender;
    @Value("${com.autobon.env:PROD}")
    private String env;

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public void getVerifyCode(HttpSession session, OutputStream out) throws IOException {
        String code = VerifyCode.generateVerifyCode(6);
        session.setAttribute("verifyCode", code);
        VerifyCode.writeVerifyCodeImage(250, 40, out, code);
    }

    @RequestMapping(value = "/verifySms", method = RequestMethod.GET)
    public JsonMessage getVerifySms(HttpSession session, @RequestParam("phone") String phone) throws IOException {
        String code = VerifyCode.generateRandomNumber(6);
        if (env.equals("TEST")) {
            code = "123456";
        } else {
            smsSender.send(phone, "【车邻邦】你的验证码是：" + code + ", 请不要把验证码泄露给其他人。");
        }
        session.setAttribute("verifySms", code);
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public JsonMessage logout() {
        SecurityContextHolder.clearContext();
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/technician/register", method = RequestMethod.POST)
    public JsonMessage register(
            @RequestParam("phone")     String phone,
            @RequestParam("password")  String password,
            @RequestParam("verifySms") String verifySms,
            HttpSession session) {
        JsonMessage ret = new JsonMessage(true);
        ArrayList<String> messages = new ArrayList<>();

        if (!Pattern.matches("^\\d{11}$", phone)) {
            ret.setError("ILLEGAL_PARAM");
            messages.add("手机号格式错误");
        } else if (technicianService.getByPhone(phone) != null) {
            ret.setError("OCCUPIED_IDENTIFIER");
            messages.add("手机号已被注册");
        }

        if (password.length() < 6) {
            ret.setError("ILLEGAL_PARAM");
            messages.add("密码至少6位");
        }
        if (!verifySms.equals(session.getAttribute("verifySms"))) {
            ret.setError("ILLEGAL_PARAM");
            messages.add("验证码错误");
        }

        if (messages.size() > 0) {
            ret.setResult(false);
            ret.setMessage(messages.stream().collect(Collectors.joining(",")));
        } else {
            Technician technician = new Technician();
            technician.setPhone(phone);
            technician.setPassword(Technician.encryptPassword(password));
            technicianService.add(technician);
            ret.setData(technician);
        }
        return ret;
    }
}
