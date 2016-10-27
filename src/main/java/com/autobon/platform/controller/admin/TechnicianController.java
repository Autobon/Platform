package com.autobon.platform.controller.admin;

import com.autobon.platform.listener.Event;
import com.autobon.platform.listener.TechnicianEventListener;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.DetailedTechnicianService;
import com.autobon.technician.service.LocationService;
import com.autobon.technician.service.TechLocationService;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController("adminTechnicianController")
@RequestMapping("/api/web/admin/technician")
public class TechnicianController {
    @Autowired TechnicianService technicianService;
    @Autowired DetailedTechnicianService detailedTechnicianService;
    @Autowired TechLocationService techLocationService;
    @Autowired LocationService locationService;
    @Autowired ApplicationEventPublisher publisher;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search(
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) Technician.Status status,
            @RequestParam(value = "page", defaultValue = "1" )  int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return new JsonMessage(true, "", "", new JsonPage<>(technicianService.find(
                phone, name, status, page, pageSize)));
    }

    @RequestMapping(value = "/{techId:\\d+}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("techId") int techId) {
        return new JsonMessage(true, "", "", detailedTechnicianService.get(techId));
    }

    /**
     * 技师申请认证
     * @param techId 技师ID
     * @param phone 手机号码
     * @param name 技师姓名
     * @param gender 技师性别
     * @param idNo 身份证号
     * @param idPhoto 手持身份证图片
     * @param bank 银行
     * @param bankAddress 银行开户行
     * @param bankCardNo 银行卡号
     * @param skill 技能  逗号分隔
     * @param filmLevel  隔热膜星级
     * @param carCoverLevel   车衣星级
     * @param colorModifyLevel   改色星级
     * @param beautyLevel 美容星级
     * @param resume 个人简介
     * @return
     */
    @RequestMapping(value = "/{techId:\\d+}", method = RequestMethod.POST)
    public JsonMessage update(@PathVariable("techId") int techId,
                              @RequestParam(value = "phone") String phone,
                              @RequestParam(value = "name") String name,
                              @RequestParam(value = "gender") String gender,
                              @RequestParam(value = "idNo") String idNo,
                              @RequestParam(value = "idPhoto") String idPhoto,
                              @RequestParam(value = "bank") String bank,
                              @RequestParam(value = "bankAddress") String bankAddress,
                              @RequestParam(value = "bankCardNo") String bankCardNo,
                              @RequestParam(value = "skill") String skill,
                              @RequestParam(value = "filmLevel", required = false) int filmLevel,
                              @RequestParam(value = "carCoverLevel", required = false) int carCoverLevel,
                              @RequestParam(value = "colorModifyLevel", required = false) int colorModifyLevel,
                              @RequestParam(value = "beautyLevel", required = false) int beautyLevel,
                              @RequestParam(value = "resume", required = false) String resume,
                              @RequestParam(value = "reference", required = false) String reference)throws Exception{

        if (!Pattern.matches("^\\d{11}$", phone)) {
            return  new JsonMessage(false,"ILLEGAL_PARAM","手机号格式错误");
        }

        if (technicianService.getByPhone(phone) != null) {
            return new JsonMessage(false,"OCCUPIED_ID","手机号已被注册");
        }

        if (!Pattern.matches("^(\\d{15})|(\\d{17}[0-9X])$",  idNo.toUpperCase())) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "身份证号码有误");
        }

        if (!Pattern.matches("^\\d+(,\\d+)*$", skill)) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "参数skill格式错误, 请填写技能编号并用分号分隔");
        }

        Technician technician = technicianService.get(techId);
        technician.setPhone(phone);
        technician.setName(name);
        technician.setGender(gender);
        technician.setIdNo(idNo.toUpperCase());
        technician.setIdPhoto(idPhoto);
        technician.setBank(bank);
        technician.setBankAddress(bankAddress);
        technician.setBankCardNo(bankCardNo);
        technician.setSkill(skill);
        technician.setFilmLevel(filmLevel);
        technician.setCarCoverLevel(carCoverLevel);
        technician.setColorModifyLevel(colorModifyLevel);
        technician.setBeautyLevel(beautyLevel);
        technician.setResume(resume);
        technician.setReference(reference);
        technicianService.save(technician);
        return new JsonMessage(true,"","",technician);
    }


    /**
     * 后台认证
     * @param techId
     * @param verified
     * @param verifyMsg
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/verify/{techId:\\d+}", method = RequestMethod.POST)
    public JsonMessage verify(@PathVariable("techId") int techId,
                              @RequestParam("verified") boolean verified,
                              @RequestParam(value = "verifyMsg", defaultValue = "") String verifyMsg) throws IOException {


        Technician tech = technicianService.get(techId);
        if (tech == null){
            return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个技师");
        }

        if (verified) {
            tech.setVerifyMsg(null);
            tech.setStatus(Technician.Status.VERIFIED);
        } else {
            if (verifyMsg.equals("")) {
                return new JsonMessage(false, "INSUFFICIENT_PARAM", "请填写认证失败原因");
            }
            tech.setStatus(Technician.Status.REJECTED);
            tech.setVerifyMsg(verifyMsg);
        }
        tech.setVerifyAt(new Date());
        technicianService.save(tech);
        publisher.publishEvent(new TechnicianEventListener.TechnicianEvent(tech, Event.Action.VERIFIED));
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/mapview", method = RequestMethod.GET)
    public JsonMessage getTechnicianLocations(
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "300") int pageSize) {
        if (pageSize > 500) pageSize = 500;
        return new JsonMessage(true, "", "", new JsonPage<>(techLocationService.findByDistinctTech(province, city, page, pageSize)));
    }

    @RequestMapping(value = "/maptrack/{techId:\\d+}", method = RequestMethod.GET)
    public JsonMessage getTechnicianTrack(@PathVariable("techId") int techId,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return new JsonMessage(true, "", "", new JsonPage<>(locationService.findByTechId(techId, page, pageSize)));
    }
}
