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
                if (workDetailShow.getTechId() == orderShow.getTechId()) {
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
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {

        List<WorkDetail> list = new ArrayList<>();

        WorkDetail workDetail = new WorkDetail();
        workDetail.setOrderId(1);
        workDetail.setProject1(1);
        workDetail.setPosition1("1,2");
        workDetail.setProject2(2);
        workDetail.setPosition2("3,4");

        list.add(workDetail);
        workDetailService.balance(1);
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
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1970-01-01 00:00:00");
        Date end = new Date();
        if(startDate != null && startDate != "") {
            start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate);
        }if(startDate != null && startDate != "") {
            end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDate);
        }
        List<WorkDetailOrderView> viewList;
        if(tech == null || tech == ""){
            Page<WorkDetailOrderView> viewPage =  workDetailOrderViewService.findViews(start, end,1,300);
            viewList = viewPage.getContent();
        }else{
            viewList =  workDetailOrderViewService.findViewsByTech(tech, start, end,1,300);
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
        sheet.setColumnWidth(5, 15 * 256);
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

        //设置合并单元格
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row (0-based)
                14, //first column (0-based)
                52
        ));
        cell = row.createCell(14);
        cell.setCellValue("隔          热             膜");
        cell.setCellStyle(style);
        //合并单元格会取第一个格子的值，但后面的格子也得创建出来
        for(int i=15;i<53;i++){
            row.createCell(i).setCellStyle(style);
        }

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row (0-based)
                53, //first column (0-based)
                88
        ));
        cell = row.createCell(53);
        cell.setCellValue("隐        形       车       衣");
        cell.setCellStyle(style);
        for(int i=54;i<89;i++){
            row.createCell(i).setCellStyle(style);
        }

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row (0-based)
                89, //first column (0-based)
                91
        ));
        cell = row.createCell(89);
        cell.setCellValue("车身改色");
        cell.setCellStyle(style);
        row.createCell(90).setCellStyle(style);
        row.createCell(91).setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row (0-based)
                92, //first column (0-based)
                94
        ));
        cell = row.createCell(92);
        cell.setCellValue("美容清洁");
        cell.setCellStyle(style);
        row.createCell(93).setCellStyle(style);
        row.createCell(94).setCellStyle(style);

        //建第二行和第三行
        HSSFRow row2 = sheet.createRow(1);
        HSSFRow row3 = sheet.createRow(2);
        for(int i = 0;i < 14;i++){        // 前13列竖着合并单元格3格，并定义前13列的二三行样式
            sheet.addMergedRegion(new CellRangeAddress(
                    0, //first row (0-based)
                    2, //last row (0-based)
                    i, //first column (0-based)
                    i
            ));
            row2.createCell(i).setCellStyle(style);
            row3.createCell(i).setCellStyle(style);
        }
        HSSFCell cell2;
        for(int i = 14;i < 95;i=i+3){
            sheet.addMergedRegion(new CellRangeAddress(           //从第14列开始每隔3列合并一次单元格
                    1, //first row (0-based)
                    1, //last row (0-based)
                    i, //first column (0-based)
                    i+2
            ));
        }
        cell2 = row2.createCell(14);
        cell2.setCellValue("前风挡");
        cell2.setCellStyle(style);
        row2.createCell(15).setCellStyle(style);
        row2.createCell(16).setCellStyle(style);

        cell2 = row2.createCell(17);
        cell2.setCellValue("左前门");
        cell2.setCellStyle(style);
        row2.createCell(18).setCellStyle(style);
        row2.createCell(19).setCellStyle(style);

        cell2 = row2.createCell(20);
        cell2.setCellValue("右前门");
        cell2.setCellStyle(style);
        row2.createCell(21).setCellStyle(style);
        row2.createCell(22).setCellStyle(style);

        cell2 = row2.createCell(23);
        cell2.setCellValue("左后门");
        cell2.setCellStyle(style);
        row2.createCell(24).setCellStyle(style);
        row2.createCell(25).setCellStyle(style);

        cell2 = row2.createCell(26);
        cell2.setCellValue("右后门");
        cell2.setCellStyle(style);
        row2.createCell(27).setCellStyle(style);
        row2.createCell(28).setCellStyle(style);

        cell2 = row2.createCell(29);
        cell2.setCellValue("后风挡");
        cell2.setCellStyle(style);
        row2.createCell(30).setCellStyle(style);
        row2.createCell(31).setCellStyle(style);

        cell2 = row2.createCell(32);
        cell2.setCellValue("大天窗");
        cell2.setCellStyle(style);
        row2.createCell(33).setCellStyle(style);
        row2.createCell(34).setCellStyle(style);

        cell2 = row2.createCell(35);
        cell2.setCellValue("中天窗");
        cell2.setCellStyle(style);
        row2.createCell(36).setCellStyle(style);
        row2.createCell(37).setCellStyle(style);

        cell2 = row2.createCell(38);
        cell2.setCellValue("小天窗");
        cell2.setCellStyle(style);
        row2.createCell(39).setCellStyle(style);
        row2.createCell(40).setCellStyle(style);

        cell2 = row2.createCell(41);
        cell2.setCellValue("左小角");
        cell2.setCellStyle(style);
        row2.createCell(42).setCellStyle(style);
        row2.createCell(43).setCellStyle(style);

        cell2 = row2.createCell(44);
        cell2.setCellValue("右小角");
        cell2.setCellStyle(style);
        row2.createCell(45).setCellStyle(style);
        row2.createCell(46).setCellStyle(style);

        cell2 = row2.createCell(47);
        cell2.setCellValue("左大角");
        cell2.setCellStyle(style);
        row2.createCell(48).setCellStyle(style);
        row2.createCell(49).setCellStyle(style);

        cell2 = row2.createCell(50);
        cell2.setCellValue("右大角");
        cell2.setCellStyle(style);
        row2.createCell(51).setCellStyle(style);
        row2.createCell(52).setCellStyle(style);


        cell2 = row2.createCell(53);
        cell2.setCellValue("前保险杠");
        cell2.setCellStyle(style);
        row2.createCell(54).setCellStyle(style);
        row2.createCell(55).setCellStyle(style);

        cell2 = row2.createCell(56);
        cell2.setCellValue("引擎盖");
        cell2.setCellStyle(style);
        row2.createCell(57).setCellStyle(style);
        row2.createCell(58).setCellStyle(style);

        cell2 = row2.createCell(59);
        cell2.setCellValue("左右前叶子板");
        cell2.setCellStyle(style);
        row2.createCell(60).setCellStyle(style);
        row2.createCell(61).setCellStyle(style);

        cell2 = row2.createCell(62);
        cell2.setCellValue("四门");
        cell2.setCellStyle(style);
        row2.createCell(63).setCellStyle(style);
        row2.createCell(64).setCellStyle(style);

        cell2 = row2.createCell(65);
        cell2.setCellValue("左右后叶子板");
        cell2.setCellStyle(style);
        row2.createCell(66).setCellStyle(style);
        row2.createCell(67).setCellStyle(style);

        cell2 = row2.createCell(68);
        cell2.setCellValue("尾盖");
        cell2.setCellStyle(style);
        row2.createCell(69).setCellStyle(style);
        row2.createCell(70).setCellStyle(style);

        cell2 = row2.createCell(71);
        cell2.setCellValue("后保险杠");
        cell2.setCellStyle(style);
        row2.createCell(72).setCellStyle(style);
        row2.createCell(73).setCellStyle(style);

        cell2 = row2.createCell(74);
        cell2.setCellValue("ABC柱套件");
        cell2.setCellStyle(style);
        row2.createCell(75).setCellStyle(style);
        row2.createCell(76).setCellStyle(style);

        cell2 = row2.createCell(77);
        cell2.setCellValue("车顶");
        cell2.setCellStyle(style);
        row2.createCell(78).setCellStyle(style);
        row2.createCell(79).setCellStyle(style);

        cell2 = row2.createCell(80);
        cell2.setCellValue("门拉手");
        cell2.setCellStyle(style);
        row2.createCell(81).setCellStyle(style);
        row2.createCell(82).setCellStyle(style);

        cell2 = row2.createCell(83);
        cell2.setCellValue("反光镜");
        cell2.setCellStyle(style);
        row2.createCell(84).setCellStyle(style);
        row2.createCell(85).setCellStyle(style);

        cell2 = row2.createCell(86);
        cell2.setCellValue("整车");
        cell2.setCellStyle(style);
        row2.createCell(87).setCellStyle(style);
        row2.createCell(88).setCellStyle(style);

        cell2 = row2.createCell(89);
        cell2.setCellValue("整车");
        cell2.setCellStyle(style);
        row2.createCell(90).setCellStyle(style);
        row2.createCell(91).setCellStyle(style);

        cell2 = row2.createCell(92);
        cell2.setCellValue("整车");
        cell2.setCellStyle(style);
        row2.createCell(93).setCellStyle(style);
        row2.createCell(94).setCellStyle(style);

        HSSFCell cell3;
        for(int i = 14;i < 95;i=i+3){
            cell3 = row3.createCell(i);
            cell3.setCellValue("施工人员");
            cell3.setCellStyle(style);
            cell3 = row3.createCell(i+1);
            cell3.setCellValue("施工型号");
            cell3.setCellStyle(style);
            cell3 = row3.createCell(i+2);
            cell3.setCellValue("提成");
            cell3.setCellStyle(style);
        }


        //新增数据行，并且设置单元格数据
        HSSFCellStyle styleD = workbook.createCellStyle();
        styleD.setBorderBottom(BorderStyle.THIN);//下边框
        styleD.setBorderLeft(BorderStyle.THIN);//左边框
        styleD.setBorderTop(BorderStyle.THIN);//上边框
        styleD.setBorderRight(BorderStyle.THIN);//右边框
        if(viewList != null && viewList.size() > 0) {
            int rowNum = 3;
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
                for(int i = 14;i < 95;i++){
                    row.createCell(i).setCellValue("");
                }
                List<WorkDetailView> detailList = order.getWorkDetails();
                for(WorkDetailView detail : detailList){
                    if(checkDetail(detail, 1, "1")){
                        row.getCell(14).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "2")){
                        row.getCell(17).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "3")){
                        row.getCell(20).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "4")){
                        row.getCell(23).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "5")){
                        row.getCell(26).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "6")){
                        row.getCell(29).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "19")){
                        row.getCell(32).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "20")){
                        row.getCell(35).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "21")){
                        row.getCell(38).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "22")){
                        row.getCell(41).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "23")){
                        row.getCell(44).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "24")){
                        row.getCell(47).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 1, "25")){
                        row.getCell(50).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "7")){
                        row.getCell(53).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "8")){
                        row.getCell(56).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "9")){
                        row.getCell(59).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "10")){
                        row.getCell(62).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "11")){
                        row.getCell(65).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "12")){
                        row.getCell(68).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "13")){
                        row.getCell(71).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "14")){
                        row.getCell(74).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "15")){
                        row.getCell(77).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "16")){
                        row.getCell(80).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "17")){
                        row.getCell(83).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 2, "18")){
                        row.getCell(86).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 3, "18")){
                        row.getCell(89).setCellValue(detail.getTechName());
                    }
                    if(checkDetail(detail, 4, "18")){
                        row.getCell(92).setCellValue(detail.getTechName());
                    }
                }

                List<OrderProductView> productList = order.getOrderProducts();
                for(OrderProductView product : productList){
                    if(product.getConstructionProjectId() == 1){
                        if(product.getConstructionPositionId() == 1){
                            product.getScrapCost();
                            row.getCell(15).setCellValue(product.getModel());
                            row.getCell(16).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 2){
                            row.getCell(18).setCellValue(product.getModel());
                            row.getCell(19).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 3){
                            row.getCell(21).setCellValue(product.getModel());
                            row.getCell(22).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 4){
                            row.getCell(24).setCellValue(product.getModel());
                            row.getCell(25).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 5){
                            row.getCell(27).setCellValue(product.getModel());
                            row.getCell(28).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 6){
                            row.getCell(30).setCellValue(product.getModel());
                            row.getCell(31).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 19){
                            row.getCell(33).setCellValue(product.getModel());
                            row.getCell(34).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 20){
                            row.getCell(36).setCellValue(product.getModel());
                            row.getCell(37).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 21){
                            row.getCell(39).setCellValue(product.getModel());
                            row.getCell(40).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 22){
                            row.getCell(42).setCellValue(product.getModel());
                            row.getCell(43).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 23){
                            row.getCell(45).setCellValue(product.getModel());
                            row.getCell(46).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 24){
                            row.getCell(48).setCellValue(product.getModel());
                            row.getCell(49).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 25){
                            row.getCell(51).setCellValue(product.getModel());
                            row.getCell(52).setCellValue(product.getConstructionCommission());
                        }
                    }else if(product.getConstructionProjectId() == 2){
                        if(product.getConstructionPositionId() == 7){
                            row.getCell(54).setCellValue(product.getModel());
                            row.getCell(55).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 8){
                            row.getCell(57).setCellValue(product.getModel());
                            row.getCell(58).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 9){
                            row.getCell(60).setCellValue(product.getModel());
                            row.getCell(61).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 10){
                            row.getCell(63).setCellValue(product.getModel());
                            row.getCell(64).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 11){
                            row.getCell(66).setCellValue(product.getModel());
                            row.getCell(67).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 12){
                            row.getCell(69).setCellValue(product.getModel());
                            row.getCell(70).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 13){
                            row.getCell(72).setCellValue(product.getModel());
                            row.getCell(73).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 14){
                            row.getCell(75).setCellValue(product.getModel());
                            row.getCell(76).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 15){
                            row.getCell(78).setCellValue(product.getModel());
                            row.getCell(79).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 16){
                            row.getCell(81).setCellValue(product.getModel());
                            row.getCell(82).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 17){
                            row.getCell(84).setCellValue(product.getModel());
                            row.getCell(85).setCellValue(product.getConstructionCommission());
                        }else if(product.getConstructionPositionId() == 18){
                            row.getCell(87).setCellValue(product.getModel());
                            row.getCell(88).setCellValue(product.getConstructionCommission());
                        }
                    }else if(product.getConstructionProjectId() == 3){
                        row.getCell(90).setCellValue(product.getModel());
                        row.getCell(91).setCellValue(product.getConstructionCommission());
                    }else if(product.getConstructionProjectId() == 4){
                        row.getCell(93).setCellValue(product.getModel());
                        row.getCell(94).setCellValue(product.getConstructionCommission());
                    }
                }

                rowNum++;
                for (Cell c : row) {
                    if(c.getColumnIndex() != 6 && c.getColumnIndex() != 8 && c.getColumnIndex() != 9 && c.getColumnIndex() != 10 && c.getColumnIndex() != 11 && c.getColumnIndex() != 12){
                        c.setCellStyle(styleD);
                    }
                }
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

    @RequestMapping(value = "/excel/download/work/{techId}", method = RequestMethod.GET)
    public void download(@PathVariable("techId") int techId,
                         @RequestParam(value = "startTime", required = false) Long startTime,
                         @RequestParam(value = "endTime", required = false) Long endTime,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        Page<WorkDetailView> views = workDetailService.findViews(techId, 1, 300);
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
        sheet.setColumnWidth(6, 150 * 256);
        sheet.setColumnWidth(8, 150 * 256);
        sheet.setColumnWidth(10, 150 * 256);
        sheet.setColumnWidth(12, 150 * 256);
        sheet.setColumnWidth(14, 150 * 256);
        sheet.setColumnWidth(16, 150 * 256);

        HSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("订单编号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("技师姓名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("下单时间");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("结算金额");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("报废扣款");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("施工项目1");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("施工部位1");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("施工项目2");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("施工部位2");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("施工项目3");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("施工部位3");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("施工项目4");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("施工部位4");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("施工项目5");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("施工部位5");
        cell.setCellStyle(style);
        cell = row.createCell(15);
        cell.setCellValue("施工项目6");
        cell.setCellStyle(style);
        cell = row.createCell(16);
        cell.setCellValue("施工部位6");
        cell.setCellStyle(style);
        cell = row.createCell(17);
        cell.setCellValue("支付状态");
        cell.setCellStyle(style);


        //新增数据行，并且设置单元格数据
        if(views != null && views.getContent().size() > 0) {
            int rowNum = 1;
            for (WorkDetailView view : views.getContent()) {

                row = sheet.createRow(rowNum);
                cell = row.createCell(0);
                cell.setCellStyle(style);
                cell.setCellValue(view.getOrderNum());
                cell = row.createCell(1);
                cell.setCellStyle(style);
                cell.setCellValue(view.getTechName());
                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell.setCellValue(view.getCreateDate());
                cell = row.createCell(3);
                cell.setCellStyle(style1);
                cell.setCellValue(view.getPayment());
                cell = row.createCell(4);
                cell.setCellStyle(style1);
                cell.setCellValue(view.getTotalCost());
                cell = row.createCell(5);
                cell.setCellStyle(style);
                cell.setCellValue(view.getProjectName1());
                cell = row.createCell(6);
                cell.setCellStyle(style);
                cell.setCellValue(changePosition(view.getPosition1()));
                cell = row.createCell(7);
                cell.setCellStyle(style);
                cell.setCellValue(view.getProjectName2());
                cell = row.createCell(8);
                cell.setCellStyle(style);
                cell.setCellValue(changePosition(view.getPosition2()));
                cell = row.createCell(9);
                cell.setCellStyle(style);
                cell.setCellValue(view.getProjectName3());
                cell = row.createCell(10);
                cell.setCellStyle(style);
                cell.setCellValue(changePosition(view.getPosition3()));
                cell = row.createCell(11);
                cell.setCellStyle(style);
                cell.setCellValue(view.getProjectName4());
                cell = row.createCell(12);
                cell.setCellStyle(style);
                cell.setCellValue(changePosition(view.getPosition4()));
                cell = row.createCell(13);
                cell.setCellStyle(style);
                cell.setCellValue(view.getProjectName5());
                cell = row.createCell(14);
                cell.setCellStyle(style);
                cell.setCellValue(changePosition(view.getPosition5()));
                cell = row.createCell(15);
                cell.setCellStyle(style);
                cell.setCellValue(view.getProjectName6());
                cell = row.createCell(16);
                cell.setCellStyle(style);
                cell.setCellValue(changePosition(view.getPosition6()));


                String status = "";
                if (view.getPayStatus() == 0) {
                    status = "未出帐";
                }
                if (view.getPayStatus() == 1) {
                    status = "已出账";
                }
                if (view.getPayStatus() == 2) {
                    status = "已转账支付";
                }
                cell = row.createCell(17);
                cell.setCellStyle(style);
                cell.setCellValue(status);


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
            position = position.replaceAll("1", "前风挡");
            position = position.replaceAll("2", "左前门");
            position = position.replaceAll("3", "右前门");
            position = position.replaceAll("4", "左后门");
            position = position.replaceAll("5", "右后门");
            position = position.replaceAll("6", "后风挡");
            position = position.replaceAll("7", "前保险杠");
            position = position.replaceAll("8", "引擎盖");
            position = position.replaceAll("9", "左右前叶子板");
            position = position.replaceAll("10", "四门");
            position = position.replaceAll("11", "左右后叶子板");
            position = position.replaceAll("12", "尾盖");
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

        }

        return position;
    }

    @RequestMapping(value = "/excel/download/work", method = RequestMethod.GET)
    public void downloadAll(@RequestParam(value = "startTime", required = false) Long startTime,
                         @RequestParam(value = "endTime", required = false) Long endTime,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        List<Technician> techList = technicianService.findAll();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面

        response.reset();

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=workDetail" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls");


        HSSFWorkbook workbook = new HSSFWorkbook();
        for(Technician te : techList){
            Page<WorkDetailView> views = workDetailService.findViews(te.getId(), 1, 300);
            HSSFSheet sheet;
            if(te.getName() == null){
                sheet = workbook.createSheet(te.getPhone());
            }else{
                sheet = workbook.createSheet(te.getName() + "  " + te.getPhone());
            }

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
            sheet.setColumnWidth(6, 150 * 256);
            sheet.setColumnWidth(8, 150 * 256);
            sheet.setColumnWidth(10, 150 * 256);
            sheet.setColumnWidth(12, 150 * 256);
            sheet.setColumnWidth(14, 150 * 256);
            sheet.setColumnWidth(16, 150 * 256);

            HSSFCell cell;

            cell = row.createCell(0);
            cell.setCellValue("订单编号");
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellValue("技师姓名");
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue("下单时间");
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellValue("结算金额");
            cell.setCellStyle(style);
            cell = row.createCell(4);
            cell.setCellValue("报废扣款");
            cell.setCellStyle(style);
            cell = row.createCell(5);
            cell.setCellValue("施工项目1");
            cell.setCellStyle(style);
            cell = row.createCell(6);
            cell.setCellValue("施工部位1");
            cell.setCellStyle(style);
            cell = row.createCell(7);
            cell.setCellValue("施工项目2");
            cell.setCellStyle(style);
            cell = row.createCell(8);
            cell.setCellValue("施工部位2");
            cell.setCellStyle(style);
            cell = row.createCell(9);
            cell.setCellValue("施工项目3");
            cell.setCellStyle(style);
            cell = row.createCell(10);
            cell.setCellValue("施工部位3");
            cell.setCellStyle(style);
            cell = row.createCell(11);
            cell.setCellValue("施工项目4");
            cell.setCellStyle(style);
            cell = row.createCell(12);
            cell.setCellValue("施工部位4");
            cell.setCellStyle(style);
            cell = row.createCell(13);
            cell.setCellValue("施工项目5");
            cell.setCellStyle(style);
            cell = row.createCell(14);
            cell.setCellValue("施工部位5");
            cell.setCellStyle(style);
            cell = row.createCell(15);
            cell.setCellValue("施工项目6");
            cell.setCellStyle(style);
            cell = row.createCell(16);
            cell.setCellValue("施工部位6");
            cell.setCellStyle(style);
            cell = row.createCell(17);
            cell.setCellValue("支付状态");
            cell.setCellStyle(style);


            //新增数据行，并且设置单元格数据
            if(views != null && views.getContent().size() > 0) {
                int rowNum = 1;
                for (WorkDetailView view : views.getContent()) {

                    row = sheet.createRow(rowNum);
                    cell = row.createCell(0);
                    cell.setCellStyle(style);
                    cell.setCellValue(view.getOrderNum());
                    cell = row.createCell(1);
                    cell.setCellStyle(style);
                    cell.setCellValue(view.getTechName());
                    cell = row.createCell(2);
                    cell.setCellStyle(style);
                    cell.setCellValue(view.getCreateDate());
                    cell = row.createCell(3);
                    cell.setCellStyle(style1);
                    cell.setCellValue(view.getPayment());
                    cell = row.createCell(4);
                    cell.setCellStyle(style1);
                    cell.setCellValue(view.getTotalCost());
                    cell = row.createCell(5);
                    cell.setCellStyle(style);
                    cell.setCellValue(view.getProjectName1());
                    cell = row.createCell(6);
                    cell.setCellStyle(style);
                    cell.setCellValue(changePosition(view.getPosition1()));
                    cell = row.createCell(7);
                    cell.setCellStyle(style);
                    cell.setCellValue(view.getProjectName2());
                    cell = row.createCell(8);
                    cell.setCellStyle(style);
                    cell.setCellValue(changePosition(view.getPosition2()));
                    cell = row.createCell(9);
                    cell.setCellStyle(style);
                    cell.setCellValue(view.getProjectName3());
                    cell = row.createCell(10);
                    cell.setCellStyle(style);
                    cell.setCellValue(changePosition(view.getPosition3()));
                    cell = row.createCell(11);
                    cell.setCellStyle(style);
                    cell.setCellValue(view.getProjectName4());
                    cell = row.createCell(12);
                    cell.setCellStyle(style);
                    cell.setCellValue(changePosition(view.getPosition4()));
                    cell = row.createCell(13);
                    cell.setCellStyle(style);
                    cell.setCellValue(view.getProjectName5());
                    cell = row.createCell(14);
                    cell.setCellStyle(style);
                    cell.setCellValue(changePosition(view.getPosition5()));
                    cell = row.createCell(15);
                    cell.setCellStyle(style);
                    cell.setCellValue(view.getProjectName6());
                    cell = row.createCell(16);
                    cell.setCellStyle(style);
                    cell.setCellValue(changePosition(view.getPosition6()));


                    String status = "";
                    if (view.getPayStatus() == 0) {
                        status = "未出帐";
                    }
                    if (view.getPayStatus() == 1) {
                        status = "已出账";
                    }
                    if (view.getPayStatus() == 2) {
                        status = "已转账支付";
                    }
                    cell = row.createCell(17);
                    cell.setCellStyle(style);
                    cell.setCellValue(status);


                    rowNum++;
                }
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
}
