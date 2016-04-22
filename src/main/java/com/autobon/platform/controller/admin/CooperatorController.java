package com.autobon.platform.controller.admin;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.cooperators.service.ReviewCooperService;
import com.autobon.platform.listener.CooperatorEventListener;
import com.autobon.platform.listener.Event;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.VerifyCode;
import com.autobon.staff.entity.Staff;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by yuh on 2016/3/10.
 */
@RestController("adminCooperatorController")
@RequestMapping("/api/web/admin/cooperator")
public class CooperatorController {
    @Autowired CooperatorService cooperatorService;
    @Autowired ReviewCooperService reviewCooperService;
    @Autowired ApplicationEventPublisher publisher;
    @Autowired
    private CoopAccountService coopAccountService;
    @Value("${com.autobon.uploadPath}") String uploadPath;
    @Value("${com.autobon.gm-path}") String gmPath;

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
        Cooperator coop = cooperatorService.get(coopId);
        CoopAccount coopAccount = coopAccountService.getByCooperatorIdAndIsMain(coopId, true);
        HashMap<String, Object> map = new HashMap<>();
        map.put("coop", coop);
        map.put("mainAccount", coopAccount);
        return new JsonMessage(true, "", "", map);
    }

    // 认证商户
    @RequestMapping(value = "/verify/{coopId:\\d+}", method = RequestMethod.POST)
    public JsonMessage verify(HttpServletRequest request,
            @PathVariable("coopId") int coopId,
            @RequestParam("verified") boolean verified,
            @RequestParam(value = "remark", defaultValue = "") String remark) throws IOException {
        Cooperator coop = cooperatorService.get(coopId);
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
        } else {
            if (remark.equals("")) {
                return new JsonMessage(false, "INSUFFICIENT_PARAM", "请填写认证失败原因");
            }
            reviewCooper.setRemark(remark);
            reviewCooper.setResult(false);
            coop.setStatusCode(2);
        }
        reviewCooperService.save(reviewCooper);
        cooperatorService.save(coop);
        publisher.publishEvent(new CooperatorEventListener.CooperatorEvent(coop, Event.Action.VERIFIED));
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/mapview", method = RequestMethod.GET)
    public JsonMessage getCooperatorLocations(
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "300") int pageSize) {
        if (pageSize > 500) pageSize = 500;
        return new JsonMessage(true, "", "", new JsonPage<>(cooperatorService.findByLocation(province, city, page, pageSize)));
    }

    @RequestMapping(value = "/{coopId:[\\d]+}", method = RequestMethod.POST)
    public JsonMessage update(@PathVariable("coopId") int coopId,
            @RequestParam("fullname") String fullname,
            @RequestParam("businessLicense") String businessLicense,
            @RequestParam("corporationName") String corporationName,
            @RequestParam("corporationIdNo") String corporationIdNo,
            @RequestParam("bussinessLicensePic") String bussinessLicensePic,
            @RequestParam("corporationIdPicA") String corporationIdPicA,
            @RequestParam("longitude") String longitude,
            @RequestParam("latitude") String latitude,
            @RequestParam("invoiceHeader") String invoiceHeader,
            @RequestParam("taxIdNo") String taxIdNo,
            @RequestParam("postcode") String postcode,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("address") String address,
            @RequestParam("contact") String contact,
            @RequestParam("contactPhone") String contactPhone,
            @RequestParam("phone") String phone,
            @RequestParam("shortname") String shortname) {
        JsonMessage msg = new JsonMessage(true);
        ArrayList<String> messages = new ArrayList<>();
        corporationIdNo = corporationIdNo.toUpperCase();
        Cooperator cooperator = cooperatorService.get(coopId);
        CoopAccount coopAccount = coopAccountService.getByCooperatorIdAndIsMain(coopId, true);

        if(!shortname.equals(coopAccount.getShortname()) && coopAccountService.getByShortname(shortname) != null){
            msg.setError("OCCUPIED_ID");
            messages.add("企业简称已被注册");
        }

        if (!Pattern.matches("^\\d{11}$", phone)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("手机号格式错误");
        } else if (!phone.equals(coopAccount.getPhone()) && coopAccountService.getByPhone(phone) != null) {
            msg.setError("OCCUPIED_ID");
            messages.add("手机号已被注册");
        }

        corporationIdNo = corporationIdNo.toUpperCase();
        if (!Pattern.matches("^(\\d{15})|(\\d{17}[0-9X])$", corporationIdNo)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("身份证号码有误");
        }

        if (!Pattern.matches("^\\d{11}$", contactPhone)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("联系人电话格式错误");
        }

        if (messages.size() > 0) {
            msg.setResult(false);
            msg.setMessage(messages.stream().collect(Collectors.joining(",")));
            return msg;
        }

        cooperator.setFullname(fullname);
        cooperator.setBusinessLicense(businessLicense);
        cooperator.setCorporationName(corporationName);
        cooperator.setCorporationIdNo(corporationIdNo);
        cooperator.setBussinessLicensePic(bussinessLicensePic);
        cooperator.setCorporationIdPicA(corporationIdPicA);
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

        coopAccount.setPhone(phone);
        coopAccount.setShortname(shortname);
        coopAccountService.save(coopAccount);
        coopAccountService.batchUpdateShortname(coopId, shortname);
        msg.setData(cooperator);
        return msg;
    }

    @RequestMapping(value = "/photo", method = RequestMethod.POST)
    public JsonMessage uploadPhoto(
            @RequestParam("file") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");

        String path = "/uploads/coop";
        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
        if (!dir.exists()) dir.mkdirs();

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + VerifyCode.generateVerifyCode(6) + extension;

        InputStream in;
        if (file == null || file.isEmpty()) return new JsonMessage(false, "没有选择上传文件");
        in = file.getInputStream();

        ConvertCmd cmd = new ConvertCmd(true);
        cmd.setSearchPath(gmPath);
        cmd.setInputProvider(new Pipe(in, null));
        IMOperation operation = new IMOperation();
        operation.addImage("-");
        operation.resize(1200, 1200, ">");
        operation.addImage(dir.getAbsolutePath() + File.separator + filename);
        cmd.run(operation);

        return new JsonMessage(true, "", "", path + "/" + filename);
    }



    @RequestMapping(method = RequestMethod.POST)
    public JsonMessage createCoop(
            @RequestParam("fullname") String fullname,
            @RequestParam("businessLicense") String businessLicense,
            @RequestParam("corporationName") String corporationName,
            @RequestParam("corporationIdNo") String corporationIdNo,
            @RequestParam("bussinessLicensePic") String bussinessLicensePic,
            @RequestParam("corporationIdPicA") String corporationIdPicA,
            @RequestParam("invoiceHeader") String invoiceHeader,
            @RequestParam("taxIdNo") String taxIdNo,
            @RequestParam("postcode") String postcode,
            @RequestParam("address") String address,
            @RequestParam("contact") String contact,
            @RequestParam("contactPhone") String contactPhone,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("longitude") String longitude,
            @RequestParam("latitude") String latitude,
            @RequestParam("phone") String phone,
            @RequestParam("shortname") String shortname) {
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

        corporationIdNo = corporationIdNo.toUpperCase();
        if (!Pattern.matches("^(\\d{15})|(\\d{17}[0-9X])$", corporationIdNo)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("身份证号码有误");
        }

        if (!Pattern.matches("^\\d{11}$", contactPhone)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("联系人电话格式错误");
        }

        if (messages.size() > 0) {
            msg.setResult(false);
            msg.setMessage(messages.stream().collect(Collectors.joining(",")));
        } else {
            //创建商户
            Cooperator cooperator = new Cooperator();
            cooperator.setFullname(fullname);
            cooperator.setBusinessLicense(businessLicense);
            cooperator.setCorporationName(corporationName);
            cooperator.setCorporationIdNo(corporationIdNo);
            cooperator.setBussinessLicensePic(bussinessLicensePic);
            cooperator.setCorporationIdPicA(corporationIdPicA);
            cooperator.setInvoiceHeader(invoiceHeader);
            cooperator.setTaxIdNo(taxIdNo);
            cooperator.setPostcode(postcode);
            cooperator.setAddress(address);
            cooperator.setContact(contact);
            cooperator.setContactPhone(contactPhone);
            cooperator.setStatusCode(0);
            cooperator.setProvince(province);
            cooperator.setCity(city);
            cooperator.setDistrict(district);
            cooperator.setLongitude(longitude);
            cooperator.setLatitude(latitude);
            cooperatorService.save(cooperator);
            msg.setData(cooperator);

            //创建商户下属管理员账户
            CoopAccount coopAccount = new CoopAccount();
            coopAccount.setShortname(shortname);
            coopAccount.setPhone(phone);
            coopAccount.setPassword(coopAccount.encryptPassword("123456"));
            coopAccount.setCooperatorId(cooperator.getId());
            coopAccount.setIsMain(true);
            coopAccount.setCreateTime(new Date());
            coopAccountService.save(coopAccount);
        }
        return msg;
    }
}
