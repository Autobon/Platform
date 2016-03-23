package com.autobon.platform.controller.coop;

import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.cooperators.service.ReviewCooperService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by yuh on 2016/3/15.
 */
@RestController
@RequestMapping("/api/mobile/coop")
public class CoopController {
    @Autowired
    MultipartResolver resolver;

    @Autowired
    private CooperatorService cooperatorService;

    @Autowired
    private ReviewCooperService reviewCooperService;

    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public JsonMessage check(HttpServletRequest request,
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
                             @RequestParam("contactPhone") String contactPhone){
        corporationIdNo = corporationIdNo.toUpperCase();
        if (!Pattern.matches("^(\\d{15})|(\\d{17}[0-9X])$", corporationIdNo))
            return new JsonMessage(false, "ILLEGAL_PARAM", "身份证号码有误");
        if (!Pattern.matches("^\\d{11}$", contactPhone)) {
            return  new JsonMessage(false,"ILLEGAL_PARAM","手机号格式错误");
        }
        Cooperator cooperator = (Cooperator)request.getAttribute("user");
        cooperator.setFullname(fullname);
        cooperator.setBusinessLicense(businessLicense);
        cooperator.setCorporationName(corporationName);
        cooperator.setCorporationIdNo(corporationIdNo);
        cooperator.setBussinessLicensePic(bussinessLicensePic);
        cooperator.setCorporationIdPicA(corporationIdPicA);
        cooperator.setLatitude(longitude);
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
        cooperator.setStatusCode(0);
        cooperatorService.save(cooperator);
        return new JsonMessage(true, "", "", cooperator);

    }

    @RequestMapping(value = "/bussinessLicensePic",method = RequestMethod.POST)
    public JsonMessage uploadBussinessLicensePic(HttpServletRequest request,
                                                 @RequestParam("file") MultipartFile file) throws  Exception{
        String path ="/uploads/coop/bussinessLicensePic";
        if (file == null || file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");
        JsonMessage msg = new JsonMessage(true);

        File dir = new File(request.getServletContext().getRealPath(path));
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;

        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        Cooperator cooperator = (Cooperator)request.getAttribute("user");
        cooperator.setBussinessLicensePic(path + "/" + filename);
        cooperatorService.save(cooperator);
        msg.setData(cooperator.getBussinessLicensePic());
        return msg;
    }

    @RequestMapping(value = "/corporationIdPicA",method = RequestMethod.POST)
    public JsonMessage uploadCorporationIdPicA(HttpServletRequest request,
                                                 @RequestParam("file") MultipartFile file) throws  Exception{
        String path ="/uploads/coop/corporationIdPicA";
        if (file == null || file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");
        JsonMessage msg = new JsonMessage(true);
        File dir = new File(request.getServletContext().getRealPath(path));
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;

        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        Cooperator cooperator = (Cooperator)request.getAttribute("user");
        cooperator.setCorporationIdPicA(path + "/" + filename);
        cooperatorService.save(cooperator);
        msg.setData(cooperator.getCorporationIdPicA());
        return msg;
    }

    @RequestMapping(value = "/getCoop",method = RequestMethod.GET)
    public JsonMessage getCoop(HttpServletRequest request) throws  Exception{
        Cooperator cooperator = (Cooperator)request.getAttribute("user");
        return new JsonMessage(true,"","",cooperator);
    }

    @RequestMapping(value = "/coopCheckResult",method = RequestMethod.GET)
    public JsonMessage coopCheckResult(HttpServletRequest request) throws  Exception{
        Map dataMap = new HashMap<>();

        Cooperator cooperator = (Cooperator)request.getAttribute("user");
        int coopId = cooperator.getId();
        ReviewCooper reviewCooper = reviewCooperService.getByCooperatorsId(coopId);

        dataMap.put("cooperator",cooperator);
        dataMap.put("reviewCooper",reviewCooper);

        return new JsonMessage(true,"","",dataMap);
    }

}
