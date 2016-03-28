package com.autobon.platform.controller.admin;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.cooperators.service.ReviewCooperService;
import com.autobon.getui.PushService;
import com.autobon.shared.JsonMessage;
import com.autobon.staff.entity.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by yuh on 2016/3/10.
 */
@RestController("adminCooperatorController")
@RequestMapping("/api/web/admin/cooperator")
public class CooperatorController {

    private static Logger log = LoggerFactory.getLogger(CooperatorController.class);

    private CooperatorService cooperatorService;

    @Autowired
    public void setCooperatorService(CooperatorService cooperatorService){
        this.cooperatorService = cooperatorService;
    }

    @Autowired
    private ReviewCooperService reviewCooperService;

    @Autowired
    private CoopAccountService coopAccountService;

    @Autowired
    @Qualifier
    ("PushServiceB")
    PushService pushService;

    @RequestMapping(value = "/coopList", method = RequestMethod.POST)
    public JsonMessage coopList(@RequestParam(value="fullname",required = false) String fullname,
                                @RequestParam(value="businessLicense",required = false) String businessLicense,
                                @RequestParam(value="statusCode",required = false) Integer statusCode,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize){

        Page<Cooperator> coopList = cooperatorService.findCoop(fullname,businessLicense,statusCode,page,pageSize);
        return new JsonMessage(true,"","",coopList);
    }


    @RequestMapping(value="/getCoop",method = RequestMethod.GET)
    public JsonMessage getCoop(@RequestParam(value="coopId") int coopId){
        Cooperator cooperator = cooperatorService.get(coopId);
        return new JsonMessage(true,"","",cooperator);
    }


    @RequestMapping(value="/checkCoop/{coopId:[\\d]+}",method = RequestMethod.POST)
    public JsonMessage checkCoop(HttpServletRequest request,
                                 @PathVariable("coopId") int coopId,
                                 @RequestParam("verified") boolean verified,
                                // @RequestParam(value = "statusCode") int statusCode,
                                 @RequestParam(value = "resultDesc",required = false) String resultDesc) throws IOException {
        Cooperator cooperator = cooperatorService.get(coopId);
        CoopAccount coopAccount = coopAccountService.getByCooperatorIdAndIsMain(coopId, true);

        if (coopAccount != null) {
            ReviewCooper reviewCooper = new ReviewCooper();
            reviewCooper.setCooperatorsId(coopId);
            reviewCooper.setReviewTime(new Date());
            Staff staff = (Staff) request.getAttribute("user");
            reviewCooper.setCheckedBy(staff.getName());
            if(verified){
                cooperator.setStatusCode(1);
                reviewCooper.setResult(true);
                String title = "你已通过合作商户资格认证";
                pushService.pushToSingle(coopAccount.getPushId(), title,
                        "{\"action\":\"VERIFICATION_SUCCEED\",\"title\":\"" + title + "\"}",
                        3*24*3600);
            }else{
                if (resultDesc.equals("")) {
                    return new JsonMessage(false, "INSUFFICIENT_PARAM", "请填写认证失败原因");
                }
                if(resultDesc!=null){
                    reviewCooper.setResultDesc(resultDesc);
                }
                cooperator.setStatusCode(2);
                reviewCooper.setResult(false);
                String title = "你的合作商户资格认证失败: " + resultDesc;
                pushService.pushToSingle(coopAccount.getPushId(), title,
                        "{\"action\":\"VERIFICATION_FAILED\",\"title\":\"" + title + "\"}",
                        3*24*3600);

            }
            reviewCooperService.save(reviewCooper);
            coopAccountService.save(coopAccount);
            cooperatorService.save(cooperator);
            return new JsonMessage(true,"","",reviewCooper);
        } else {
            return new JsonMessage(false, "ILLEGAL_PARAM", "这个合作商户没有设置主账户");
        }

    }


    @RequestMapping(value = "/update/{coopId:[\\d]+}", method = RequestMethod.POST)
    public JsonMessage update(@PathVariable("coopId") int coopId,
                              @RequestParam(value="phone",required = true) String phone,
                              @RequestParam(value="shortname",required = true) String shortname,
                              @RequestParam(value="fullname",required = true) String fullname,
                              @RequestParam(value = "businessLicense",required = true) String businessLicense,
                              @RequestParam(value = "corporationName",required = true) String corporationName,
                              @RequestParam(value = "corporationIdNo",required = true) String corporationIdNo,
                              @RequestParam(value = "bussinessLicensePic",required = true) String bussinessLicensePic,
                              @RequestParam(value = "corporationIdPicA",required = true) String corporationIdPicA,
                              @RequestParam(value = "corporationIdPicB",required = true) String corporationIdPicB,
                              @RequestParam(value = "longitude",required = true) String longitude,
                              @RequestParam(value = "latitude",required = true) String latitude,
                              @RequestParam(value = "invoiceHeader",required = true) String invoiceHeader,
                              @RequestParam(value = "taxIdNo",required = true) String taxIdNo,
                              @RequestParam(value = "postcode",required = true) String postcode,
                              @RequestParam(value = "province",required = true) String province,
                              @RequestParam(value = "city",required = true) String city,
                              @RequestParam(value = "district",required = true) String district,
                              @RequestParam(value = "address",required = true) String address,
                              @RequestParam(value = "contact",required = true) String contact,
                              @RequestParam(value = "contactPhone",required = true) String contactPhone) {

        if (!Pattern.matches("^\\d{11}$", phone)) {
            return  new JsonMessage(false,"ILLEGAL_PARAM","手机号格式错误");
        } else if (cooperatorService.getByPhone(phone) != null) {
            return new JsonMessage(false,"OCCUPIED_ID","手机号已被注册");
        }

        corporationIdNo = corporationIdNo.toUpperCase();
        if (!Pattern.matches("^(\\d{15})|(\\d{17}[0-9X])$", corporationIdNo))
            return new JsonMessage(false, "ILLEGAL_PARAM", "身份证号码有误");

/*
        CoopAccount coopAccount = coopAccountService.getByCooperatorIdAndIsMain(coopId, true);
        if(coopAccount == null){
            return new JsonMessage(false,"商户没有设置主账号");
        }
        coopAccount.setShortname(shortname);
        coopAccountService.save(coopAccount);*/

        Cooperator cooperator = cooperatorService.get(coopId);
        cooperator.setPhone(phone);
        //cooperator.setShortname(shortname);
        cooperator.setFullname(fullname);
        cooperator.setBusinessLicense(businessLicense);
        cooperator.setCorporationName(corporationName);
        cooperator.setCorporationIdNo(corporationIdNo);
        cooperator.setBussinessLicensePic(bussinessLicensePic);
        cooperator.setCorporationIdPicA(corporationIdPicA);
        cooperator.setCorporationIdPicB(corporationIdPicB);
        cooperator.setLongitude(longitude);
        cooperator.setLatitude(latitude);
        cooperator.setInvoiceHeader(invoiceHeader);
        cooperator.setTaxIdNo(taxIdNo);
        cooperator.setPostcode(postcode);
        cooperator.setProvince(province);
        cooperator.setCity(city);
        cooperator.setDistrict(district);
        cooperator.setAddress(address);
        cooperator.setContact(contact);
        cooperator.setContactPhone(contactPhone);
        cooperatorService.save(cooperator);
        return new JsonMessage(true,"","",cooperator);
    }


}
