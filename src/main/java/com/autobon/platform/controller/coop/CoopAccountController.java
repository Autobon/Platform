package com.autobon.platform.controller.coop;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.cooperators.service.ReviewCooperService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
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

    @Autowired
    private CoopAccountService coopAccountService;

    @Autowired
    private ReviewCooperService reviewCooperService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonMessage register(
            @RequestParam("shortname") String shortname,
            @RequestParam("phone")     String phone,
            @RequestParam("password")  String password,
            @RequestParam("verifySms") String verifySms) {

        JsonMessage msg = new JsonMessage(true);
        ArrayList<String> messages = new ArrayList<>();

        if(coopAccountService.getByShortname(shortname)!=null){
            msg.setError("OCCUPIED_ID");
            messages.add("企业简称已被注册");
        }

        if (!Pattern.matches("^\\d{11}$", phone)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("手机号格式错误");
        } else if (coopAccountService.getByPhone(phone) != null) {
            msg.setError("OCCUPIED_ID");
            messages.add("手机号已被注册");
        }

        if (password.length() < 6) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("密码至少6位");
        }
        String code = redisCache.get("verifySms:" + phone);
        if (!verifySms.equals(code)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("验证码错误");
        }

        if (messages.size() > 0) {
            msg.setResult(false);
            msg.setMessage(messages.stream().collect(Collectors.joining(",")));
        } else {
            CoopAccount coopAccount = new CoopAccount();
            coopAccount.setShortname(shortname);
            coopAccount.setPhone(phone);
            coopAccount.setPassword(coopAccount.encryptPassword(password));
            coopAccountService.save(coopAccount);
            msg.setData(coopAccount);
        }
        return msg;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonMessage login(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam("shortname") String shortname,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password) {

        JsonMessage msg = new JsonMessage(true);
        CoopAccount coopAccount = coopAccountService.getByPhone(phone);

        if (coopAccount == null) {
            msg.setResult(false);
            msg.setError("NO_SUCH_USER");
            msg.setMessage("手机号未注册");
        } else if (!coopAccount.getPassword().equals(CoopAccount.encryptPassword(password))) {
            msg.setResult(false);
            msg.setError("PASSWORD_MISMATCH");
            msg.setMessage("密码错误");
        }else if(!coopAccount.getShortname().equals(shortname)){
            msg.setResult(false);
            msg.setError("NO_SUCH_USER");
            msg.setMessage("手机号与企业简称不匹配");
        } else if(coopAccount.isFired()){
            msg.setResult(false);
            msg.setError("USER_FIRED");
            msg.setMessage("该员工已离职");
        } else {
            response.addCookie(new Cookie("autoken", CoopAccount.makeToken(coopAccount.getId())));
            coopAccount.setLastLoginTime(new Date());
            coopAccount.setLastLoginIp(request.getRemoteAddr());
            coopAccountService.save(coopAccount);

            Cooperator cooperator = null;
            ReviewCooper reviewCooper = null;
            int cooperatorId = 0;
            cooperatorId = coopAccount.getCooperatorId();
            if(cooperatorId>0){
                cooperator = cooperatorService.get(cooperatorId);
                List<ReviewCooper> reviewCooperList = reviewCooperService.getByCooperatorId(cooperatorId);
                if(reviewCooperList.size()>0){
                    reviewCooper = reviewCooperList.get(0);
                }
            }

            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("coopAccount", coopAccount);
            dataMap.put("cooperator", cooperator);
            dataMap.put("reviewCooper", reviewCooper);
            msg.setData(dataMap);
        }
        return msg;
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public JsonMessage resetPassword(
            @RequestParam("phone")     String phone,
            @RequestParam("password")  String password,
            @RequestParam("verifySms") String verifySms) throws Exception {

        JsonMessage msg = new JsonMessage(true);
        CoopAccount coopAccount = coopAccountService.getByPhone(phone);
        if (coopAccount == null) {
            msg.setResult(false);
            msg.setError("NO_SUCH_USER");
            msg.setMessage("手机号未注册");
        } else if (!verifySms.equals(redisCache.get("verifySms:" + phone))) {
            msg.setResult(false);
            msg.setError("ILLEGAL_PARAM");
            msg.setMessage("验证码错误");
        } else if (password.length() < 6) {
            msg.setResult(false);
            msg.setError("ILLEGAL_PARAM");
            msg.setMessage("密码至少6位");
        } else {
            coopAccount.setPassword(CoopAccount.encryptPassword(password));
            coopAccountService.save(coopAccount);
        }
        return msg;
    }


    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public JsonMessage changePassword(HttpServletRequest request,
                                      @RequestParam("oldPassword") String oldPassword,
                                      @RequestParam("newPassword") String newPassword) {
        if (newPassword.length() < 6) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "密码至少6位");
        } else {
            //Cooperator cooperator = (Cooperator) request.getAttribute("user");
            CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
            if (!coopAccount.getPassword().equals(CoopAccount.encryptPassword(oldPassword))) {
                return new JsonMessage(false, "ILLEGAL_PARAM", "原密码错误");
            }
            coopAccount.setPassword(CoopAccount.encryptPassword(newPassword));
            coopAccountService.save(coopAccount);
        }
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/getSaleList",method = RequestMethod.POST)
    public JsonMessage getSaleList(HttpServletRequest request) throws Exception{
        //Cooperator cooperator = (Cooperator)request.getAttribute("user");
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        if(!coopAccount.isMain()){
            return  new JsonMessage(false,"当前账户不是管理账号");
        }
        int coopId = coopAccount.getCooperatorId();
        List<CoopAccount> coopAccountList = coopAccountService.findCoopAccountByCooperatorId(coopId);
        return new JsonMessage(true,"","",coopAccountList);
    }

    @RequestMapping(value = "/saleFired",method = RequestMethod.POST)
    public JsonMessage saleFired(@RequestParam("coopAccountId") int coopAccountId) throws Exception{
        CoopAccount coopAccount = coopAccountService.getById(coopAccountId);
        if(!coopAccount.isMain()){
            return  new JsonMessage(false,"当前账户不是管理账号");
        }
        if(coopAccount!=null){
            coopAccount.setFired(true);
            coopAccountService.save(coopAccount);
            return new JsonMessage(true,"","",coopAccount);
        }else{
            return  new JsonMessage(false,"商户id不正确");
        }
    }


    @RequestMapping(value = "/addAccount",method = RequestMethod.POST)
    public JsonMessage addAccount(HttpServletRequest request,
                                  @RequestParam("phone") String phone,
                                  @RequestParam("name") String name,
                                  @RequestParam("gender") boolean gender) throws  Exception{
        CoopAccount coopAccountLogin = (CoopAccount) request.getAttribute("user");
        if(!coopAccountLogin.isMain()){
            return  new JsonMessage(false,"当前账户不是管理账号");
        }
        int coopId = coopAccountLogin.getCooperatorId();
        //Cooperator cooperator = (Cooperator)request.getAttribute("user");
        //int coopId = cooperator.getId();
        CoopAccount coopAccount = new CoopAccount();
        coopAccount.setCooperatorId(coopId);
        coopAccount.setPhone(phone);
        coopAccount.setName(name);
        coopAccount.setGender(gender);
        //设置默认密码为123456
        coopAccount.setPassword(CoopAccount.encryptPassword("123456"));
        coopAccount.setCreateTime(new Date());
        coopAccountService.save(coopAccount);
        return new JsonMessage(true,"","",coopAccount);
    }

    @RequestMapping(value = "/changeAccountPassword",method = RequestMethod.POST)
    public JsonMessage changeAccountPassword(@RequestParam("coopAccountId") int coopAccountId,
                                            @RequestParam("oldPassword") String oldPassword,
                                            @RequestParam("newPassword") String newPassword) {
        if (newPassword.length() < 6) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "密码至少6位");
        } else {
            CoopAccount coopAccount =coopAccountService.getById(coopAccountId);

            if (!coopAccount.getPassword().equals(coopAccount.encryptPassword(oldPassword))) {
                return new JsonMessage(false, "ILLEGAL_PARAM", "原密码错误");
            }
            coopAccount.setPassword(CoopAccount.encryptPassword(newPassword));
            coopAccountService.save(coopAccount);
        }
        return new JsonMessage(true);
    }

    /**
     * 更新用户的个推ID
     * @param pushId
     * @return
     */
    @RequestMapping(value = "/pushId", method = RequestMethod.POST)
    public JsonMessage savePushId(HttpServletRequest request,
                                  @RequestParam("pushId") String pushId) {
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        CoopAccount oCoopAccount = coopAccountService.getByPushId(pushId);

        if (oCoopAccount != null) {
            if (coopAccount.getId() == oCoopAccount.getId()) return new JsonMessage(true);
            else {
                oCoopAccount.setPushId(null);
                coopAccountService.save(oCoopAccount);
            }
        }

        coopAccount.setPushId(pushId);
        coopAccountService.save(coopAccount);
        return new JsonMessage(true);
    }
}
