package com.autobon.platform.controller.pc;



import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.order.entity.*;
import com.autobon.order.service.*;
import com.autobon.order.vo.*;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.JsonResult;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.LocationStatusService;
import com.autobon.technician.service.TechnicianService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by wh on 2016/11/15.
 */
@RestController
@RequestMapping("/api/web/admin/order")
public class OrderV2Controller {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderProductService orderProductService;

    @Autowired
    LocationStatusService locationStatusService;

    @Autowired
    CooperatorService cooperatorService;

    @Autowired
    WorkDetailService workDetailService;

    @Autowired
    ConstructionWasteService constructionWasteService;

    @Autowired
    TechnicianService technicianService;

    @Autowired
    ConstructionProjectService constructionProjectService;

    @Autowired
    OrderViewService orderViewService;

    @Autowired
    WorkDetailOrderViewService workDetailOrderViewService;

    private int length = 0;

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/{orderId}", method = RequestMethod.GET)
    public JsonResult getById(@PathVariable("orderId") int orderId) {
        OrderView orderShow = orderViewService.findById(orderId);
        List<WorkDetailShow> workDetailShowList = workDetailService.getByOrderId(orderId);
        if (workDetailShowList != null) {
            for (WorkDetailShow workDetailShow : workDetailShowList) {
                if (workDetailShow.getTechId() - orderShow.getTechId() == 0) {
                    workDetailShow.setIsMainTech(1);
                }
            }
            List<ConstructionWasteShow> constructionWasteShows = constructionWasteService.getByOrderId(orderId);
            orderShow.setWorkDetailShows(workDetailShowList);
            orderShow.setConstructionWasteShows(constructionWasteShows);

        }
        return new JsonResult(true, orderShow);
    }

    /**
     * 查询订单
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/{orderId}/info", method = RequestMethod.GET)
    public JsonResult getByOrderId(@PathVariable("orderId") int orderId) {
        return new JsonResult(true, orderService.get(orderId));
    }

    /**
     * 查询订单产品列表
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/{orderId}/product", method = RequestMethod.GET)
    public JsonResult getProduct(@PathVariable("orderId") int orderId) {

        return new JsonResult(true, orderProductService.findByOrderId(orderId));
    }


    /**
     * 修改订单
     *
     * @param orderId         订单ID
     * @param type            施工项目
     * @param statusCode      订单状态
     * @param remark          备注
     * @param positionLon     纬度
     * @param positionLat     经度
     * @param agreedStartTime 预约开工时间
     * @param agreedEndTime   最晚交车时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/v2/{orderId}", method = RequestMethod.POST)
    public JsonResult modifyOrder(@PathVariable("orderId") int orderId,
                                  @RequestParam(value = "type", required = false) String type,
                                  @RequestParam(value = "status", required = false) Order.Status statusCode,
                                  @RequestParam(value = "techId", required = false) Integer techId,
                                  @RequestParam(value = "remark", required = false) String remark,
                                  @RequestParam(value = "positionLon", required = false) String positionLon,
                                  @RequestParam(value = "positionLat", required = false) String positionLat,
                                  @RequestParam(value = "agreedStartTime", required = false) Long agreedStartTime,
                                  @RequestParam(value = "agreedEndTime", required = false) Long agreedEndTime) throws Exception {

        Order order = orderService.get(orderId);
        if (order == null) {
            return new JsonResult(false, "订单不存在");
        }

        if (statusCode != null) {
            order.setStatusCode(statusCode.getStatusCode());
        }

        if (agreedStartTime != null) {
            Date date = new Date(agreedStartTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateStr = sdf.format(date);
            order.setAgreedStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr));
        }

        if (agreedEndTime != null) {
            Date date = new Date(agreedEndTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateStr = sdf.format(date);
            order.setAgreedEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr));
        }

        if (techId != null) {
            if (order.getStatusCode() <= Order.Status.NEWLY_CREATED.getStatusCode()) {
                order.setStatusCode(Order.Status.TAKEN_UP.getStatusCode());
            }
            order.setMainTechId(techId);
        }

        order.setType(type == null ? order.getType() : type);
        //   order.setStatusCode(statusCode == null ? order.getStatusCode() : statusCode);
        order.setRemark(remark == null ? order.getRemark() : remark);
        order.setPositionLat(positionLat == null ? order.getPositionLat() : positionLat);
        order.setPositionLon(positionLon == null ? order.getPositionLon() : positionLon);
        //   order.setAgreedEndTime(agreedEndTime == null ? order.getAgreedEndTime() : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(agreedEndTime));
        //     order.setAgreedStartTime(agreedStartTime == null ? order.getStartTime() : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(agreedStartTime));
        orderService.save(order);
        return new JsonResult(true, order);
    }


    /**
     * 通过订单编号获取施工项目及对应的施工部位
     *
     * @param request
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/project/position/{orderId}", method = RequestMethod.GET)
    public JsonResult getProject(HttpServletRequest request,
                                 @PathVariable("orderId") int orderId) {

        try {
            Order order = orderService.get(orderId);

            if (order == null) {
                return new JsonResult(false, "没有这个订单");
            }
            return new JsonResult(true, orderService.getProject(orderId));

        } catch (Exception e) {
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 通过 施工项目查看对应的施工部位
     *
     * @param request
     * @param projectId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/project/{projectId}/position", method = RequestMethod.GET)
    public JsonResult getPosition(HttpServletRequest request,
                                  @PathVariable("projectId") int projectId) {

        try {
            return new JsonResult(true, constructionProjectService.findByProject(projectId));
        } catch (Exception e) {
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 保存订单产品
     *
     * @param orderId          订单ID
     * @param orderProductList 产品列表
     * @return
     */
    @RequestMapping(value = "/project/product/{orderId}", method = RequestMethod.POST)
    public JsonResult saveOrderProduct(@PathVariable("orderId") int orderId,
                                       @RequestBody List<OrderProduct> orderProductList) {

        Order order = orderService.get(orderId);

        if (order == null) {
            return new JsonResult(false, "没有这个订单");
        }

        if (orderProductList != null && orderProductList.size() > 0) {
            orderProductService.batchInsert(orderProductList);
            return new JsonResult(true, "保存成功");
        }

        return new JsonResult(false, "没有填写产品项目");
    }


    /**
     * 修改订单产品
     *
     * @param orderId          订单ID
     * @param orderProductList 订单产品列表
     * @return
     */
    @RequestMapping(value = "/project/product/{orderId}", method = RequestMethod.PUT)
    public JsonResult modifyOrderProduct(@PathVariable("orderId") int orderId,
                                         @RequestBody List<OrderProduct> orderProductList) {

        Order order = orderService.get(orderId);

        if (order == null) {
            return new JsonResult(false, "没有这个订单");
        }

        if (orderProductList != null && orderProductList.size() > 0) {
            orderProductService.save(orderProductList);
            return new JsonResult(true, "保存成功");
        }

        return new JsonResult(false, "没有填写产品项目");
    }


    /**
     * 通过订单编号或者施工项目及施工部位
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/project/product/{orderId}", method = RequestMethod.GET)
    public JsonResult getOrderProduct(@PathVariable("orderId") int orderId) {

        Order order = orderService.get(orderId);

        if (order == null) {
            return new JsonResult(false, "没有这个订单");
        }


        return new JsonResult(true, orderProductService.get(orderId));
    }


    /**
     * 后台指派技师 地图展示
     * 第一次加载数据时，无需传入经纬度 取商户坐标为中心点，需要传入 千米数
     * 拖动地图控件时将中心点 传入该API
     *
     * @param orderId   订单ID
     * @param longitude
     * @param latitude
     * @param kilometre
     * @return
     */
    @RequestMapping(value = "/technician/assign/{orderId}", method = RequestMethod.GET)
    public JsonResult assign(@PathVariable("orderId") int orderId,
                             @RequestParam(value = "longitude", required = false) String longitude,
                             @RequestParam(value = "latitude", required = false) String latitude,
                             @RequestParam(value = "kilometre", required = false) int kilometre) {

        Order order = orderService.get(orderId);

        if (order == null) {
            return new JsonResult(false, "没有这个订单");
        }
        Cooperator cooperator = cooperatorService.get(order.getCoopId());
        if (cooperator == null) {
            return new JsonResult(false, "商户不存在");
        }
        CoopTechnicianLocation coopTechnicianLocation = new CoopTechnicianLocation();
        coopTechnicianLocation.setCoopLatitude(cooperator.getLatitude());
        if (longitude == null && latitude == null) {
            coopTechnicianLocation.setCoopLongitude(cooperator.getLongitude());
            coopTechnicianLocation.setLocalStatuses(locationStatusService.getTechByDistance(coopTechnicianLocation.getCoopLatitude(), coopTechnicianLocation.getCoopLongitude(), kilometre));
        } else {
            coopTechnicianLocation.setCoopLongitude(cooperator.getLongitude());
            coopTechnicianLocation.setLocalStatuses(locationStatusService.getTechByDistance(latitude, longitude, kilometre));

        }
        return new JsonResult(true, coopTechnicianLocation);
    }


