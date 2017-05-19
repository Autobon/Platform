package com.autobon.platform.controller.technician;

import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import com.autobon.technician.vo.TechnicianShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by yuh on 2016/2/16.
 */
@RestController
@RequestMapping("/api/mobile")
public class CertificateController {

    @Autowired
    private TechnicianService technicianService;

    @RequestMapping(value="/technician/certificate", method = RequestMethod.POST)
    public JsonMessage commitCertificate(HttpServletRequest request,
            @RequestParam("name") String name,
            @RequestParam("idNo") String idNo,
            @RequestParam("skills") String skills,
            @RequestParam("bank") String bank,
            @RequestParam("idPhoto") String idPhoto,
            @RequestParam("bankCardNo") String bankCardNo) {
        idNo = idNo.toUpperCase();
        if (!Pattern.matches("(\\d{15})|(\\d{17}[0-9X])", idNo))
            return new JsonMessage(false, "ILLEGAL_IDNO", "身份证号码有误");
        if (!Pattern.matches("\\d+(,\\d+)*", skills))
            return new JsonMessage(false, "ILLEGAL_SKILLS", "参数skills格式错误, 请填写技能编号并用分号分隔");
        if (Pattern.matches("\\d.*", name))
            return new JsonMessage(false, "ILLEGAL_NAME", "姓名不能以数字开头");

        Technician technician = (Technician) request.getAttribute("user");
        technician.setName(name);

        technician.setIdNo(idNo);
        technician.setIdPhoto(idPhoto);
        technician.setSkill(skills);
        technician.setBank(bank);
        technician.setBankCardNo(bankCardNo);
        technician.setStatus(Technician.Status.IN_VERIFICATION);
        technician.setRequestVerifyAt(new Date());
        technicianService.save(technician);

        return new JsonMessage(true, "", "", technician);
    }

    @RequestMapping(value = "/technician/changeBankCard", method = RequestMethod.POST)
    public JsonMessage changeBankCard(HttpServletRequest request,
            @RequestParam("bank") String bank,
            @RequestParam("bankCardNo") String bankCardNo) {
        Technician technician = (Technician) request.getAttribute("user");
        technician.setBank(bank);
        technician.setBankCardNo(bankCardNo);
        technicianService.save(technician);
        return new JsonMessage(true);
    }


    /**
     * 车邻邦二期
     * 技师认证
     * @param technicianShow
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/v2/technician/certificate", method = RequestMethod.POST)
    public JsonResult authentication(@RequestBody TechnicianShow technicianShow,
                                     HttpServletRequest request)throws Exception {

        String idNo = technicianShow.getIdNo().toUpperCase();
        if (!Pattern.matches("(\\d{15})|(\\d{17}[0-9X])", idNo)) {
            return new JsonResult(false, "身份证号码有误");

        }
        if (Pattern.matches("\\d.*", technicianShow.getName())) {
            return new JsonResult(false, "姓名不能以数字开头");
        }
        Technician technician = (Technician) request.getAttribute("user");
        if(technician == null){
            return new JsonResult(false, "请重新登陆");
        }
        technician.setName(technicianShow.getName());
        technician.setIdNo(idNo);
        technician.setIdPhoto(technicianShow.getIdPhoto());
        technician.setBank(technicianShow.getBank());
        technician.setBankCardNo(technicianShow.getBankCardNo());
        technician.setReference(technicianShow.getReference());
        technician.setBankAddress(technicianShow.getBankAddress());
        technician.setFilmLevel(technicianShow.getFilmLevel());
        technician.setFilmWorkingSeniority(technicianShow.getFilmWorkingSeniority());
        technician.setCarCoverLevel(technicianShow.getFilmWorkingSeniority());
        technician.setCarCoverWorkingSeniority(technicianShow.getCarCoverWorkingSeniority());
        technician.setColorModifyLevel(technicianShow.getColorModifyLevel());
        technician.setColorModifyWorkingSeniority(technicianShow.getColorModifyWorkingSeniority());
        technician.setBeautyLevel(technicianShow.getBeautyLevel());
        technician.setBeautyWorkingSeniority(technicianShow.getBeautyWorkingSeniority());
        technician.setResume(technicianShow.getResume());
        technician.setStatus(Technician.Status.IN_VERIFICATION);
        technician.setRequestVerifyAt(new Date());
        technicianService.save(technician);

        return new JsonResult(true, technician);
    }



}
