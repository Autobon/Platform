package com.autobon.platform.controller.app.merchant;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.cooperators.service.ReviewCooperService;
import com.autobon.order.entity.Order;
import com.autobon.order.service.OrderService;
import com.autobon.platform.listener.CooperatorEventListener;
import com.autobon.platform.listener.Event;
import com.autobon.platform.listener.OrderEventListener;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonResult;
import com.autobon.shared.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by wh on 2016/11/10.
 */
@RestController
@RequestMapping("/api/mobile/coop")
public class MerchantController {

    @Autowired
    RedisCache redisCache;

    @Autowired
    CooperatorService cooperatorService;

    @Autowired
    CoopAccountService coopAccountService;

    @Autowired
    ReviewCooperService reviewCooperService;

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    OrderService orderService;

    /**
     * 商户注册
     * @param phone 手机号码
     * @param password 密码
     * @param verifySms 验证码
     * @return
     */
    @RequestMapping(value = "/merchant/register", method = RequestMethod.POST)
    public JsonResult register(@RequestParam("phone")     String phone,
                               @RequestParam("password")  String password,
                               @RequestParam("verifySms") String verifySms) {

        JsonResult jsonResult = new JsonResult(true);
        ArrayList<String> messages = new ArrayList<>();

        if (!Pattern.matches("^\\d{11}$", phone)) {
            messages.add("手机号格式错误");
        } else if (coopAccountService.getByPhone(phone) != null) {
            messages.add("手机号已被注册");
        }

        if (password.length() < 6) {
            messages.add("密码至少6位");
        }
        String code = redisCache.get("verifySms:" + phone);
        if (!verifySms.equals(code)) {
            messages.add("验证码错误");
        }

        if (messages.size() > 0) {
            jsonResult.setStatus(false);
            jsonResult.setMessage(messages.stream().collect(Collectors.joining(",")));
        } else {
            CoopAccount coopAccount = new CoopAccount();
            coopAccount.setPhone(phone);
            coopAccount.setPassword(coopAccount.encryptPassword(password));
            coopAccountService.save(coopAccount);
            jsonResult.setMessage(coopAccount);
        }
        return jsonResult;
    }


    /**
     * 商户登陆
     * @param phone
     * @param password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/merchant/login", method = RequestMethod.POST)
    public JsonResult login(@RequestParam("phone") String phone,
                            @RequestParam("password") String password,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        JsonResult jsonResult = new JsonResult(true);
        CoopAccount coopAccount = coopAccountService.getByPhone(phone);

        if (coopAccount == null) {
            jsonResult.setMessage("手机号未注册");
        } else if (!coopAccount.getPassword().equals(CoopAccount.encryptPassword(password))) {
            jsonResult.setMessage("密码错误");
        } else if(coopAccount.isFired()){
            jsonResult.setMessage("该员工已离职");
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
            jsonResult.setMessage(dataMap);
        }
        return jsonResult;
    }


    /**
     * 技师认证
     * @param request
     * @param enterpriseName
     * @param businessLicensePic
     * @param longitude
     * @param latitude
     * @return
     */
    @RequestMapping(value = "/merchant/certificate",method = RequestMethod.POST)
    public JsonMessage certificate(HttpServletRequest request,
                             @RequestParam("enterpriseName") String enterpriseName,
                             @RequestParam("businessLicensePic") String businessLicensePic,
                             @RequestParam(value = "longitude",required = false) String longitude,
                             @RequestParam(value ="latitude",required = false) String latitude){

        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int coopId = coopAccount.getCooperatorId();

        //没有认证，继续认证
        if(coopId == 0){
            Cooperator cooperator = new Cooperator();
            cooperator.setFullname(enterpriseName);
            cooperator.setBussinessLicensePic(businessLicensePic);
            cooperator.setLongitude(longitude);
            cooperator.setLatitude(latitude);
            cooperator.setStatusCode(0);
            cooperatorService.save(cooperator);

            coopAccount.setCooperatorId(cooperator.getId());
            coopAccount.setIsMain(true);
            coopAccountService.save(coopAccount);
            publisher.publishEvent(new CooperatorEventListener.CooperatorEvent(cooperator, Event.Action.CREATED));
            return new JsonMessage(true, "", "", cooperator);
        }else {
            //是否认证成功，成功则止。认证失败，再认证
            Cooperator cooperator = cooperatorService.get(coopId);
            if (cooperator == null) {
                return new JsonMessage(false, "没有此关联商户");
            } else {
                int statusCode = cooperator.getStatusCode();
                if (statusCode == 2) {
                    cooperator.setBussinessLicensePic(businessLicensePic);
                    cooperator.setLongitude(longitude);
                    cooperator.setLatitude(latitude);
                    cooperator.setStatusCode(0);
                    cooperatorService.save(cooperator);
                    return new JsonMessage(true, "", "", cooperator);
                } else if (statusCode == 1) {
                    return new JsonMessage(true, "你已经认证成功");
                } else if (statusCode == 0) {
                    return new JsonMessage(true, "等待审核");
                } else {
                    return new JsonMessage(false, "商户状态码不正确");
                }
            }
        }

    }



    /**
     *
     * @param request
     * @param photo
     * @param remark
     * @param agreedStartTime
     * @param agreedEndTime
     * @param type
     * @param pushToAll
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/merchant/order",method = RequestMethod.POST)
    public JsonResult createOrder(HttpServletRequest request,
                                   @RequestParam("photo") String photo,
                                   @RequestParam("remark") String remark,
                                   @RequestParam("agreedStartTime") String agreedStartTime,
                                   @RequestParam("agreedEndTime") String agreedEndTime,
                                   @RequestParam("type") String type,
                                   @RequestParam(value = "pushToAll", defaultValue = "true") boolean pushToAll) throws Exception {


        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int coopId = coopAccount.getCooperatorId();
        Cooperator cooperator = cooperatorService.get(coopId);
        int statusCode = cooperator.getStatusCode();
        if(statusCode !=1) {
            return new JsonResult(false,   "商户未通过验证");
        }

        if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", agreedStartTime)
                ||!Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", agreedEndTime)) {
            return new JsonResult(false,   "订单时间格式不对, 正确格式: 2016-02-10 09:23");
        }

        Order order = new Order();
        order.setCreatorId(coopAccount.getId());
        order.setCoopId(coopId);
        order.setCreatorName(coopAccount.getShortname());
        order.setPhoto(photo);
        order.setRemark(remark);
        order.setAgreedStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(agreedStartTime));
        order.setAgreedEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(agreedEndTime));
        order.setAddTime(new Date());
        order.setType(type);
        order.setPositionLon(cooperator.getLongitude());
        order.setPositionLat(cooperator.getLatitude());
        order.setContactPhone(coopAccount.getPhone());
        if (!pushToAll) order.setStatus(Order.Status.CREATED_TO_APPOINT);
        orderService.save(order);
        cooperator.setOrderNum(cooperator.getOrderNum() + 1);
        cooperatorService.save(cooperator);

        publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.CREATED));
        return new JsonResult(true,   order);
    }

}
