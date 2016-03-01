package com.autobon.platform.controller.technician;

import com.autobon.platform.utils.ArrayUtil;
import com.autobon.platform.utils.IdentityUtil;
import com.autobon.share.JsonMessage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by yuh on 2016/2/16.
 */
@RestController
@RequestMapping("/api/mobile")
public class CertificateController {
    @Value("${com.autobon.skill}")
    private String skill;

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private ArrayUtil arrayUtil;

    @Autowired
    private IdentityUtil identityUtil;

    @RequestMapping(value="/technician/commitCertificate", method = RequestMethod.POST)
    public JsonMessage commitCertificate(
            @RequestParam("name") String name,
            @RequestParam("idNo") String idNo,
            @RequestParam("skillArray") String[] skillArray,
            @RequestParam("bank") String bank,
            @RequestParam("bankCardNo") String bankCardNo) {

        JsonMessage jsonMessage = new JsonMessage(true,"commitCertificate");
        ArrayList<String> messages = new ArrayList<>();

        String skill = arrayUtil.arrayToString(skillArray);

        if(!identityUtil.checkIDCard(idNo)){
            jsonMessage.setError("ILLEGAL_PARAM");
            messages.add("身份证号码有误");
        }

        if (messages.size() > 0) {
            jsonMessage.setResult(false);
            jsonMessage.setMessage(messages.stream().collect(Collectors.joining(",")));
        } else {
            //Technician technician = technicianService.getById(id);
            Technician technician = (Technician) SecurityContextHolder.getContext().getAuthentication().getDetails();
            //Technician technician = new Technician();
            technician.setName(name);
            technician.setIdNo(idNo);
            technician.setSkill(skill);
            technician.setBank(bank);
            technician.setBankCardNo(bankCardNo);

            technicianService.save(technician);
            jsonMessage.setData(technician);
        }

        return jsonMessage;
    }

    @RequestMapping(value="/technician/getCertificate", method = RequestMethod.POST)
    public JsonMessage getCertificate() {
        JsonMessage jsonMessage = new JsonMessage(true,"getCertificate");

        Technician technician = (Technician) SecurityContextHolder.getContext().getAuthentication().getDetails();
        jsonMessage.setData(technician);

        return jsonMessage;
    }


    @RequestMapping(value = "/technician/changeBankCard", method = RequestMethod.POST)
    public JsonMessage changeBankCard(
            @RequestParam("bank") String bank,
            @RequestParam("bankCardNo") String bankCardNo) {
        JsonMessage jsonMessage = new JsonMessage(true,"changeBankCard");

        Technician technician = (Technician) SecurityContextHolder.getContext().getAuthentication().getDetails();
        technician.setBank(bank);
        technician.setBankCardNo(bankCardNo);
        technicianService.save(technician);

        return jsonMessage;
    }

}
