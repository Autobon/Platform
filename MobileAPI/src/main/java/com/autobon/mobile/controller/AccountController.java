package com.autobon.mobile.controller;

import com.autobon.mobile.entity.Technician;
import com.autobon.mobile.service.TechnicianService;
import com.autobon.mobile.utils.JsonMessage;
import com.autobon.mobile.utils.RedisCache;
import com.autobon.mobile.utils.SmsSender;
import com.autobon.mobile.utils.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired private RedisCache redisCache;
    @Value("${com.autobon.env:PROD}")
    private String env;

//    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
//    public void getVerifyCode(OutputStream out) throws IOException {
//        String code = VerifyCode.generateVerifyCode(6);
//        //TODO session.setAttribute("verifyCode", code);
//        VerifyCode.writeVerifyCodeImage(250, 40, out, code);
//    }

    @RequestMapping(value = "/verifySms", method = RequestMethod.GET)
    public JsonMessage getVerifySms(@RequestParam("phone") String phone) throws IOException {
        String code = VerifyCode.generateRandomNumber(6);
        if (env.equals("TEST")) {
            code = "123456";
        } else {
            smsSender.send(phone, "【车邻邦】你的验证码是：" + code + ", 请不要把验证码泄露给其他人。");
        }
        redisCache.set(("verifySms:" + phone).getBytes(), code.getBytes(), 15*60);
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
            @RequestParam("verifySms") String verifySms) {

        JsonMessage ret = new JsonMessage(true);
        ArrayList<String> messages = new ArrayList<>();

        if (!Pattern.matches("^\\d{11}$", phone)) {
            ret.setError("ILLEGAL_PARAM");
            messages.add("手机号格式错误");
        } else if (technicianService.getByPhone(phone) != null) {
            ret.setError("OCCUPIED_ID");
            messages.add("手机号已被注册");
        }

        if (password.length() < 6) {
            ret.setError("ILLEGAL_PARAM");
            messages.add("密码至少6位");
        }
        if (!verifySms.equals(new String(redisCache.get(("verifySms:" + phone).getBytes())))) {
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
            technicianService.save(technician);
            ret.setData(technician);
        }
        return ret;
    }

    @RequestMapping(value = "/technician/login", method = RequestMethod.POST)
    public JsonMessage loginTechnician(HttpServletResponse response,
            @RequestParam("phone")    String phone,
            @RequestParam("password") String password) {

        JsonMessage ret = new JsonMessage(true);
        Technician technician = technicianService.getByPhone(phone);

        if (technician == null) {
            ret.setResult(false);
            ret.setError("NO_SUCH_USER");
            ret.setMessage("手机号未注册");
        } else if (!technician.getPassword().equals(Technician.encryptPassword(password))) {
            ret.setResult(false);
            ret.setError("ILLEGAL_PARAM");
            ret.setMessage("密码错误");
        } else {
            response.addCookie(new Cookie("autoken", Technician.makeToken(technician.getId())));
            ret.setData(technician);
        }
        return ret;
    }

    @RequestMapping(value = "/technician/resetPassword", method = RequestMethod.POST)
    public JsonMessage resetPassword(HttpSession session,
            @RequestParam("phone")     String phone,
            @RequestParam("verifySms") String verifySms) throws Exception {

        JsonMessage ret = new JsonMessage(true);
        Technician technician = technicianService.getByPhone(phone);
        if (technician == null) {
            ret.setResult(false);
            ret.setError("NO_SUCH_USER");
            ret.setMessage("手机号未注册");
        } else if (!verifySms.equals(new String(redisCache.get(("verifySms:" + phone).getBytes())))) {
            ret.setResult(false);
            ret.setError("ILLEGAL_PARAM");
            ret.setMessage("验证码错误");
        } else {
            String password = VerifyCode.generateRandomNumber(6);
            technician.setPassword(Technician.encryptPassword(password));
            technicianService.save(technician);
            smsSender.send(phone, "【车邻邦】你的新密码是：" + password + ", 请登录后及时更换密码。");
        }
        return ret;
    }

    @RequestMapping(value = "/technician/changePassword", method = RequestMethod.POST)
    public JsonMessage changePassword(@RequestParam("password") String password) {
        System.out.println("password: " + password);
        JsonMessage ret = new JsonMessage(true);
        if (password.length() < 6) {
            ret.setResult(false);
            ret.setError("ILLEGAL_PARAM");
            ret.setMessage("密码至少6位");
        } else {
            Technician technician = (Technician) SecurityContextHolder.getContext().getAuthentication().getDetails();
            technician.setPassword(password);
            technicianService.save(technician);
        }
        return ret;
    }
}