    /**
     * 查询技师
     *
     * @param query    查询内容 纯数字则查询手机 反之查询姓名
     * @param page     页码
     * @param pageSize 页面大小
     * @return JsonResult对象
     */
    @RequestMapping(value = "/technician/assign", method = RequestMethod.GET)
    public JsonResult getTechnician(@RequestParam(value = "query", required = false) String query,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        try {

            Page<Technician> technicians;
            String query1;
            if (query != null) {
                query1 = "%" + query + "%";
                if (Pattern.matches("\\d+", query)) {
                    technicians = technicianService.find(query1, null, page, pageSize);

                } else {
                    technicians = technicianService.find(null, query1, page, pageSize);
                }
            } else {
                technicians = technicianService.find(null, null, page, pageSize);
            }

            return new JsonResult(true, technicians);
        } catch (Exception e) {
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 后台指派技师
     *
     * @param orderId 订单ID
     * @param techId  技师ID
     * @return
     */
    @RequestMapping(value = "/{orderId}/technician/{techId}/assign", method = RequestMethod.POST)
    public JsonResult assign(@PathVariable("orderId") int orderId,
                             @PathVariable("techId") int techId) {

        Order order = orderService.get(orderId);
        if (order == null) {
            return new JsonResult(false, "没有这个订单");
        }

        Technician technician = technicianService.get(techId);
        if (technician != null && technician.getWorkStatus() == 3) {
            return new JsonResult(false, "技师未认证或休息状态，不可指派");
        }

        order.setReassignmentStatus(2);
        if (order.getStatusCode() == -10) {
            order.setStatusCode(Order.Status.TAKEN_UP.getStatusCode());
            order.setReassignmentStatus(0);
            order.setTakenTime(new Date());
        }

        order.setMainTechId(techId);

        orderService.save(order);
        return new JsonResult(true, order);
    }


    /**
     * 测试使用
     */
    @RequestMapping(value = "/v2/test", method = RequestMethod.GET)
    public void test() {

        List<WorkDetail> list = workDetailService.findAll();

        for(WorkDetail workDetail : list){
            if(workDetail.getPosition1() != null){
                String[] strings = workDetail.getPosition1().split(",");
                for(String s : strings){
                    OrderProduct orderProduct = orderProductService.findByOrderIdAndProjectAndPosition(workDetail.getOrderId(), workDetail.getProject1(), Integer.parseInt(s));
                    if(orderProduct != null && orderProduct.getWorkDetailId() != null){
                        orderProduct.setWorkDetailId(workDetail.getId());
                        orderProductService.save(orderProduct);
                    }
                }
            }
        }
    }


    /**
     * 导出EXCEL
     *
     * @param tech      技师电话或者名字
     * @param coopId    商户ID
     * @param startTime 订单开始时间
     * @param endTime   订单结束时间
     * @param status    订单状态
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/excel/download", method = RequestMethod.GET)
    public void download(@RequestParam(value = "orderNum", required = false) String orderNum,
                         @RequestParam(value = "orderCreator", required = false) String orderCreator,
                         @RequestParam(value = "orderType", required = false) Integer orderType,
                         @RequestParam(value = "orderStatus", required = false) Order.Status orderStatus,
                         @RequestParam(value = "sort", defaultValue = "id") String sort,
                         @RequestParam(value = "tech", required = false) String tech,
                         @RequestParam(value = "coopId", required = false) String coopId,
                         @RequestParam(value = "startTime", required = false) Long startTime,
                         @RequestParam(value = "endTime", required = false) Long endTime,
                         @RequestParam(value = "status", required = false) Order.Status status,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {


        String creatorName = null;
        String contactPhone = null;
        List<Integer> types = null;
        Integer statusCode = orderStatus != null ? orderStatus.getStatusCode() : null;


        if (orderCreator != null) {
            if (Pattern.matches("\\d+", orderCreator)) {
                contactPhone = orderCreator;
            } else {
                creatorName = orderCreator;
            }
        }

        if (orderType != null) {
            types = Arrays.asList(orderType);
        }

        Page<Order> orderPage;


        if (tech != null && !tech.equals("")) {
            List<Integer> ids = technicianService.find(tech);
            ids.add(-1);
            orderPage = orderService.findOrder(orderNum, creatorName, contactPhone,
                    ids, types, statusCode, sort, Sort.Direction.DESC, 1, 300);
        } else {
            orderPage = orderService.find(orderNum, creatorName, contactPhone,
                    types, statusCode, sort, Sort.Direction.DESC, 1, 300);
        }



            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面

            response.reset();

            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=order" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls");


            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("待结算清单表");

            HSSFCellStyle style = workbook.createCellStyle();
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

            HSSFRow row = sheet.createRow(0);
            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            sheet.setColumnWidth(2, 12 * 256);
            sheet.setColumnWidth(3, 17 * 256);

            HSSFCell cell;

            cell = row.createCell(0);
            cell.setCellValue("订单ID");
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellValue("订单编号");
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue("订单类型");
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellValue("车牌");
            cell.setCellStyle(style);
            cell = row.createCell(4);
            cell.setCellValue("车型");
            cell.setCellStyle(style);
            cell = row.createCell(5);
            cell.setCellValue("车架号");
            cell.setCellStyle(style);
            cell = row.createCell(6);
            cell.setCellValue("预约开始时间");
            cell.setCellStyle(style);
            cell = row.createCell(7);
            cell.setCellValue("下单人");
            cell.setCellStyle(style);
            cell = row.createCell(8);
            cell.setCellValue("下单时间");
            cell.setCellStyle(style);
            cell = row.createCell(9);
            cell.setCellValue("订单状态");
            cell.setCellStyle(style);


            cell = row.createCell(10);
            cell.setCellValue("补录状态");
            cell.setCellStyle(style);
            cell = row.createCell(11);
            cell.setCellValue("改派状态");
            cell.setCellStyle(style);


            //新增数据行，并且设置单元格数据
            if(orderPage != null && orderPage.getContent().size() > 0) {
                int rowNum = 1;
                for (Order order : orderPage.getContent()) {

                    row = sheet.createRow(rowNum);
                    row.createCell(0).setCellValue(order.getId());
                    row.createCell(1).setCellValue(order.getOrderNum());
                    String type = order.getType();
                    type = type.replaceAll("1", "隔热膜");
                    type = type.replaceAll("2", "隐形车衣");
                    type = type.replaceAll("3", "车身改色");
                    type = type.replaceAll("4", "美容清洁");
                    row.createCell(2).setCellValue(type);

                    row.createCell(3).setCellValue(order.getLicense() == null ? "" : order.getLicense());
                    row.createCell(4).setCellValue(order.getVehicleModel() == null ? "" : order.getVehicleModel());
                    row.createCell(5).setCellValue(order.getVin() == null ? "" : order.getVin());

                    row.createCell(6).setCellValue(order.getAgreedStartTime() == null ? "" : order.getAgreedStartTime() + "");
                    row.createCell(7).setCellValue(order.getCreatorName() + "");
                    row.createCell(8).setCellValue(order.getAddTime() + "");


                    String status1 = "";
                    if (order.getStatus().getStatusCode() == -10) {
                        status1 = "待指派";
                    }
                    if (order.getStatus().getStatusCode() == 0) {
                        status1 = "待指派";
                    } else if (order.getStatus().getStatusCode() == 10) {
                        status1 = "已接单";
                    } else if (order.getStatus().getStatusCode() == 50) {
                        status1 = "工作中";
                    } else if (order.getStatus().getStatusCode() == 60) {
                        status1 = "已完成";
                    } else if (order.getStatus().getStatusCode() == 70) {
                        status1 = "已评价";
                    } else if (order.getStatus().getStatusCode() == 200) {
                        status1 = "已撤销";
                    }

                    row.createCell(9).setCellValue(status1);
                    row.createCell(10).setCellValue(order.getProductStatus() == 0 ? "未补录" : "已补录");
                    if (order.getReassignmentStatus() == 0) {
                        row.createCell(11).setCellValue("未申请改派");
                    } else if (order.getReassignmentStatus() == 1) {
                        row.createCell(11).setCellValue("已申请改派");
                    } else {
                        row.createCell(11).setCellValue("已处理");
                    }


                    rowNum++;
                }
            }

            response.reset();
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=order"
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
     * 导出EXCEL(新)
     *
     * @param tech      技师电话或者名字
     * @param coopId    商户ID
     * @param startDate 订单开始时间
     * @param endDate  订单结束时间
     * @param status    订单状态
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/excel/download/view", method = RequestMethod.GET)
    public void downloadView(@RequestParam(value = "orderNum", required = false) String orderNum,
                         @RequestParam(value = "orderCreator", required = false) String orderCreator,
                         @RequestParam(value = "orderType", required = false) Integer orderType,
                         @RequestParam(value = "orderStatus", required = false) Order.Status orderStatus,
                         @RequestParam(value = "sort", defaultValue = "id") String sort,
                         @RequestParam(value = "tech", required = false) String tech,
                         @RequestParam(value = "coopId", required = false) String coopId,
                         @RequestParam(value = "startDate", required = false) String startDate,
                         @RequestParam(value = "endDate", required = false) String endDate,
                         @RequestParam(value = "status", required = false) Order.Status status,
                         @RequestParam(value = "idList", required = false) String idList,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2018-08-31 23:59:59");
        Date end = new Date();
        if(startDate != null && startDate != "") {
            start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate);
        }
        if(endDate != null && endDate != "") {
            end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDate);
        }
        List<WorkDetailOrderView> viewList;
        if(idList != null && !idList.equals("")){
            Page<WorkDetailOrderView> viewPage =  workDetailOrderViewService.findByIds(idList,1,10000);
            viewList = viewPage.getContent();
        }else if(tech == null || tech == ""){
            Page<WorkDetailOrderView> viewPage =  workDetailOrderViewService.findViews(start, end,1,10000);
            viewList = viewPage.getContent();
        }else{
            viewList =  workDetailOrderViewService.findViewsByTech(tech, start, end,1,10000);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面

        response.reset();

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=order" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls");


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("待结算清单表");


        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setAlignment(HorizontalAlignment.CENTER);

        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1, 18 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 12 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 18 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 18 * 256);
        sheet.setColumnWidth(9, 18 * 256);
        sheet.setColumnWidth(10, 18 * 256);
        sheet.setColumnWidth(11, 18 * 256);
        sheet.setColumnWidth(12, 18 * 256);

        HSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("订单ID");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("订单编号");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("订单类型");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("车牌");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("车型");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("车架号");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("预约开始时间");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("下单人");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("下单时间");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("接单时间");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("签到时间");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("开始施工时间");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("完成施工时间");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("订单状态");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("施工项目");
        cell.setCellStyle(style);



        //新增数据行，并且设置单元格数据
        HSSFCellStyle styleD = workbook.createCellStyle();
        styleD.setBorderBottom(BorderStyle.THIN);//下边框
        styleD.setBorderLeft(BorderStyle.THIN);//左边框
        styleD.setBorderTop(BorderStyle.THIN);//上边框
        styleD.setBorderRight(BorderStyle.THIN);//右边框
        if(viewList != null && viewList.size() > 0) {
            int rowNum = 1;
            for (WorkDetailOrderView order : viewList) {

                row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getOrderNum());
                String type = order.getType();
                type = type.replaceAll("1", "隔热膜");
                type = type.replaceAll("2", "隐形车衣");
                type = type.replaceAll("3", "车身改色");
                type = type.replaceAll("4", "美容清洁");
                row.createCell(2).setCellValue(type);

                row.createCell(3).setCellValue(order.getLicense() == null ? "" : order.getLicense());
                row.createCell(4).setCellValue(order.getVehicleModel() == null ? "" : order.getVehicleModel());
                row.createCell(5).setCellValue(order.getVin() == null ? "" : order.getVin());

                cell = row.createCell(6);
                cell.setCellStyle(style);
                if(order.getAgreedStartTime() != null) cell.setCellValue(order.getAgreedStartTime());
                row.createCell(7).setCellValue(order.getCreatorName());
                cell = row.createCell(8);
                cell.setCellStyle(style);
                if(order.getCreateTime() != null) cell.setCellValue(order.getCreateTime());
                cell = row.createCell(9);
                cell.setCellStyle(style);
                if(order.getTakenTime() != null) cell.setCellValue(order.getTakenTime());
                cell = row.createCell(10);
                cell.setCellStyle(style);
                if(order.getSignTime() != null) cell.setCellValue(order.getSignTime());
                cell = row.createCell(11);
                cell.setCellStyle(style);
                if(order.getStartTime() != null) cell.setCellValue(order.getStartTime());
                cell = row.createCell(12);
                cell.setCellStyle(style);
                if(order.getEndTime() != null) cell.setCellValue(order.getEndTime());


                String status1 = "";
                if (order.getStatus().getStatusCode() == -10) {
                    status1 = "待指派";
                }
                if (order.getStatus().getStatusCode() == 0) {
                    status1 = "待指派";
                } else if (order.getStatus().getStatusCode() == 10) {
                    status1 = "已接单";
                } else if (order.getStatus().getStatusCode() == 50) {
                    status1 = "工作中";
                } else if (order.getStatus().getStatusCode() == 60) {
                    status1 = "已完成";
                } else if (order.getStatus().getStatusCode() == 70) {
                    status1 = "已评价";
                } else if (order.getStatus().getStatusCode() == 200) {
                    status1 = "已撤销";
                }
                row.createCell(13).setCellValue(status1);

                String type1 = order.getType();
                type1 = type1.replaceAll("1", "隔热膜");
                type1 = type1.replaceAll("2", "隐形车衣");
                type1 = type1.replaceAll("3", "车身改色");
                type1 = type1.replaceAll("4", "美容清洁");
                row.createCell(14).setCellValue(type1);
//                for(int i = 14;i < 95;i++){
//                    row.createCell(i).setCellValue("");
//                }
                List<WorkDetailView> detailList = order.getWorkDetails();
                List<OrderProductView> productList = order.getOrderProducts();
                for(OrderProductView product : productList){}
                for(WorkDetailView detail : detailList){
                    if(detail.getPosition1() != null){
                        String[] ids = detail.getPosition1().split(",");
                        int m = 14;
                        for(String s : ids){
                            HSSFRow row0 = sheet.getRow(0);
                            if(row0.getCell(m + 1) == null || row0.getCell(m + 1).getRichStringCellValue() == null){
                                cell = row0.createCell(m + 1);
                                cell.setCellValue("施工人员");
                                cell.setCellStyle(style);
                                cell = row0.createCell(m + 2);
                                cell.setCellValue("施工部位" + ((m + 1 - 14)/5 + 1));
                                cell.setCellStyle(style);
                                cell = row0.createCell(m + 3);
                                cell.setCellValue("施工型号");
                                cell.setCellStyle(style);
                                cell = row0.createCell(m + 4);
                                cell.setCellValue("提成");
                                cell.setCellStyle(style);
                                cell = row0.createCell(m + 5);
                                cell.setCellValue("报废扣款");
                                cell.setCellStyle(style);
                            }
                            OrderProductView p = orderProductService.findByOrderIdAndProject(detail.getOrderId(), detail.getProject1(), Integer.parseInt(s));

                            cell = row.createCell(m + 1);
                            cell.setCellStyle(style);
                            cell.setCellValue(detail.getTechName());
                            sheet.setColumnWidth(m + 2, 18 * 256);
                            cell = row.createCell(m + 2);
                            cell.setCellStyle(style);
                            cell.setCellValue(p == null?"" : p.getName());
                            sheet.setColumnWidth(m + 3, 18 * 256);
                            cell = row.createCell(m + 3);
                            cell.setCellStyle(styleD);
                            cell.setCellValue(p == null?"" : p.getModel());
                            cell = row.createCell(m + 4);
                            cell.setCellStyle(styleD);
                            if(p != null && p.getConstructionCommission() != null) cell.setCellValue(p.getConstructionCommission());
                            cell = row.createCell(m + 5);
                            cell.setCellStyle(styleD);
                            if(p != null && p.getScrapCost() != null) cell.setCellValue(p.getScrapCost());

                            m += 5;
                        }
                    }
                }

                rowNum++;
            }
        }

        response.reset();
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=order"
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
     * 导出EXCEL(新存)
     *
     * @param tech      技师电话或者名字
     * @param coopId    商户ID
     * @param startDate 订单开始时间
     * @param endDate  订单结束时间
     * @param status    订单状态
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/excel/download/view111", method = RequestMethod.GET)
    public void downloadView111(@RequestParam(value = "orderNum", required = false) String orderNum,
                             @RequestParam(value = "orderCreator", required = false) String orderCreator,
                             @RequestParam(value = "orderType", required = false) Integer orderType,
                             @RequestParam(value = "orderStatus", required = false) Order.Status orderStatus,
                             @RequestParam(value = "sort", defaultValue = "id") String sort,
                             @RequestParam(value = "tech", required = false) String tech,
                             @RequestParam(value = "coopId", required = false) String coopId,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate,
                             @RequestParam(value = "status", required = false) Order.Status status,
                             @RequestParam(value = "idList", required = false) String idList,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
//        Date start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1970-01-01 00:00:00");
//        Date end = new Date();
//        if(startDate != null && startDate != "") {
//            start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate);
//        }
//        if(endDate != null && endDate != "") {
//            end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDate);
//        }
//        List<WorkDetailOrderView> viewList;
//        if(idList != null && !idList.equals("")){
//            Page<WorkDetailOrderView> viewPage =  workDetailOrderViewService.findByIds(idList,1,4000);
//            viewList = viewPage.getContent();
//        }else if(tech == null || tech == ""){
//            Page<WorkDetailOrderView> viewPage =  workDetailOrderViewService.findViews(start, end,1,4000);
//            viewList = viewPage.getContent();
//        }else{
//            viewList =  workDetailOrderViewService.findViewsByTech(tech, start, end,1,4000);
//        }
//
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        byte[] content = os.toByteArray();
//        InputStream is = new ByteArrayInputStream(content);
//        // 设置response参数，可以打开下载页面
//
//        response.reset();
//
//        response.setHeader("content-type", "application/octet-stream");
//        response.setContentType("application/octet-stream");
//        response.addHeader("Content-Disposition", "attachment;filename=order" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls");
//
//
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("待结算清单表");
//
//
//        HSSFCellStyle style = workbook.createCellStyle();
//        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
//        style.setBorderBottom(BorderStyle.THIN);//下边框
//        style.setBorderLeft(BorderStyle.THIN);//左边框
//        style.setBorderTop(BorderStyle.THIN);//上边框
//        style.setBorderRight(BorderStyle.THIN);//右边框
//        style.setAlignment(HorizontalAlignment.CENTER);
//
//        HSSFRow row = sheet.createRow(0);
//        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
//        sheet.setColumnWidth(1, 18 * 256);
//        sheet.setColumnWidth(2, 20 * 256);
//        sheet.setColumnWidth(3, 12 * 256);
//        sheet.setColumnWidth(5, 15 * 256);
//        sheet.setColumnWidth(6, 18 * 256);
//        sheet.setColumnWidth(7, 15 * 256);
//        sheet.setColumnWidth(8, 18 * 256);
//        sheet.setColumnWidth(9, 18 * 256);
//        sheet.setColumnWidth(10, 18 * 256);
//        sheet.setColumnWidth(11, 18 * 256);
//        sheet.setColumnWidth(12, 18 * 256);
//
//        HSSFCell cell;
//
//        cell = row.createCell(0);
//        cell.setCellValue("订单ID");
//        cell.setCellStyle(style);
//        cell = row.createCell(1);
//        cell.setCellValue("订单编号");
//        cell.setCellStyle(style);
//        cell = row.createCell(2);
//        cell.setCellValue("订单类型");
//        cell.setCellStyle(style);
//        cell = row.createCell(3);
//        cell.setCellValue("车牌");
//        cell.setCellStyle(style);
//        cell = row.createCell(4);
//        cell.setCellValue("车型");
//        cell.setCellStyle(style);
//        cell = row.createCell(5);
//        cell.setCellValue("车架号");
//        cell.setCellStyle(style);
//        cell = row.createCell(6);
//        cell.setCellValue("预约开始时间");
//        cell.setCellStyle(style);
//        cell = row.createCell(7);
//        cell.setCellValue("下单人");
//        cell.setCellStyle(style);
//        cell = row.createCell(8);
//        cell.setCellValue("下单时间");
//        cell.setCellStyle(style);
//        cell = row.createCell(9);
//        cell.setCellValue("接单时间");
//        cell.setCellStyle(style);
//        cell = row.createCell(10);
//        cell.setCellValue("签到时间");
//        cell.setCellStyle(style);
//        cell = row.createCell(11);
//        cell.setCellValue("开始施工时间");
//        cell.setCellStyle(style);
//        cell = row.createCell(12);
//        cell.setCellValue("完成施工时间");
//        cell.setCellStyle(style);
//        cell = row.createCell(13);
//        cell.setCellValue("订单状态");
//        cell.setCellStyle(style);
//
//        //设置合并单元格
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row (0-based)
//                14, //first column (0-based)
//                65
//        ));
//        cell = row.createCell(14);
//        cell.setCellValue("隔          热             膜");
//        cell.setCellStyle(style);
//        //合并单元格会取第一个格子的值，但后面的格子也得创建出来
//        for(int i=15;i<66;i++){
//            row.createCell(i).setCellStyle(style);
//        }
//
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row (0-based)
//                66, //first column (0-based)
//                205
//        ));
//        cell = row.createCell(66);
//        cell.setCellValue("隐        形       车       衣");
//        cell.setCellStyle(style);
//        for(int i=67;i<206;i++){
//            row.createCell(i).setCellStyle(style);
//        }
//
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row (0-based)
//                206, //first column (0-based)
//                321
//        ));
//        cell = row.createCell(206);
//        cell.setCellValue("车身改色");
//        cell.setCellStyle(style);
//        for(int i=207;i<322;i++){
//            row.createCell(i).setCellStyle(style);
//        }
//
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row (0-based)
//                322, //first column (0-based)
//                413
//        ));
//        cell = row.createCell(322);
//        cell.setCellValue("美容清洁");
//        cell.setCellStyle(style);
//        for(int i=323;i<414;i++){
//            row.createCell(i).setCellStyle(style);
//        }
//
//        //建第二行和第三行
//        HSSFRow row2 = sheet.createRow(1);
//        HSSFRow row3 = sheet.createRow(2);
//        for(int i = 0;i < 14;i++){        // 前13列竖着合并单元格3格，并定义前13列的二三行样式
//            sheet.addMergedRegion(new CellRangeAddress(
//                    0, //first row (0-based)
//                    2, //last row (0-based)
//                    i, //first column (0-based)
//                    i
//            ));
//            row2.createCell(i).setCellStyle(style);
//            row3.createCell(i).setCellStyle(style);
//        }
//        HSSFCell cell2;
//        for(int i = 14;i < 414;i=i+4){
//            sheet.addMergedRegion(new CellRangeAddress(           //从第14列开始每隔4列合并一次单元格
//                    1, //first row (0-based)
//                    1, //last row (0-based)
//                    i, //first column (0-based)
//                    i+3
//            ));
//        }
//        List<ConstructionProject> cp = constructionProjectService.findAll();
//        int j = 14;
//        for(ConstructionProject c : cp){
//            String[] ss = c.getIds().split(",");
//            for(int i = 0; i < ss.length; i ++){
//                cell2 = row2.createCell(j);
//                cell2.setCellValue(changePosition(ss[i]));
//                cell2.setCellStyle(style);
//                row2.createCell(j + 1).setCellStyle(style);
//                row2.createCell(j + 2).setCellStyle(style);
//                row2.createCell(j + 3).setCellStyle(style);
//                j += 4;
//            }
//        }
//        cell2 = row2.createCell(14);
//        cell2.setCellValue("前挡");
//        cell2.setCellStyle(style);
//        row2.createCell(15).setCellStyle(style);
//        row2.createCell(16).setCellStyle(style);
//        row2.createCell(17).setCellStyle(style);
//
//        cell2 = row2.createCell(18);
//        cell2.setCellValue("后档");
//        cell2.setCellStyle(style);
//        row2.createCell(19).setCellStyle(style);
//        row2.createCell(20).setCellStyle(style);
//        row2.createCell(21).setCellStyle(style);
//
//        cell2 = row2.createCell(22);
//        cell2.setCellValue("左前门");
//        cell2.setCellStyle(style);
//        row2.createCell(23).setCellStyle(style);
//        row2.createCell(24).setCellStyle(style);
//        row2.createCell(25).setCellStyle(style);
//
//        cell2 = row2.createCell(26);
//        cell2.setCellValue("右前门");
//        cell2.setCellStyle(style);
//        row2.createCell(27).setCellStyle(style);
//        row2.createCell(28).setCellStyle(style);
//        row2.createCell(29).setCellStyle(style);
//
//        cell2 = row2.createCell(30);
//        cell2.setCellValue("左后门");
//        cell2.setCellStyle(style);
//        row2.createCell(31).setCellStyle(style);
//        row2.createCell(32).setCellStyle(style);
//        row2.createCell(33).setCellStyle(style);
//
//        cell2 = row2.createCell(34);
//        cell2.setCellValue("右后门");
//        cell2.setCellStyle(style);
//        row2.createCell(35).setCellStyle(style);
//        row2.createCell(36).setCellStyle(style);
//        row2.createCell(37).setCellStyle(style);
//
//        cell2 = row2.createCell(38);
//        cell2.setCellValue("左小角");
//        cell2.setCellStyle(style);
//        row2.createCell(39).setCellStyle(style);
//        row2.createCell(40).setCellStyle(style);
//        row2.createCell(41).setCellStyle(style);
//
//        cell2 = row2.createCell(42);
//        cell2.setCellValue("右小角");
//        cell2.setCellStyle(style);
//        row2.createCell(43).setCellStyle(style);
//        row2.createCell(44).setCellStyle(style);
//        row2.createCell(45).setCellStyle(style);
//
//        cell2 = row2.createCell(46);
//        cell2.setCellValue("左大角");
//        cell2.setCellStyle(style);
//        row2.createCell(47).setCellStyle(style);
//        row2.createCell(48).setCellStyle(style);
//        row2.createCell(49).setCellStyle(style);
//
//        cell2 = row2.createCell(50);
//        cell2.setCellValue("右大角");
//        cell2.setCellStyle(style);
//        row2.createCell(51).setCellStyle(style);
//        row2.createCell(52).setCellStyle(style);
//        row2.createCell(53).setCellStyle(style);
//
//        cell2 = row2.createCell(54);
//        cell2.setCellValue("前天窗");
//        cell2.setCellStyle(style);
//        row2.createCell(55).setCellStyle(style);
//        row2.createCell(56).setCellStyle(style);
//        row2.createCell(57).setCellStyle(style);
//
//        cell2 = row2.createCell(58);
//        cell2.setCellValue("中天窗");
//        cell2.setCellStyle(style);
//        row2.createCell(59).setCellStyle(style);
//        row2.createCell(60).setCellStyle(style);
//        row2.createCell(61).setCellStyle(style);
//
//        cell2 = row2.createCell(62);
//        cell2.setCellValue("后天窗");
//        cell2.setCellStyle(style);
//        row2.createCell(63).setCellStyle(style);
//        row2.createCell(64).setCellStyle(style);
//        row2.createCell(65).setCellStyle(style);
//
//        cell2 = row2.createCell(66);
//        cell2.setCellValue("整车");
//        cell2.setCellStyle(style);
//        row2.createCell(67).setCellStyle(style);
//        row2.createCell(68).setCellStyle(style);
//        row2.createCell(69).setCellStyle(style);
//
//        cell2 = row2.createCell(70);
//        cell2.setCellValue("整车不含顶");
//        cell2.setCellStyle(style);
//        row2.createCell(71).setCellStyle(style);
//        row2.createCell(72).setCellStyle(style);
//        row2.createCell(73).setCellStyle(style);
//
//        cell2 = row2.createCell(74);
//        cell2.setCellValue("车顶");
//        cell2.setCellStyle(style);
//        row2.createCell(75).setCellStyle(style);
//        row2.createCell(76).setCellStyle(style);
//        row2.createCell(77).setCellStyle(style);
//
//        HSSFCell cell3;
//        for(int i = 14;i < 414;i=i+4){
//            cell3 = row3.createCell(i);
//            cell3.setCellValue("施工人员");
//            cell3.setCellStyle(style);
//            cell3 = row3.createCell(i+1);
//            cell3.setCellValue("施工型号");
//            cell3.setCellStyle(style);
//            cell3 = row3.createCell(i+2);
//            cell3.setCellValue("提成");
//            cell3.setCellStyle(style);
//            cell3 = row3.createCell(i+3);
//            cell3.setCellValue("报废扣款");
//            cell3.setCellStyle(style);
//        }
//
//
//        //新增数据行，并且设置单元格数据
//        HSSFCellStyle styleD = workbook.createCellStyle();
//        styleD.setBorderBottom(BorderStyle.THIN);//下边框
//        styleD.setBorderLeft(BorderStyle.THIN);//左边框
//        styleD.setBorderTop(BorderStyle.THIN);//上边框
//        styleD.setBorderRight(BorderStyle.THIN);//右边框
//        if(viewList != null && viewList.size() > 0) {
//            int rowNum = 3;
//            for (WorkDetailOrderView order : viewList) {
//
//                row = sheet.createRow(rowNum);
//                row.createCell(0).setCellValue(order.getId());
//                row.createCell(1).setCellValue(order.getOrderNum());
//                String type = order.getType();
//                type = type.replaceAll("1", "隔热膜");
//                type = type.replaceAll("2", "隐形车衣");
//                type = type.replaceAll("3", "车身改色");
//                type = type.replaceAll("4", "美容清洁");
//                row.createCell(2).setCellValue(type);
//
//                row.createCell(3).setCellValue(order.getLicense() == null ? "" : order.getLicense());
//                row.createCell(4).setCellValue(order.getVehicleModel() == null ? "" : order.getVehicleModel());
//                row.createCell(5).setCellValue(order.getVin() == null ? "" : order.getVin());
//
//                cell = row.createCell(6);
//                cell.setCellStyle(style);
//                if(order.getAgreedStartTime() != null) cell.setCellValue(order.getAgreedStartTime());
//                row.createCell(7).setCellValue(order.getCreatorName());
//                cell = row.createCell(8);
//                cell.setCellStyle(style);
//                if(order.getCreateTime() != null) cell.setCellValue(order.getCreateTime());
//                cell = row.createCell(9);
//                cell.setCellStyle(style);
//                if(order.getTakenTime() != null) cell.setCellValue(order.getTakenTime());
//                cell = row.createCell(10);
//                cell.setCellStyle(style);
//                if(order.getSignTime() != null) cell.setCellValue(order.getSignTime());
//                cell = row.createCell(11);
//                cell.setCellStyle(style);
//                if(order.getStartTime() != null) cell.setCellValue(order.getStartTime());
//                cell = row.createCell(12);
//                cell.setCellStyle(style);
//                if(order.getEndTime() != null) cell.setCellValue(order.getEndTime());
//
//
//                String status1 = "";
//                if (order.getStatus().getStatusCode() == -10) {
//                    status1 = "待指派";
//                }
//                if (order.getStatus().getStatusCode() == 0) {
//                    status1 = "待指派";
//                } else if (order.getStatus().getStatusCode() == 10) {
//                    status1 = "已接单";
//                } else if (order.getStatus().getStatusCode() == 50) {
//                    status1 = "工作中";
//                } else if (order.getStatus().getStatusCode() == 60) {
//                    status1 = "已完成";
//                } else if (order.getStatus().getStatusCode() == 70) {
//                    status1 = "已评价";
//                } else if (order.getStatus().getStatusCode() == 200) {
//                    status1 = "已撤销";
//                }
//                row.createCell(13).setCellValue(status1);
//                for(int i = 14;i < 95;i++){
//                    row.createCell(i).setCellValue("");
//                }
//                List<WorkDetailView> detailList = order.getWorkDetails();
//                for(WorkDetailView detail : detailList){
//                    if(checkDetail(detail, 1, "1")){
//                        row.getCell(14).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "2")){
//                        row.getCell(17).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "3")){
//                        row.getCell(20).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "4")){
//                        row.getCell(23).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "5")){
//                        row.getCell(26).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "6")){
//                        row.getCell(29).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "19")){
//                        row.getCell(32).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "20")){
//                        row.getCell(35).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "21")){
//                        row.getCell(38).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "22")){
//                        row.getCell(41).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "23")){
//                        row.getCell(44).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "24")){
//                        row.getCell(47).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 1, "25")){
//                        row.getCell(50).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "7")){
//                        row.getCell(53).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "8")){
//                        row.getCell(56).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "9")){
//                        row.getCell(59).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "10")){
//                        row.getCell(62).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "11")){
//                        row.getCell(65).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "12")){
//                        row.getCell(68).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "13")){
//                        row.getCell(71).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "14")){
//                        row.getCell(74).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "15")){
//                        row.getCell(77).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "16")){
//                        row.getCell(80).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "17")){
//                        row.getCell(83).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 2, "18")){
//                        row.getCell(86).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 3, "18")){
//                        row.getCell(89).setCellValue(detail.getTechName());
//                    }
//                    if(checkDetail(detail, 4, "18")){
//                        row.getCell(92).setCellValue(detail.getTechName());
//                    }
//                }
//
//                List<OrderProductView> productList = order.getOrderProducts();
//                for(OrderProductView product : productList){
//                    if(product.getConstructionProjectId() == 1){
//                        if(product.getConstructionPositionId() == 1){
//                            product.getScrapCost();
//                            row.getCell(15).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(16).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 2){
//                            row.getCell(18).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(19).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 3){
//                            row.getCell(21).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(22).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 4){
//                            row.getCell(24).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(25).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 5){
//                            row.getCell(27).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(28).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 6){
//                            row.getCell(30).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(31).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 19){
//                            row.getCell(33).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(34).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 20){
//                            row.getCell(36).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(37).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 21){
//                            row.getCell(39).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(40).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 22){
//                            row.getCell(42).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(43).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 23){
//                            row.getCell(45).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(46).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 24){
//                            row.getCell(48).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(49).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 25){
//                            row.getCell(51).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(52).setCellValue(product.getConstructionCommission());
//                        }
//                    }else if(product.getConstructionProjectId() == 2){
//                        if(product.getConstructionPositionId() == 7){
//                            row.getCell(54).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(55).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 8){
//                            row.getCell(57).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(58).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 9){
//                            row.getCell(60).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(61).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 10){
//                            row.getCell(63).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(64).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 11){
//                            row.getCell(66).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(67).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 12){
//                            row.getCell(69).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(70).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 13){
//                            row.getCell(72).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(73).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 14){
//                            row.getCell(75).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(76).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 15){
//                            row.getCell(78).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(79).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 16){
//                            row.getCell(81).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(82).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 17){
//                            row.getCell(84).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(85).setCellValue(product.getConstructionCommission());
//                        }else if(product.getConstructionPositionId() == 18){
//                            row.getCell(87).setCellValue(product.getModel());
//                            if(product.getConstructionCommission() != null) row.getCell(88).setCellValue(product.getConstructionCommission());
//                        }
//                    }else if(product.getConstructionProjectId() == 3){
//                        row.getCell(90).setCellValue(product.getModel());
//                        if(product.getConstructionCommission() != null) row.getCell(91).setCellValue(product.getConstructionCommission());
//                    }else if(product.getConstructionProjectId() == 4){
//                        row.getCell(93).setCellValue(product.getModel());
//                        if(product.getConstructionCommission() != null) row.getCell(94).setCellValue(product.getConstructionCommission());
//                    }
//                }
//
//                rowNum++;
//                for (Cell c : row) {
//                    if(c.getColumnIndex() != 6 && c.getColumnIndex() != 8 && c.getColumnIndex() != 9 && c.getColumnIndex() != 10 && c.getColumnIndex() != 11 && c.getColumnIndex() != 12){
//                        c.setCellStyle(styleD);
//                    }
//                }
//            }
//        }
//
//        response.reset();
//        response.setHeader("content-type", "application/octet-stream");
//        response.setContentType("application/octet-stream");
//        response.addHeader("Content-Disposition", "attachment;filename=order"
//                + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls");
//        ServletOutputStream out = null;
//        try {
//            out = response.getOutputStream();
//            workbook.write(out);
//            out.close();
//        } catch (Exception e) {
//            throw new RuntimeException("导出失败");
//        }
    }

    @RequestMapping(value = "/excel/download/work/{techId}", method = RequestMethod.GET)
    public void download(@PathVariable("techId") int techId,
                         @RequestParam(value = "startTime", required = false) Long startTime,
                         @RequestParam(value = "endTime", required = false) Long endTime,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        Page<WorkDetailView> views = workDetailService.findViews(techId, 1, 4000);
// INSERT INTO t_tech_finance(tech_id,sum_income) SELECT t.id,SUM(w.payment) from t_technician t LEFT JOIN t_work_detail w on w.tech_id = t.id GROUP BY t.id
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面

        response.reset();

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=workDetail" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls");


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("待结算清单表");

        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setAlignment(HorizontalAlignment.CENTER);

        HSSFCellStyle style1 = workbook.createCellStyle();
        style1.setBorderBottom(BorderStyle.THIN);//下边框
        style1.setBorderLeft(BorderStyle.THIN);//左边框
        style1.setBorderTop(BorderStyle.THIN);//上边框
        style1.setBorderRight(BorderStyle.THIN);//右边框
        style1.setAlignment(HorizontalAlignment.CENTER);

        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0, 18 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 17 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(8, 22 * 256);
        sheet.setColumnWidth(9, 16 * 256);
        sheet.setColumnWidth(10, 16 * 256);
//        sheet.setColumnWidth(10, 150 * 256);
//        sheet.setColumnWidth(12, 150 * 256);
//        sheet.setColumnWidth(14, 150 * 256);
//        sheet.setColumnWidth(16, 150 * 256);

        HSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("日期");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("技师姓名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("订单编号");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("商户名称");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("施工项目");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("结算金额");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("报废扣款");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("车型");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("车架号");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("车牌号");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("单号");
        cell.setCellStyle(style);
//        String str = workDetailService.findLargest(techId);
//        if(str != null){
//            String[] positions = str.split(",");
//            int j = 10;
//            int k = 1;
//            for(String s : positions){
//                cell = row.createCell(j + 1);
//                cell.setCellValue("施工部位" + k);
//                cell.setCellStyle(style);
//                cell = row.createCell(j + 2);
//                cell.setCellValue("施工型号");
//                cell.setCellStyle(style);
//                cell = row.createCell(j + 3);
//                cell.setCellValue("提成");
//                cell.setCellStyle(style);
//                j += 3;
//                k ++;
//            }
//        }else{
//            cell = row.createCell(11);
//            cell.setCellValue("施工部位");
//            cell.setCellStyle(style);
//            cell = row.createCell(12);
//            cell.setCellValue("施工型号");
//            cell.setCellStyle(style);
//            cell = row.createCell(13);
//            cell.setCellValue("提成");
//            cell.setCellStyle(style);
//        }


        //新增数据行，并且设置单元格数据
        if(views != null && views.getContent().size() > 0) {
            int rowNum = 1;
            for (WorkDetailView view : views.getContent()) {

                row = sheet.createRow(rowNum);
                cell = row.createCell(0);
                cell.setCellStyle(style);
                cell.setCellValue(view.getAddTime());
                cell = row.createCell(1);
                cell.setCellStyle(style);
                cell.setCellValue(view.getTechName());
                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell.setCellValue(view.getOrderNum());
                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(view.getFullname());
                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(view.getProjectName1());
                cell = row.createCell(5);
                cell.setCellStyle(style1);
                cell.setCellValue(view.getPayment());
                cell = row.createCell(6);
                cell.setCellStyle(style1);
                cell.setCellValue(view.getTotalCost());
                cell = row.createCell(7);
                cell.setCellStyle(style);
                cell.setCellValue(view.getVehicleModel());
                cell = row.createCell(8);
                cell.setCellStyle(style);
                cell.setCellValue(view.getVin());
                cell = row.createCell(9);
                cell.setCellStyle(style);
                cell.setCellValue(view.getLicense());
                cell = row.createCell(10);
                cell.setCellStyle(style);
                cell.setCellValue(view.getRealOrderNum());
                if(view.getPosition1() != null) {
                    String[] pos = view.getPosition1().split(",");
                    int m = 10;
                    for(String s : pos){
                        HSSFRow row0 = sheet.getRow(0);
                        if(row0.getCell(m + 1) == null || row0.getCell(m + 1).getRichStringCellValue() == null){
                            cell = row0.createCell(m + 1);
                            cell.setCellValue("施工部位" + ((m + 1 - 10)/3 + 1));
                            cell.setCellStyle(style);
                            cell = row0.createCell(m + 2);
                            cell.setCellValue("施工型号");
                            cell.setCellStyle(style);
                            cell = row0.createCell(m + 3);
                            cell.setCellValue("提成");
                            cell.setCellStyle(style);
                        }
                        OrderProductView p = orderProductService.findByOrderIdAndProject(view.getOrderId(), view.getProject1(), Integer.parseInt(s));

                        sheet.setColumnWidth(m + 1, 18 * 256);
                        cell = row.createCell(m + 1);
                        cell.setCellStyle(style);
                        cell.setCellValue(p == null?"" : p.getName());
                        sheet.setColumnWidth(m + 2, 16 * 256);
                        cell = row.createCell(m + 2);
                        cell.setCellStyle(style);
                        cell.setCellValue(p == null?"" : p.getModel());
                        cell = row.createCell(m + 3);
                        cell.setCellStyle(style1);
                        if(p != null && p.getConstructionCommission() != null) cell.setCellValue(p.getConstructionCommission());
                        m += 3;
                    }
                }
//                cell = row.createCell(11);
//                cell.setCellStyle(style);
//                cell.setCellValue(changePosition(view.getPosition4()));

                rowNum++;
            }
        }

        response.reset();
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=order"
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

    // 判断是否有这个部位
    private boolean checkDetail(WorkDetailView detail, int tag1, String tag2){
        if(detail.getProject1() != null && detail.getProject1() == tag1 && detail.getPosition1().contains(tag2)
                || detail.getProject2() != null && detail.getProject2() == tag1 && detail.getPosition2().contains(tag2)
                || detail.getProject3() != null && detail.getProject3() == tag1 && detail.getPosition3().contains(tag2)
                || detail.getProject4() != null && detail.getProject4() == tag1 && detail.getPosition4().contains(tag2)
                || detail.getProject5() != null && detail.getProject5() == tag1 && detail.getPosition5().contains(tag2)
                || detail.getProject6() != null && detail.getProject6() == tag1 && detail.getPosition6().contains(tag2)){
            return true;
        }else{
            return false;
        }
    }


    private String changePosition(String position){
        if(position != null){
            position = position.replaceAll("1", "前挡");
            position = position.replaceAll("2", "左前门");
            position = position.replaceAll("3", "右前门");
            position = position.replaceAll("4", "左后门");
            position = position.replaceAll("5", "右后门");
            position = position.replaceAll("6", "后挡");
            position = position.replaceAll("7", "前保险杠");
            position = position.replaceAll("8", "引擎盖");
            position = position.replaceAll("9", "左右前叶子板");
            position = position.replaceAll("10", "四门");
            position = position.replaceAll("11", "左右后叶子板");
            position = position.replaceAll("12", "后箱盖");
            position = position.replaceAll("13", "后保险杠");
            position = position.replaceAll("14", "ABC柱套件");
            position = position.replaceAll("15", "车顶");
            position = position.replaceAll("16", "门拉手");
            position = position.replaceAll("17", "反光镜");
            position = position.replaceAll("18", "整车");
            position = position.replaceAll("19", "大天窗");
            position = position.replaceAll("20", "中天窗");
            position = position.replaceAll("21", "小天窗");
            position = position.replaceAll("22", "左小角");
            position = position.replaceAll("23", "右小角");
            position = position.replaceAll("24", "左大角");
            position = position.replaceAll("25", "右大角");
            position = position.replaceAll("26", "左裙边");
            position = position.replaceAll("27", "右裙边");
            position = position.replaceAll("28", "整车不含顶");
            position = position.replaceAll("29", "前杠");
            position = position.replaceAll("30", "后杠");
            position = position.replaceAll("31", "左前叶子板");
            position = position.replaceAll("32", "右前叶子板");
            position = position.replaceAll("33", "左后叶子板");
            position = position.replaceAll("34", "右后叶子板");
            position = position.replaceAll("35", "左后视镜");
            position = position.replaceAll("36", "右后视镜");
            position = position.replaceAll("37", "手扣");
            position = position.replaceAll("38", "中控");
            position = position.replaceAll("39", "四门脚踏");
            position = position.replaceAll("40", "四门饰条");
            position = position.replaceAll("41", "两前座椅");
            position = position.replaceAll("42", "两后座椅");
            position = position.replaceAll("43", "喜悦套餐");
            position = position.replaceAll("44", "前杠角");
            position = position.replaceAll("45", "后杠角");
            position = position.replaceAll("46", "四轮眉");
            position = position.replaceAll("47", "新车漆面处理");
            position = position.replaceAll("48", "新车整备");
            position = position.replaceAll("49", "二手车全车整备翻新");
            position = position.replaceAll("50", "完美交车");
            position = position.replaceAll("51", "镀膜/镀晶（新车）");
            position = position.replaceAll("52", "镀膜/镀晶（旧车）");
            position = position.replaceAll("53", "镀膜/镀晶（维护）");
            position = position.replaceAll("54", "打蜡封釉");
            position = position.replaceAll("55", "抛光（旧车）");
            position = position.replaceAll("56", "轮毂镀膜/镀晶");
            position = position.replaceAll("57", "轮毂清洗翻新");
            position = position.replaceAll("58", "外观镀铬件翻新");
            position = position.replaceAll("59", "外观镀铬件养护");
            position = position.replaceAll("60", "门板镀膜");
            position = position.replaceAll("61", "玻璃镀膜");
            position = position.replaceAll("62", "真皮镀膜");
            position = position.replaceAll("63", "真皮清洁");
            position = position.replaceAll("64", "氧触媒套装");
            position = position.replaceAll("65", "内饰消毒去味");
            position = position.replaceAll("66", "内饰清洗");
            position = position.replaceAll("67", "内部聚乙烯养护");
            position = position.replaceAll("68", "底盘装甲");
            position = position.replaceAll("69", "发动机排气管/皮带维护");
            position = position.replaceAll("70", "发动机舱线路保护");
            position = position.replaceAll("71", "发动机表面清洁");
            position = position.replaceAll("72", "天窗护理");
            position = position.replaceAll("73", "滤芯更换");
            position = position.replaceAll("74", "行车记录仪安装");
            position = position.replaceAll("75", "大灯翻新");
            position = position.replaceAll("76", "全车撕改色膜/车衣");
            position = position.replaceAll("77", "后视镜犀牛皮");
            position = position.replaceAll("78", "四门手扣犀牛皮");
            position = position.replaceAll("79", "整车车身");
            position = position.replaceAll("80", "前天窗");
            position = position.replaceAll("81", "中天窗");
            position = position.replaceAll("82", "后天窗");
            position = position.replaceAll("83", "新车封釉/镀膜/镀晶");
            position = position.replaceAll("84", "新车揭膜除胶或打蜡");
            position = position.replaceAll("85", "新车精洗整备（除胶除锈）");
            position = position.replaceAll("86", "杀菌/去味套餐");
            position = position.replaceAll("87", "二手车全车整备翻新");
            position = position.replaceAll("88", "旧车封釉/镀膜/镀晶");
            position = position.replaceAll("89", "内饰清洗/翻新");
            position = position.replaceAll("90", "发动机舱清洁养护");
            position = position.replaceAll("91", "轮毂翻新/镀膜");
            position = position.replaceAll("92", "玻璃清洁/镀膜");
            position = position.replaceAll("93", "真皮清洁/镀膜");
            position = position.replaceAll("94", "镀膜/镀晶维护");
            position = position.replaceAll("95", "后视镜犀牛皮");
            position = position.replaceAll("96", "四门手扣犀牛皮");
            position = position.replaceAll("97", "底盘装甲");
            position = position.replaceAll("98", "滤芯更换");
            position = position.replaceAll("99", "零售店镀晶（含机舱轮毂轮胎内饰整备）");
            position = position.replaceAll("100", "零售店极光养护（含机舱轮毂轮胎内饰整备）");
            position = position.replaceAll("101", "零售店内饰翻新（含机舱轮毂轮胎漆面打蜡）");
            position = position.replaceAll("102", "零售店真皮清洗镀膜（含机舱轮毂轮胎漆面打蜡）");
            position = position.replaceAll("103", "零售店撕车衣/改色膜");
            position = position.replaceAll("104", "零售店行车记录仪安装");
            position = position.replaceAll("105", "零售店裁脚垫");
            position = position.replaceAll("106", "左机盖");
            position = position.replaceAll("107", "右机盖");
            position = position.replaceAll("108", "左前杠");
            position = position.replaceAll("109", "右前杠");
            position = position.replaceAll("110", "左后杠");
            position = position.replaceAll("111", "右后杠");

        }

        return position;
    }

    @RequestMapping(value = "/excel/download/work", method = RequestMethod.GET)
    public void downloadAll(@RequestParam(value = "startTime", required = false) Long startTime,
                         @RequestParam(value = "endTime", required = false) Long endTime,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        List<WorkDetailView> views = workDetailService.findAllViews();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面

        response.reset();

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=workDetail" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls");


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("技师财务清单");

        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setAlignment(HorizontalAlignment.CENTER);

        HSSFCellStyle style1 = workbook.createCellStyle();
        style1.setBorderBottom(BorderStyle.THIN);//下边框
        style1.setBorderLeft(BorderStyle.THIN);//左边框
        style1.setBorderTop(BorderStyle.THIN);//上边框
        style1.setBorderRight(BorderStyle.THIN);//右边框
        style1.setAlignment(HorizontalAlignment.CENTER);

        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0, 18 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 17 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(8, 22 * 256);
        sheet.setColumnWidth(9, 16 * 256);
        sheet.setColumnWidth(10, 16 * 256);

        HSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("日期");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("技师姓名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("订单编号");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("商户名称");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("施工项目");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("结算金额");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("报废扣款");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("车型");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("车架号");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("车牌号");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("单号");
        cell.setCellStyle(style);
//        String str = workDetailService.findLargest();
//        if(str != null){
//            String[] positions = str.split(",");
//            int j = 10;
//            int k = 1;
//            for(String s : positions){
//                cell = row.createCell(j + 1);
//                cell.setCellValue("施工部位" + k);
//                cell.setCellStyle(style);
//                cell = row.createCell(j + 2);
//                cell.setCellValue("施工型号");
//                cell.setCellStyle(style);
//                cell = row.createCell(j + 3);
//                cell.setCellValue("提成");
//                cell.setCellStyle(style);
//                j += 3;
//                k ++;
//            }
//        }else{
//            cell = row.createCell(11);
//            cell.setCellValue("施工部位");
//            cell.setCellStyle(style);
//            cell = row.createCell(12);
//            cell.setCellValue("施工型号");
//            cell.setCellStyle(style);
//            cell = row.createCell(13);
//            cell.setCellValue("提成");
//            cell.setCellStyle(style);
//        }


        //新增数据行，并且设置单元格数据
        if(views != null && views.size() > 0) {
            int rowNum = 1;
            for (WorkDetailView view : views) {

                row = sheet.createRow(rowNum);
                cell = row.createCell(0);
                cell.setCellStyle(style);
                cell.setCellValue(view.getAddTime());
                cell = row.createCell(1);
                cell.setCellStyle(style);
                cell.setCellValue(view.getTechName());
                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell.setCellValue(view.getOrderNum());
                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(view.getFullname());
                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(view.getProjectName1());
                cell = row.createCell(5);
                cell.setCellStyle(style1);
                cell.setCellValue(view.getPayment());
                cell = row.createCell(6);
                cell.setCellStyle(style1);
                cell.setCellValue(view.getTotalCost());
                cell = row.createCell(7);
                cell.setCellStyle(style);
                cell.setCellValue(view.getVehicleModel());
                cell = row.createCell(8);
                cell.setCellStyle(style);
                cell.setCellValue(view.getVin());
                cell = row.createCell(9);
                cell.setCellStyle(style);
                cell.setCellValue(view.getLicense());
                cell = row.createCell(10);
                cell.setCellStyle(style);
                cell.setCellValue(view.getRealOrderNum());
                if(view.getPosition1() != null) {
                    String[] pos = view.getPosition1().split(",");
                    int m = 10;
                    for(String s : pos){
                        HSSFRow row0 = sheet.getRow(0);
                        if(row0.getCell(m + 1) == null || row0.getCell(m + 1).getRichStringCellValue() == null){
                            cell = row0.createCell(m + 1);
                            cell.setCellValue("施工部位" + ((m + 1 - 10)/3 + 1));
                            cell.setCellStyle(style);
                            cell = row0.createCell(m + 2);
                            cell.setCellValue("施工型号");
                            cell.setCellStyle(style);
                            cell = row0.createCell(m + 3);
                            cell.setCellValue("提成");
                            cell.setCellStyle(style);
                        }
                        OrderProductView p = orderProductService.findByOrderIdAndProject(view.getOrderId(), view.getProject1(), Integer.parseInt(s));

                        sheet.setColumnWidth(m + 1, 18 * 256);
                        cell = row.createCell(m + 1);
                        cell.setCellStyle(style);
                        cell.setCellValue(p == null?"" : p.getName());
                        sheet.setColumnWidth(m + 2, 16 * 256);
                        cell = row.createCell(m + 2);
                        cell.setCellStyle(style);
                        cell.setCellValue(p == null?"" : p.getModel());
                        cell = row.createCell(m + 3);
                        cell.setCellStyle(style1);
                        if(p != null && p.getConstructionCommission() != null) cell.setCellValue(p.getConstructionCommission());
                        m += 3;
                    }
                }


                rowNum++;
            }
        }

        response.reset();
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=order"
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



    @RequestMapping(value = "/test/finish", method = RequestMethod.GET)
    public JsonResult testFinance(){
        workDetailService.testFinance();
        return new JsonResult(true, "修改完成");
    }
}
