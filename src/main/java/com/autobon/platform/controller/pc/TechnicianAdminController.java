package com.autobon.platform.controller.pc;

import com.autobon.order.entity.WorkDetail;
import com.autobon.order.repository.OrderProductRepository;
import com.autobon.order.repository.WorkDetailRepository;
import com.autobon.order.service.WorkDetailService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.JsonResult;
import com.autobon.staff.entity.Staff;
import com.autobon.technician.entity.TechCashApply;
import com.autobon.technician.entity.TechFinance;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechCashApplyService;
import com.autobon.technician.service.TechFinanceService;
import com.autobon.technician.service.TechnicianService;
import com.autobon.technician.vo.TechCashApplyShow;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wh on 2016/11/22.
 */

@RestController
@RequestMapping("/api/web/admin")
public class TechnicianAdminController {

    @Autowired
    TechnicianService technicianService;

    @Autowired
    TechCashApplyService techCashApplyService;

    @Autowired
    TechFinanceService techFinanceService;

    @Autowired
    WorkDetailRepository workDetailRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @RequestMapping(value = "/v2/test/ty", method = RequestMethod.GET)
    public JsonResult gai(){
        List<WorkDetail> workDetails = workDetailRepository.findAll();
        if(workDetails !=null && workDetails.size()> 0){

            for(WorkDetail workDetail: workDetails){

                float cost = 0;
                int orderId = workDetail.getOrderId();
                if(workDetail.getProject1() != null&&workDetail.getPosition1()!=null){
                    int projectId = workDetail.getProject1();
                    String positionId = workDetail.getPosition1();
                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    cost += total;

                }
                if(workDetail.getProject2() != null&&workDetail.getPosition2()!=null){
                    int projectId = workDetail.getProject2();
                    String positionId = workDetail.getPosition2();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }

                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    cost += total;
                }
                if(workDetail.getProject3() != null&&workDetail.getPosition3()!=null){
                    int projectId = workDetail.getProject3();
                    String positionId = workDetail.getPosition3();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    cost += total;
                }
                if(workDetail.getProject4() != null&&workDetail.getPosition4()!=null){
                    int projectId = workDetail.getProject4();
                    String positionId = workDetail.getPosition4();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    cost += total;
                }

                if(workDetail.getProject5() != null&&workDetail.getPosition5()!=null){
                    int projectId = workDetail.getProject5();
                    String positionId = workDetail.getPosition5();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    cost += total;
                }

                if(workDetail.getProject6() != null&&workDetail.getPosition6()!=null){
                    int projectId = workDetail.getProject6();
                    String positionId = workDetail.getPosition6();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    cost += total;
                }

                workDetail.setTotalCost(cost);
                workDetailRepository.save(workDetail);
            }
        }
        return  new JsonResult(true, "ok");
    }

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
            technician.setReference(reference == null ? technician.getReference() : reference);
            technician.setWorkStatus(workStatus == null ? technician.getWorkStatus(): workStatus);
            String skill = "";
            if(filmLevel!=null&& filmLevel!= 0){
                skill+="1";
            }

            if(carCoverLevel!=null&& carCoverLevel!= 0){
                if(skill.length() == 0) {
                    skill += "2";
                }else{
                    skill +=",2";
                }
            }

            if(colorModifyLevel!=null&& colorModifyLevel!= 0) {
                if (skill.length() == 0) {
                    skill += "3";
                } else {
                    skill += ",3";
                }

            }
            if(beautyLevel!=null&& beautyLevel!= 0) {
                if (skill.length() == 0) {
                    skill += "4";
                } else {
                    skill += ",4";
                }
            }

                technician.setSkill(skill);

            technician = technicianService.save(technician);
            return new JsonResult(true, technician);
        }

        return  new JsonResult(false, "技师不存在");
    }





    @RequestMapping(value = "/technician/download", method = RequestMethod.GET)
    public void download(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {




        List<Technician> list  = technicianService.findAll();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("技师信息表");

        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(10, 15 * 256);
        sheet.setColumnWidth(11, 15 * 256);
        sheet.setColumnWidth(12, 15 * 256);
        sheet.setColumnWidth(13, 15 * 256);
        sheet.setColumnWidth(14, 15 * 256);
        sheet.setColumnWidth(15, 15 * 256);
        sheet.setColumnWidth(16, 15 * 256);
        sheet.setColumnWidth(17, 15 * 256);
        sheet.setColumnWidth(18, 15 * 256);


        HSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("手机号");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("身份证号");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("开户银行");
        cell = row.createCell(5);
        cell.setCellValue("银行卡号");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("注册时间");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("申请认证时间");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("认证日期");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("账户状态");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("推荐人手机");
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellValue("隔热膜星级");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("隔热膜年限");
        cell.setCellStyle(style);


        cell = row.createCell(13);
        cell.setCellValue("美容星级");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("美容年限");
        cell.setCellStyle(style);


        cell = row.createCell(15);
        cell.setCellValue("车衣星级");
        cell.setCellStyle(style);
        cell = row.createCell(16);
        cell.setCellValue("车衣年限");
        cell.setCellStyle(style);


        cell = row.createCell(17);
        cell.setCellValue("改色星级");
        cell.setCellStyle(style);
        cell = row.createCell(18);
        cell.setCellValue("改色年限");
        cell.setCellStyle(style);





        //新增数据行，并且设置单元格数据


        //新增数据行，并且设置单元格数据
        if(list != null && list.size() > 0) {
            int rowNum = 1;
            for (Technician technician : list) {

                row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(technician.getId());
                row.createCell(1).setCellValue(technician.getName());
                row.createCell(2).setCellValue(technician.getPhone());

                row.createCell(3).setCellValue(technician.getIdNo());
                row.createCell(4).setCellValue(technician.getBank());
                row.createCell(5).setCellValue(technician.getBankCardNo());

                row.createCell(6).setCellValue(technician.getCreateAt() == null? "":technician.getCreateAt()+"");
                row.createCell(7).setCellValue(technician.getRequestVerifyAt() == null? "":technician.getRequestVerifyAt()+"");
                row.createCell(8).setCellValue(technician.getVerifyAt() == null? "":technician.getVerifyAt()+"");




                if(technician.getStatus().equals(Technician.Status.NEWLY_CREATED)){
                    row.createCell(9).setCellValue("新注册");
                }else if(technician.getStatus().equals(Technician.Status.IN_VERIFICATION)){
                    row.createCell(9).setCellValue("认证中");
                }else if(technician.getStatus().equals(Technician.Status.VERIFIED)){
                    row.createCell(9).setCellValue("认证通过");
                }else if(technician.getStatus().equals(Technician.Status.REJECTED)){
                    row.createCell(9).setCellValue("认证失败");
                }else{
                    row.createCell(9).setCellValue("帐户禁用");
                }


                row.createCell(10).setCellValue(technician.getReference());

                row.createCell(11).setCellValue(technician.getFilmLevel()+"星");
                row.createCell(12).setCellValue(technician.getFilmWorkingSeniority()+"年");
                row.createCell(13).setCellValue(technician.getBeautyLevel()+"星");
                row.createCell(14).setCellValue(technician.getBeautyWorkingSeniority()+"年");
                row.createCell(15).setCellValue(technician.getCarCoverLevel()+"星");
                row.createCell(16).setCellValue(technician.getCarCoverWorkingSeniority()+"年");
                row.createCell(17).setCellValue(technician.getColorModifyLevel()+"星");
                row.createCell(18).setCellValue(technician.getColorModifyWorkingSeniority()+"年");

                rowNum++;
            }
        }

        response.reset();
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=technician"
                + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("导出失败");
        }



    }


    /**
     * 查询提现申请列表
     * @param techId
     * @param techName
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/cash/apply", method = RequestMethod.GET)
    public JsonMessage getListApply(HttpServletRequest request,
                                    @RequestParam(value = "techId",required = false) Integer techId,
                                    @RequestParam(value = "techName",required = false) String techName,
                                    @RequestParam(value = "state",required = false) Integer state,
                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize){
        Staff staff = (Staff) request.getAttribute("user");
        String res = "";
        if(staff != null){
            if(staff.getRole().equals("SUPER")){
                res = "able";
            }
        }
        return new JsonMessage(true, "" , res, new JsonPage<>(techCashApplyService.find(techName, techId, state, page, pageSize)));
    }

    /**
     * 执行支付
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/cash/apply/{id:\\d+}/pay", method = RequestMethod.POST)
    public JsonResult toApply(@PathVariable("id") int id,
                              HttpServletRequest request)throws Exception {
        Staff staff = (Staff) request.getAttribute("user");
        if(!staff.getRole().equals("SUPER")){
            return new JsonResult(false, "没有权限");
        }
        TechCashApply techCashApply = techCashApplyService.findById(id);
        if(techCashApply == null){
            return new JsonResult(false, "申请单不存在");
        }
        if(techCashApply.getState() == 2){
            return new JsonResult(false, "已全部提现，无法支付");
        }
        if(techCashApply.getState() == 3){
            return new JsonResult(false, "申请已取消，无法支付");
        }
        Technician t = technicianService.get(techCashApply.getTechId());
        if(t == null){
            return new JsonResult(false, "技师不存在");
        }
        TechFinance techFinance = techFinanceService.getByTechId(t.getId());
        if(techCashApply.getApplyMoney().compareTo(techFinance.getNotCash()) == 1){
            return new JsonResult(false, "金额超出未提现金额，无法支付");
        }
        techCashApply.setState(2);
        techCashApply.setPayment(techCashApply.getApplyMoney());
        techCashApply.setPayDate(new Date());
        TechCashApply res = techCashApplyService.save(techCashApply);
        if(res == null){
            return new JsonResult(false, "支付失败");
        }
        //修改该技师的流水
        //techFinance.setSumIncome(techFinance.getSumIncome().subtract(res.getPayment()));
        techFinance.setNotCash(techFinance.getNotCash().subtract(res.getPayment()));
        techFinance.setSumCash(techFinance.getSumCash().add(res.getPayment()));
        techFinance.setAlreadyApply(techFinance.getAlreadyApply().subtract(res.getPayment()));
        techFinanceService.save(techFinance);
        return new JsonResult(true, res);
    }


    /**
     * 技师账号禁用
     * @param id
     * @return
     */
    @RequestMapping(value = "/technician/ban/{id}", method = RequestMethod.POST)
    public JsonMessage technicianBan(@PathVariable("id") int id){

        Technician technician = technicianService.findById(id);

        if(technician == null){
            return new JsonMessage(false, "技师不存在");
        }
        if(technician.getStatus().getStatusCode() == 0){
            return new JsonMessage(false, "商户未认证，请先认证再操作");
        }

        technician.setStatus(Technician.Status.BANNED);
        technicianService.save(technician);

        return new JsonMessage(true, "禁用成功");
  }

    /**
     * 技师账号解禁
     * @param id
     * @return
     */
    @RequestMapping(value = "/technician/unban/{id}", method = RequestMethod.POST)
    public JsonMessage technicianUnBan(@PathVariable("id") int id){

        Technician technician = technicianService.findById(id);

        if(technician == null){
            return new JsonMessage(false, "技师不存在");
        }
        if(technician.getStatus().getStatusCode() == 0){
            return new JsonMessage(false, "商户未认证，不能解禁");
        }

        technician.setStatus(Technician.Status.VERIFIED);
        technicianService.save(technician);

        return new JsonMessage(true, "解禁成功");
    }

}
