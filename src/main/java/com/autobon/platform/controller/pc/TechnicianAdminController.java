package com.autobon.platform.controller.pc;

import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by wh on 2016/11/22.
 */

@RestController
@RequestMapping("/api/web/admin")
public class TechnicianAdminController {

    @Autowired
    TechnicianService technicianService;

    /**
     * 查询技师
     * @param tid
     * @return
     */
    @RequestMapping(value = "/v2/technician/{tid}", method = RequestMethod.GET)
    public JsonResult findById(@PathVariable("tid") int tid){
        Technician technician  = technicianService.get(tid);
        if(technician != null){
            return new JsonResult(true, technician);
        }
        return  new JsonResult(false, "技师不存在");
    }


    /**
     * 修改技师
     * @param tid 技师ID
     * @param name 技师名称
     * @param gender 性别
     * @param avatar 头像
     * @param idNo 身份证号
     * @param idPhoto 身份证图片
     * @param bank 银行
     * @param bankAddress 开户行地址
     * @param bankCardNo 银行卡号
     * @param filmLevel 贴膜级别
     * @param filmWorkingSeniority 贴膜年限
     * @param carCoverLevel 隐形车衣级别
     * @param carCoverWorkingSeniority 隐形车衣年限
     * @param colorModifyLevel 车身改色级别
     * @param colorModifyWorkingSeniority 车身改色年限
     * @param beautyLevel  美容级别
     * @param beautyWorkingSeniority 美容年限
     * @param resume 简介
     * @param reference 推荐人
     * @return
     */
    @RequestMapping(value = "/v2/technician/{tid}", method = RequestMethod.POST)
    public JsonResult modify(@PathVariable("tid") int tid,
                             @RequestParam(value = "name",required = false) String name,
                             @RequestParam(value = "gender",required = false) String gender,
                             @RequestParam(value = "avatar",required = false) String avatar,
                             @RequestParam(value = "idNo",required = false) String idNo,
                             @RequestParam(value = "idPhoto",required = false) String idPhoto,
                             @RequestParam(value = "bank",required = false) String bank,
                             @RequestParam(value = "bankAddress",required = false) String bankAddress,
                             @RequestParam(value = "bankCardNo",required = false) String bankCardNo,
                             @RequestParam(value = "filmLevel",required = false) Integer filmLevel,
                             @RequestParam(value = "filmWorkingSeniority",required = false) Integer filmWorkingSeniority,
                             @RequestParam(value = "carCoverLevel",required = false) Integer carCoverLevel,
                             @RequestParam(value = "carCoverWorkingSeniority",required = false) Integer carCoverWorkingSeniority,
                             @RequestParam(value = "colorModifyLevel",required = false) Integer colorModifyLevel,
                             @RequestParam(value = "colorModifyWorkingSeniority",required = false) Integer colorModifyWorkingSeniority,
                             @RequestParam(value = "beautyLevel",required = false) Integer beautyLevel,
                             @RequestParam(value = "beautyWorkingSeniority",required = false) Integer beautyWorkingSeniority,
                             @RequestParam(value = "resume",required = false) String resume,
                             @RequestParam(value = "reference",required = false) String reference,
                             @RequestParam(value = "workStatus",required = false) Integer workStatus){
        Technician technician  = technicianService.get(tid);
        if(technician != null){
            technician.setName(name == null ? technician.getName() : name);
            technician.setGender(gender == null ? technician.getGender() : gender);
            technician.setAvatar(avatar == null ? technician.getAvatar() : avatar);
            technician.setIdNo(idNo == null ? technician.getIdNo() : idNo);
            technician.setIdPhoto(idPhoto == null ? technician.getIdPhoto() : idPhoto);
            technician.setBank(bank == null ? technician.getBank() : bank);
            technician.setBankAddress(bankAddress == null ? technician.getBankAddress() : bankAddress);
            technician.setBankCardNo(bankCardNo == null ? technician.getBankCardNo() : bankCardNo);
            technician.setFilmLevel(filmLevel == null ? technician.getFilmLevel() : filmLevel);
            technician.setFilmWorkingSeniority(filmWorkingSeniority == null ? technician.getFilmWorkingSeniority() : filmWorkingSeniority);
            technician.setCarCoverLevel(carCoverLevel == null ? technician.getCarCoverLevel() : carCoverLevel);
            technician.setCarCoverWorkingSeniority(carCoverWorkingSeniority == null ? technician.getCarCoverWorkingSeniority() : carCoverWorkingSeniority);
            technician.setColorModifyLevel(colorModifyLevel == null ? technician.getColorModifyLevel() : colorModifyLevel);
            technician.setColorModifyWorkingSeniority(colorModifyWorkingSeniority == null ? technician.getColorModifyWorkingSeniority() : colorModifyWorkingSeniority);
            technician.setBeautyLevel(beautyLevel == null ? technician.getBeautyLevel() : beautyLevel);
            technician.setBeautyWorkingSeniority(beautyWorkingSeniority == null ? technician.getBeautyWorkingSeniority() : beautyWorkingSeniority);
            technician.setResume(resume == null ? technician.getResume() : resume);
            technician.setReference(reference == null? technician.getReference(): reference);
            technician.setWorkStatus(workStatus == null ? technician.getWorkStatus(): workStatus);
            technician = technicianService.save(technician);
            return new JsonResult(true, technician);
        }

        return  new JsonResult(false, "技师不存在");
    }



}
