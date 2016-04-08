package com.autobon.platform.controller.admin;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.cooperators.service.ReviewCooperService;
import com.autobon.getui.PushService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.staff.entity.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired CooperatorService cooperatorService;
    @Autowired CoopAccountService coopAccountService;
    @Autowired ReviewCooperService reviewCooperService;
    @Autowired @Qualifier("PushServiceB")
    PushService pushService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search(
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "corporationName", required = false) String corporationName,
            @RequestParam(value = "statusCode", required = false) Integer statusCode,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        return new JsonMessage(true, "", "", new JsonPage<>(cooperatorService.find(
                fullname, corporationName, statusCode, page, pageSize)));
    }

    @RequestMapping(value = "/{coopId:\\d+}", method = RequestMethod.GET)
    public JsonMessage getCoop(@PathVariable(value = "coopId") int coopId) {
        return new JsonMessage(true, "", "", cooperatorService.get(coopId));
    }

    // 认证商户
    @RequestMapping(value = "/verify/{coopId:\\d+}", method = RequestMethod.POST)
    public JsonMessage verify(HttpServletRequest request,
            @PathVariable("coopId") int coopId,
            @RequestParam("verified") boolean verified,
            @RequestParam(value = "remark", defaultValue = "") String remark) throws IOException {
        Cooperator coop = cooperatorService.get(coopId);
        CoopAccount coopAccount = coopAccountService.getByCooperatorIdAndIsMain(coopId, true);
        Staff staff = (Staff) request.getAttribute("user");

        if (coop == null) {
            return new JsonMessage(false, "NO_SUCH_COOPERATOR", "没有这个商户");
        }

        ReviewCooper reviewCooper = new ReviewCooper();
        reviewCooper.setCooperatorId(coopId);
        reviewCooper.setReviewTime(new Date());
        reviewCooper.setReviewerId(staff.getId());
        if (verified) {
            coop.setStatusCode(1);
            reviewCooper.setResult(true);
            String title = "你已通过合作商户资格认证";
            pushService.pushToSingle(coopAccount.getPushId(), title,
                    "{\"action\":\"VERIFICATION_SUCCEED\",\"title\":\"" + title + "\"}",
                    3 * 24 * 3600);
        } else {
            if (remark.equals("")) {
                return new JsonMessage(false, "INSUFFICIENT_PARAM", "请填写认证失败原因");
            }
            reviewCooper.setRemark(remark);
            reviewCooper.setResult(false);
            coop.setStatusCode(2);
            String title = "你的合作商户资格认证失败: " + remark;
            pushService.pushToSingle(coopAccount.getPushId(), title,
                    "{\"action\":\"VERIFICATION_FAILED\",\"title\":\"" + title + "\"}",
                    3 * 24 * 3600);
        }
        reviewCooperService.save(reviewCooper);
        cooperatorService.save(coop);
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/mapview", method = RequestMethod.GET)
    public JsonMessage getCooperatorLocations(
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "300") int pageSize) {
        if (pageSize > 500) pageSize = 500;
        return new JsonMessage(true, "", "", cooperatorService.findByLocation(province, city, page, pageSize));
    }

    @RequestMapping(value = "/update/{coopId:[\\d]+}", method = RequestMethod.POST)
    public JsonMessage update(@PathVariable("coopId") int coopId,
            @RequestParam(value = "fullname") String fullname,
            @RequestParam(value = "businessLicense") String businessLicense,
            @RequestParam(value = "corporationName") String corporationName,
            @RequestParam(value = "corporationIdNo") String corporationIdNo,
            @RequestParam(value = "bussinessLicensePic") String bussinessLicensePic,
            @RequestParam(value = "corporationIdPicA") String corporationIdPicA,
            @RequestParam(value = "corporationIdPicB") String corporationIdPicB,
            @RequestParam(value = "longitude") String longitude,
            @RequestParam(value = "latitude") String latitude,
            @RequestParam(value = "invoiceHeader") String invoiceHeader,
            @RequestParam(value = "taxIdNo") String taxIdNo,
            @RequestParam(value = "postcode") String postcode,
            @RequestParam(value = "province") String province,
            @RequestParam(value = "city") String city,
            @RequestParam(value = "district") String district,
            @RequestParam(value = "address") String address,
            @RequestParam(value = "contact") String contact,
            @RequestParam(value = "contactPhone") String contactPhone) {

        corporationIdNo = corporationIdNo.toUpperCase();
        if (!Pattern.matches("^(\\d{15})|(\\d{17}[0-9X])$", corporationIdNo))
            return new JsonMessage(false, "ILLEGAL_PARAM", "身份证号码有误");

        Cooperator cooperator = cooperatorService.get(coopId);
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
        return new JsonMessage(true, "", "", cooperator);
    }


}
