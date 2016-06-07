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
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/3/1.
 */
@RestController("adminTechnicianController")
@RequestMapping("/api/web/admin/technician")
public class TechnicianController {
    @Autowired
    TechnicianService technicianService;
    @Autowired
    DetailedTechnicianService detailedTechnicianService;
    @Autowired
    TechLocationService techLocationService;
    @Autowired
    LocationService locationService;
    @Autowired
    ApplicationEventPublisher publisher;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search(
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) Technician.Status status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return new JsonMessage(true, "", "", new JsonPage<>(technicianService.find(
                phone, name, status, page, pageSize)));
    }

    @RequestMapping(value = "/{techId:\\d+}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("techId") int techId) {
        return new JsonMessage(true, "", "", detailedTechnicianService.get(techId));
    }

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
                              @RequestParam(value = "skill") String skill) {

        if (!Pattern.matches("^\\d{11}$", phone)) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "手机号格式错误");
        } else if (technicianService.getByPhone(phone) != null) {
            return new JsonMessage(false, "OCCUPIED_ID", "手机号已被注册");
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
        return new JsonMessage(true, "", "", technician);
    }

    @RequestMapping(value = "/verify/{techId:\\d+}", method = RequestMethod.POST)
    public JsonMessage verify(
            @PathVariable("techId") int techId,
            @RequestParam("verified") boolean verified,
            @RequestParam(value = "verifyMsg", defaultValue = "") String verifyMsg) throws IOException {
        Technician tech = technicianService.get(techId);
        if (tech == null) return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个技师");

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

    /**
     * 增加技师
     *
     * @param phone
     * @param password
     * @param name
     * @param gender
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonMessage saveTechnician(@RequestParam(value = "phone") String phone,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "name") String name,
                                      @RequestParam(value = "gender") String gender
    ) {

        if (!Pattern.matches("^\\d{11}$", phone)) {
            return new JsonMessage(false, "ILLEGAL_REPEAT", "手机号格式错误");
        } else if (technicianService.getByPhone(phone) != null) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "手机号已经注册");
        } else {
            Technician technician = new Technician();
            technician.setPhone(phone);
            technician.setName(name);
            technician.setGender(gender);
            technician.setPassword(password);
            technicianService.save(technician);
            return new JsonMessage(true, "", "新建技师成功", technician);
        }
    }

    /**
     * 删除技师操作
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public JsonMessage delectById(@PathVariable("id") int id) {
        if (technicianService.get(id) == null) {
            return new JsonMessage(false, "对象不存在");
        } else {
            technicianService.deleteById(id);
            return new JsonMessage(true, "", "删除技师成功", technicianService.get(id));
        }
    }

    /**
     * 查询所有技师
     *
     * @return
     */
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public JsonMessage selectAll() {
        return new JsonMessage(true, "", "所有技师", technicianService.findList());
    }

    /**
     * 修改技师
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public JsonMessage updateById(@PathVariable("id") int id,
                                  @RequestParam(value = "phone") String phone,
                                  @RequestParam(value = "password") String password,
                                  @RequestParam(value = "name") String name,
                                  @RequestParam(value = "gender") String gender) {
        technicianService.updatebyId(phone, password, name, gender, id);
        return new JsonMessage(true, "", "修改技师成功", technicianService.get(id));
    }

}
