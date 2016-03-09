package com.autobon.platform.controller.admin;

import com.autobon.getui.PushService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController("adminTechnicianController")
@RequestMapping("/api/web/admin/technician")
public class TechnicianController {
    @Autowired TechnicianService technicianService;
    @Autowired PushService pushService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search(@RequestParam(value = "query", defaultValue = "") String query,
                              @RequestParam(value = "page",     defaultValue = "1" )  int page,
                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        if ("".equals(query)) {
            return new JsonMessage(true, "", "",
                    new JsonPage<>(technicianService.findAll(page, pageSize)));
        } else if (Pattern.matches("^[0-9]+$", query)) {
            Technician t = technicianService.getByPhone(query);
            ArrayList<Technician> list = new ArrayList<>();
            if (t != null) {
                list.add(t);
                return new JsonMessage(true, "", "", new JsonPage<>(1, 20, 1, 1, 1, list));
            } else {
                return new JsonMessage(true, "", "", new JsonPage<>(1, 20, 0, 0, 0, list));
            }
        } else {
            return new JsonMessage(true, "", "",
                    new JsonPage<>(technicianService.findByName(query, page, pageSize)));
        }
    }

    @RequestMapping(value = "/{techId:[\\d]+}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("techId") int techId) {
        return new JsonMessage(true, "", "", technicianService.get(techId));
    }

    @RequestMapping(value = "/{techId:[\\d]+}", method = RequestMethod.POST)
    public JsonMessage update(@PathVariable("techId") int techId,
                              @RequestParam(value="phone",required = true) String phone,
                              @RequestParam(value="name",required = true) String name,
                              @RequestParam(value="gender",required = true) String gender,
                              @RequestParam(value = "idNo",required = true) String idNo,
                              @RequestParam(value = "idPhoto",required = true) String idPhoto,
                              @RequestParam(value = "bank",required = true) String bank,
                              @RequestParam(value = "bankAddress",required = true) String bankAddress,
                              @RequestParam(value = "bankCardNo",required = true) String bankCardNo,
                              @RequestParam(value = "skill",required = true) String skill) {

        if (!Pattern.matches("^\\d{11}$", phone)) {
            return  new JsonMessage(false,"ILLEGAL_PARAM","手机号格式错误");
        } else if (technicianService.getByPhone(phone) != null) {
            return new JsonMessage(false,"OCCUPIED_ID","手机号已被注册");
        }

        idNo = idNo.toUpperCase();
        if (!Pattern.matches("^(\\d{15})|(\\d{17}[0-9X])$", idNo))
            return new JsonMessage(false, "ILLEGAL_PARAM", "身份证号码有误");
        if (!Pattern.matches("^\\d+(,\\d+)*$", skill))
            return new JsonMessage(false, "ILLEGAL_PARAM", "参数skill格式错误, 请填写技能编号并用分号分隔");

        Technician technician = technicianService.get(techId);
        technician.setPhone(phone);
        technician.setName(name);
        technician.setGender(gender);
        technician.setIdNo(idNo);
        technician.setIdPhoto(idPhoto);
        technician.setBank(bank);
        technician.setBankAddress(bankAddress);
        technician.setBankCardNo(bankCardNo);
        technician.setSkill(skill);
        technicianService.save(technician);
        return new JsonMessage(true,"","",technician);
    }


    @RequestMapping(value = "/verify/{techId:[\\d]+}", method = RequestMethod.POST)
    public JsonMessage verify(
            @PathVariable("techId") int techId,
            @RequestParam("verified") boolean verified,
            @RequestParam(value = "verifyMsg", defaultValue = "") String verifyMsg) throws IOException {
        Technician tech = technicianService.get(techId);
        if (tech != null) {
            if (verified) {
                tech.setStatus(Technician.Status.VERIFIED);
                String title = "你已通过技师资格认证";
                pushService.pushToSingle(tech.getPushId(), title,
                        "{\"action\":\"VERIFICATION_SUCCEED\",\"title\":\"" + title + "\"}",
                        3*24*3600);
            } else {
                if (verifyMsg.equals("")) {
                    return new JsonMessage(false, "INSUFFICIENT_PARAM", "请填写认证失败原因");
                }
                tech.setStatus(Technician.Status.REJECTED);
                String title = "你的技师资格认证失败: " + verifyMsg;
                pushService.pushToSingle(tech.getPushId(), title,
                        "{\"action\":\"VERIFICATION_FAILED\",\"title\":\"" + title + "\"}",
                        3*24*3600);
            }
            tech.setVerifyAt(new Date());
            tech.setVerifyMsg(verifyMsg);
            technicianService.save(tech);
            return new JsonMessage(true);
        } else {
            return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个技师");
        }
    }
}
