package com.autobon.platform.controller.app.merchant;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import com.autobon.cooperators.entity.ReviewCooper;
import com.autobon.cooperators.service.CoopAccountService;
import com.autobon.cooperators.service.CooperatorService;
import com.autobon.cooperators.service.ReviewCooperService;
import com.autobon.order.entity.Comment;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderView;
import com.autobon.order.service.*;
import com.autobon.order.vo.OrderConstructionShow;
import com.autobon.order.vo.OrderShow;
import com.autobon.order.vo.ProjectPositionShow;
import com.autobon.order.vo.WorkDetailShow;
import com.autobon.platform.listener.CooperatorEventListener;
import com.autobon.platform.listener.Event;
import com.autobon.platform.listener.OrderEventListener;
import com.autobon.shared.*;
import com.autobon.technician.entity.LocationStatus;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.LocationStatusService;
import com.autobon.technician.service.TechStatService;
import com.autobon.technician.service.TechnicianService;
import com.autobon.technician.vo.TechnicianLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by wh on 2016/11/10.
 */
@RestController
@RequestMapping("/api/mobile/coop")
public class MerchantController {

    @Autowired
    RedisCache redisCache;
    @Autowired
    CooperatorService cooperatorService;
    @Autowired
    CoopAccountService coopAccountService;
    @Autowired
    ReviewCooperService reviewCooperService;
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    OrderService orderService;
    @Autowired
    WorkDetailService workDetailService;
    @Autowired
    ConstructionProjectService constructionProjectService;
    @Autowired
    LocationStatusService locationStatusService;
    @Autowired
    TechnicianService technicianService;
    @Autowired
    CommentService commentService;
    @Autowired
    TechStatService techStatService;
    @Value("${com.autobon.uploadPath}") String uploadPath;

    @Autowired
    OrderViewService orderViewService;
    @Autowired
    OrderStatusRecordService orderStatusRecordService;


    /**
     * 商户注册
     * @param phone 手机号码
     * @param password 密码
     * @param verifySms 验证码
     * @return
     */
    @RequestMapping(value = "/merchant/register", method = RequestMethod.POST)
    public JsonResult register(@RequestParam("phone")     String phone,
                               @RequestParam("password")  String password,
                               @RequestParam("verifySms") String verifySms) {

        JsonResult jsonResult = new JsonResult(true);
        ArrayList<String> messages = new ArrayList<>();

        if (!Pattern.matches("^\\d{11}$", phone)) {
            messages.add("手机号格式错误");
        } else if (coopAccountService.getByPhone(phone) != null) {
            messages.add("手机号已被注册");
        }

        if (password.length() < 6) {
            messages.add("密码至少6位");
        }
        String code = redisCache.get("verifySms:" + phone);
        if (!verifySms.equals(code)) {
            messages.add("验证码错误");
        }

        if (messages.size() > 0) {
            jsonResult.setStatus(false);
            jsonResult.setMessage(messages.stream().collect(Collectors.joining(",")));
        } else {
            CoopAccount coopAccount = new CoopAccount();
            coopAccount.setPhone(phone);
            coopAccount.setPassword(coopAccount.encryptPassword(password));
            coopAccountService.save(coopAccount);
            jsonResult.setMessage(coopAccount);
        }
        return jsonResult;
    }


    /**
     * 商户登陆
     * @param phone
     * @param password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/merchant/login", method = RequestMethod.POST)
    public JsonResult login(@RequestParam("phone") String phone,
                            @RequestParam("password") String password,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        JsonResult jsonResult = new JsonResult(true);
        CoopAccount coopAccount = coopAccountService.getByPhone(phone);

        if (coopAccount == null) {
            jsonResult.setStatus(false);
            jsonResult.setMessage("手机号未注册");
        } else if (!coopAccount.getPassword().equals(CoopAccount.encryptPassword(password))) {
            jsonResult.setStatus(false);
            jsonResult.setMessage("密码错误");
        } else if(coopAccount.isFired()){
            jsonResult.setStatus(false);
            jsonResult.setMessage("该员工已离职");
        } else {
            response.addCookie(new Cookie("autoken", CoopAccount.makeToken(coopAccount.getId())));
            coopAccount.setLastLoginTime(new Date());
            coopAccount.setLastLoginIp(request.getRemoteAddr());
            coopAccountService.save(coopAccount);

            Cooperator cooperator = null;
            ReviewCooper reviewCooper = null;
            int cooperatorId = 0;
            cooperatorId = coopAccount.getCooperatorId();
            if(cooperatorId>0){
                cooperator = cooperatorService.get(cooperatorId);
                List<ReviewCooper> reviewCooperList = reviewCooperService.getByCooperatorId(cooperatorId);
                if(reviewCooperList.size()>0){
                    reviewCooper = reviewCooperList.get(0);
                }
            }
            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("coopAccount", coopAccount);
            dataMap.put("cooperator", cooperator);
            dataMap.put("reviewCooper", reviewCooper);
            jsonResult.setMessage(dataMap);
        }
        return jsonResult;
    }


    /**
     * 商户认证
     * @param request
     * @param enterpriseName
     * @param businessLicensePic
     * @param longitude
     * @param latitude
     * @return
     */
    @RequestMapping(value = "/merchant/certificate",method = RequestMethod.POST)
    public JsonResult certificate(HttpServletRequest request,
                                   @RequestParam("enterpriseName") String enterpriseName,
                                   @RequestParam("businessLicensePic") String businessLicensePic,
                                   @RequestParam(value = "longitude",required = false) String longitude,
                                   @RequestParam(value ="latitude",required = false) String latitude){

        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int coopId = coopAccount.getCooperatorId();

        //没有认证，继续认证
        if(coopId == 0){
            Cooperator cooperator = new Cooperator();
            cooperator.setFullname(enterpriseName);
            cooperator.setBussinessLicensePic(businessLicensePic);
            cooperator.setLongitude(longitude);
            cooperator.setLatitude(latitude);
            cooperator.setStatusCode(0);
            cooperatorService.save(cooperator);

            coopAccount.setCooperatorId(cooperator.getId());
            coopAccount.setIsMain(true);
            coopAccountService.save(coopAccount);
            publisher.publishEvent(new CooperatorEventListener.CooperatorEvent(cooperator, Event.Action.CREATED));
            return new JsonResult(true,  cooperator);
        }else {
            //是否认证成功，成功则止。认证失败，再认证
            Cooperator cooperator = cooperatorService.get(coopId);
            if (cooperator == null) {
                return new JsonResult(false, "没有此关联商户");
            } else {
                int statusCode = cooperator.getStatusCode();
                if (statusCode == 2) {
                    cooperator.setFullname(enterpriseName);
                    cooperator.setBussinessLicensePic(businessLicensePic);
                    cooperator.setLongitude(longitude);
                    cooperator.setLatitude(latitude);
                    cooperator.setStatusCode(0);
                    cooperatorService.save(cooperator);
                    return new JsonResult(true,  cooperator);
                } else if (statusCode == 1) {
                    return new JsonResult(true, "你已经认证成功");
                } else if (statusCode == 0) {
                    return new JsonResult(true, "等待审核");
                } else {
                    return new JsonResult(false, "商户状态码不正确");
                }
            }
        }

    }


    /**
     * 商户认证上传营业执照图片
     * @param request
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/merchant/bussinessLicensePic",method = RequestMethod.POST)
    public JsonResult uploadBussinessLicensePic(HttpServletRequest request,
                                                 @RequestParam("file") MultipartFile file) throws  Exception{
        String path ="/uploads/coop/bussinessLicensePic";
        if (file == null || file.isEmpty()) return new JsonResult(false,  "没有上传文件");
        JsonResult jsonResult = new JsonResult(true);

        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;

        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        jsonResult.setMessage(path + "/" + filename);
        return jsonResult;
    }


    /**
     * 查看 商户详情信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/merchant",method = RequestMethod.GET)
    public JsonResult getCoop(HttpServletRequest request) throws  Exception{
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        Integer coopId = coopAccount.getCooperatorId();
        if(coopId != null){
            Cooperator cooperator = cooperatorService.get(coopId);
            return new JsonResult(true, cooperator);
        }
        return new JsonResult(false,"商户不存在或没有通过认证");
    }


    /**
     * 商户审核信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/merchant/coopCheckResult",method = RequestMethod.GET)
    public JsonResult coopCheckResult(HttpServletRequest request) throws  Exception{
        Map<String,Object> dataMap = new HashMap<String,Object>();

        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        Integer coopId = coopAccount.getCooperatorId();
        Cooperator cooperator = cooperatorService.get(coopId);
        List<ReviewCooper> reviewCooperList = reviewCooperService.getByCooperatorId(coopId);
        if(reviewCooperList.size()>0){
            dataMap.put("reviewCooper",reviewCooperList.get(0));
        }else{
            dataMap.put("reviewCooper",null);
        }

        dataMap.put("cooperator",cooperator);
        return new JsonResult(true ,dataMap);
    }



    /**
     * 查询技师
     * @param query 查询内容 纯数字则查询手机 反之查询姓名
     * @param page 页码
     * @param pageSize 页面大小
     * @return JsonResult对象
     */
    @RequestMapping(value = "/merchant/technician",method = RequestMethod.GET)
    public JsonResult getTechnician(@RequestParam("query") String query,
                                    @RequestParam(value = "page",  defaultValue = "1" )  int page,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    HttpServletRequest request){

        try {
            Technician tech = (Technician) request.getAttribute("user");
            if (tech == null) {
                return new JsonResult(false, "登陆过期");
            }
            Page<Technician> technicians;
            String query1 = "%" + query + "%";
            if (Pattern.matches("\\d+", query)) {
                technicians = technicianService.find(query1, null, page, pageSize);

            } else {
                technicians = technicianService.find(null, query1, page, pageSize);
            }

            return new JsonResult(true, new JsonPage<>(technicians));
        }catch (Exception e){
            return  new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 合作商户的订单统计
     * @param request
     * @return
     */
    @RequestMapping(value="/merchant/order/orderCount",method = RequestMethod.GET)
    public JsonResult orderCount(HttpServletRequest request){
        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int coopId = coopAccount.getCooperatorId();
        int coopAccountId = coopAccount.getId();
        boolean isMain = coopAccount.isMain();

        if(isMain){
            Cooperator cooperator = cooperatorService.get(coopId);
            int orderNum = cooperator.getOrderNum();
            return new JsonResult(true, orderNum);
        }else{
            return new JsonResult(true,  orderService.countOfCoopAccount(coopAccountId));
        }

    }


    /**
     * 查询订单技师详情
     * @param techId
     * @param request
     * @return
     */
    @RequestMapping(value = "/merchant/technician/{techId}",method = RequestMethod.GET)
    public JsonResult getTechnician(@PathVariable("techId") int techId,
                                    HttpServletRequest request){




        TechnicianLocation locationStatus = technicianService.getById(techId);
        if(locationStatus != null){
            return  new JsonResult(true, locationStatus);
        }
        return new JsonResult(false, "技师不存在");


    }

    /**
     *
     * 商户下单
     * @param request
     * @param photo
     * @param remark
     * @param agreedStartTime
     * @param agreedEndTime
     * @param type
     * @param pushToAll
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/merchant/order",method = RequestMethod.POST)
    public JsonResult createOrder(HttpServletRequest request,
                                  @RequestParam("photo") String photo,
                                  @RequestParam("remark") String remark,
                                  @RequestParam("agreedStartTime") String agreedStartTime,
                                  @RequestParam("agreedEndTime") String agreedEndTime,
                                  @RequestParam("type") String type,
                                  @RequestParam(value = "pushToAll", defaultValue = "true") boolean pushToAll) throws Exception {


        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int coopId = coopAccount.getCooperatorId();
        Cooperator cooperator = cooperatorService.get(coopId);
        int statusCode = cooperator.getStatusCode();
        if(statusCode !=1) {
            return new JsonResult(false,   "商户未通过验证");
        }

        if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", agreedStartTime)
                ||!Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", agreedEndTime)) {
            return new JsonResult(false,   "订单时间格式不对, 正确格式: 2016-02-10 09:23");
        }

        Order order = new Order();
        order.setCreatorId(coopAccount.getId());
        order.setCoopId(coopId);
        order.setCreatorName(coopAccount.getName());
        order.setPhoto(photo);
        order.setRemark(remark);
        order.setAgreedStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(agreedStartTime));
        order.setAgreedEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(agreedEndTime));
        order.setAddTime(new Date());
        // order.setType(type);
        order.setPositionLon(cooperator.getLongitude());
        order.setPositionLat(cooperator.getLatitude());
        order.setContactPhone(coopAccount.getPhone());
        if (!pushToAll) order.setStatus(Order.Status.CREATED_TO_APPOINT);

        String[] types = type.split(",");
        for(String s : types){
            order.setType(s);
            orderService.save(order);
        }
        cooperator.setOrderNum(cooperator.getOrderNum() + 1);
        cooperatorService.save(cooperator);

        publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.CREATED));
        return new JsonResult(true,   order);
    }


    /**
     * 查询订单详情
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/merchant/order/{orderId:\\d+}", method = RequestMethod.GET)
    public JsonResult getOrder(@PathVariable("orderId") int orderId,
                               HttpServletRequest request) {


        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        if(coopAccount == null){
            return new JsonResult(false, "登陆过期");
        }
        Order order = orderService.get(orderId);

        if (order == null||order.getCoopId()!= coopAccount.getCooperatorId()) {
            return new JsonResult(false,  "没有这个订单");
        }
        OrderView orderShow = orderViewService.findById(orderId);

        LocationStatus locationStatus =locationStatusService.findByTechId(orderShow.getTechId());
        if(locationStatus != null){
            orderShow.setTechLatitude(locationStatus.getLat());
            orderShow.setTechLongitude(locationStatus.getLng());
        }

        List<WorkDetailShow> workDetailShowList = workDetailService.getByOrderId(orderId);
        Map<Integer, String> projectMap = constructionProjectService.getProject();
        Map<Integer, String> positionMap = constructionProjectService.getPosition();

        List<OrderConstructionShow> constructionShowList = new ArrayList<>();
        for(WorkDetailShow workDetailShow: workDetailShowList) {
            OrderConstructionShow orderConstructionShow = new OrderConstructionShow();
            List<ProjectPositionShow> projectPositionShows = new ArrayList<>();
            orderConstructionShow.setTechId(workDetailShow.getTechId());
            orderConstructionShow.setTechName(workDetailShow.getTechName());
            orderConstructionShow.setIsMainTech(workDetailShow.getTechId() == order.getMainTechId()?1:0);
            orderConstructionShow.setPayStatus(workDetailShow.getPayStatus());
            orderConstructionShow.setPayment(workDetailShow.getPayment());
            if (workDetailShow.getPosition1() != null && workDetailShow.getPosition1() != null) {
                String projectName = projectMap.get(workDetailShow.getProject1());
                String position = workDetailShow.getPosition1();
                String[] positionArr = position.split(",");
                String positionStr = "";
                for (String positionId : positionArr) {
                    if (positionStr.length() > 0) {
                        positionStr += "," + positionMap.get(Integer.valueOf(positionId));
                    } else {
                        positionStr += positionMap.get(Integer.valueOf(positionId));
                    }
                }
                ProjectPositionShow projectPositionShow = new ProjectPositionShow(projectName, positionStr);
                projectPositionShows.add(projectPositionShow);
            }
            if (workDetailShow.getPosition2() != null && workDetailShow.getPosition2() != null) {

                String projectName = projectMap.get(workDetailShow.getProject2());

                String position = workDetailShow.getPosition2();
                String[] positionArr = position.split(",");
                String positionStr = "";
                for (String positionId : positionArr) {
                    if (positionStr.length() > 0) {
                        positionStr += "," + positionMap.get(Integer.valueOf(positionId));
                    } else {
                        positionStr += positionMap.get(Integer.valueOf(positionId));
                    }
                }
                ProjectPositionShow projectPositionShow = new ProjectPositionShow(projectName, positionStr);
                projectPositionShows.add(projectPositionShow);
            }
            if (workDetailShow.getPosition3() != null && workDetailShow.getPosition3() != null) {

                String projectName = projectMap.get(workDetailShow.getProject3());

                String position = workDetailShow.getPosition3();
                String[] positionArr = position.split(",");
                String positionStr = "";
                for (String positionId : positionArr) {
                    if (positionStr.length() > 0) {
                        positionStr += "," + positionMap.get(Integer.valueOf(positionId));
                    } else {
                        positionStr += positionMap.get(Integer.valueOf(positionId));
                    }
                }
                ProjectPositionShow projectPositionShow = new ProjectPositionShow(projectName, positionStr);
                projectPositionShows.add(projectPositionShow);
            }
            if (workDetailShow.getPosition4() != null && workDetailShow.getPosition4() != null) {

                String projectName = projectMap.get(workDetailShow.getProject4());

                String position = workDetailShow.getPosition4();
                String[] positionArr = position.split(",");
                String positionStr = "";
                for (String positionId : positionArr) {
                    if (positionStr.length() > 0) {
                        positionStr += "," + positionMap.get(Integer.valueOf(positionId));
                    } else {
                        positionStr += positionMap.get(Integer.valueOf(positionId));
                    }
                }
                ProjectPositionShow projectPositionShow = new ProjectPositionShow(projectName, positionStr);
                projectPositionShows.add(projectPositionShow);
            }

            orderConstructionShow.setProjectPosition(projectPositionShows);
            constructionShowList.add(orderConstructionShow);
        }
        String type = orderShow.getType();
        if(type!=null&& type.length()>0) {
            String[] projectArr = type.split(",");
            String projectStr = "";
            for (String projectId : projectArr) {
                if (projectStr.length() > 0) {
                    projectStr += "," + projectMap.get(Integer.valueOf(projectId));
                } else {
                    projectStr += projectMap.get(Integer.valueOf(projectId));
                }
            }
            orderShow.setType(projectStr);
        }

        orderShow.setOrderConstructionShow(constructionShowList);
        return new JsonResult(true, orderShow);
    }


    /**
     *
     * 商户上传订单图片
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/merchant/order/uploadPhoto",method = RequestMethod.POST)
    public JsonResult uploadPhoto(@RequestParam("file") MultipartFile file) throws  Exception{
        String path ="/uploads/order/photo";
        if (file == null || file.isEmpty()) return new JsonResult(false,  "没有上传文件");
        JsonResult jsonResult = new JsonResult(true);
        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;

        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        jsonResult.setMessage(path + "/" + filename);
        return jsonResult;
    }


    /**
     * 商户指派技师
     * @param orderId
     * @param techId
     * @return
     */
    @RequestMapping(value = "/merchant/order/appoint", method = RequestMethod.POST)
    public JsonResult appointOrder(@RequestParam("orderId") int orderId,
                                   @RequestParam("techId") int techId) {
        Order order = orderService.get(orderId);
        Technician tech = technicianService.get(techId);
        if (order.getStatus() != Order.Status.CREATED_TO_APPOINT)
            return new JsonResult(false,  "订单不可指定技师");

        if (tech == null) {
            return new JsonResult(false,  "技师不存在");
        }

        order.setMainTechId(techId);
        order.setTakenTime(new Date());
        order.setStatus(Order.Status.TAKEN_UP);
        orderService.save(order);
        publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.APPOINTED));
        return new JsonResult(true);
    }


    /**
     * 商户撤销订单
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/merchant/order/{orderId:\\d+}/cancel", method = RequestMethod.PUT)
    public JsonResult cancelOrder(HttpServletRequest request,
                                  @PathVariable("orderId") int orderId) {
        CoopAccount account = (CoopAccount) request.getAttribute("user");
        Order order = orderService.get(orderId);
        if (order == null || order.getCreatorId() != account.getId() || order.getCoopId() != account.getCooperatorId()) {
            return new JsonResult(false,  "你没有此定单");
        }


        if (order.getStatusCode() < Order.Status.IN_PROGRESS.getStatusCode()) {
            order.setStatus(Order.Status.CANCELED);
            orderService.save(order);
            publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.CANCELED));
            return new JsonResult(true);
        } else {
            return new JsonResult(false,  "已开始施工的订单不可撤销");
        }
    }


    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/merchant/technician/distance", method = RequestMethod.GET)
    public JsonResult getDistance(@RequestParam(value = "page",  defaultValue = "1" )  int page,
                                  @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                  HttpServletRequest request) {

        CoopAccount account = (CoopAccount) request.getAttribute("user");
        int coopId =  account.getCooperatorId();
        Cooperator cooperator =  cooperatorService.get(coopId);
        return new JsonResult(true,locationStatusService.getTechByDistance(cooperator.getLatitude(), cooperator.getLongitude(), page, pageSize));

    }


    /**
     *
     * @param query
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/merchant/technician/assign", method = RequestMethod.GET)
    public JsonResult getDistance(@RequestParam(value = "query",  required = true )  String query,
                                  @RequestParam(value = "longitude",required = false) String longitude,
                                  @RequestParam(value ="latitude",required = false) String latitude,
                                  @RequestParam(value = "page",  defaultValue = "1" )  int page,
                                  @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                  HttpServletRequest request) {

        CoopAccount account = (CoopAccount) request.getAttribute("user");


        return new JsonResult(true,locationStatusService.getTechByName(latitude,longitude,query, page, pageSize));

    }


    /**
     * 商户查询订单
     * @param status 1未完成  2 已完成  3未评价 4 全部订单
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/merchant/order", method = RequestMethod.GET)
    public JsonResult getOdrders( @RequestParam(value = "status", defaultValue = "1") int status,
                                  @RequestParam(value = "workDate",required = false) String workDate,
                                  @RequestParam(value = "vin",required = false) String vin,
                                  @RequestParam(value = "phone",required = false) String phone,
                                  @RequestParam(value = "page",  defaultValue = "1" )  int page,
                                  @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                  HttpServletRequest request) {

        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
        int coopId = coopAccount.getCooperatorId();

        Page<OrderView> orders;
        orders = orderViewService.findCoopOrder(coopId, status, workDate, vin, phone, page, pageSize);
        return new JsonResult(true,orders);

    }



    /**
     * 商户订单评价
     * @param orderId
     * @param star
     * @param arriveOnTime
     * @param completeOnTime
     * @param professional
     * @param dressNeatly
     * @param carProtect
     * @param goodAttitude
     * @param advice
     * @return
     */
    @RequestMapping(value = "/merchant/order/comment", method = RequestMethod.POST)
    public JsonResult comment(@RequestParam("orderId") int orderId,
                               @RequestParam("star") int star,
                               @RequestParam(value = "arriveOnTime", defaultValue = "false") boolean arriveOnTime,
                               @RequestParam(value = "completeOnTime", defaultValue = "false") boolean completeOnTime,
                               @RequestParam(value = "professional", defaultValue = "false") boolean professional,
                               @RequestParam(value = "dressNeatly", defaultValue = "false") boolean dressNeatly,
                               @RequestParam(value = "carProtect", defaultValue = "false") boolean carProtect,
                               @RequestParam(value = "goodAttitude", defaultValue = "false") boolean goodAttitude,
                               @RequestParam("advice") String advice) {

        JsonResult jsonResult = new JsonResult(true, "comment");
        Order order = orderService.get(orderId);
        if (order.getStatus() == Order.Status.FINISHED) {

            int mainTechId = order.getMainTechId();
            if (mainTechId == 0) {
                return new JsonResult(false, "此订单未指定技师");
            }

            Comment comment = new Comment();
            comment.setTechId(mainTechId);
            comment.setOrderId(orderId);
            comment.setStar(star);
            comment.setArriveOnTime(arriveOnTime);
            comment.setCompleteOnTime(completeOnTime);
            comment.setProfessional(professional);
            comment.setDressNeatly(dressNeatly);
            comment.setCarProtect(carProtect);
            comment.setGoodAttitude(goodAttitude);
            comment.setAdvice(advice);
            commentService.save(comment);

            order.setStatus(Order.Status.COMMENTED);
            orderService.save(order);

            // 写入技师星级统计
            TechStat mainStat = techStatService.getByTechId(mainTechId);
            if (mainStat == null) {
                mainStat = new TechStat();
                mainStat.setTechId(mainTechId);
            }
            int commentCount = commentService.countByTechId(mainTechId);
            float starRate = commentService.calcStarRateByTechId(mainTechId,
                    Date.from(LocalDate.now().minusMonths(12).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            if (commentCount < 100) starRate = ((100 - commentCount) * 3f + commentCount * starRate) / 100f;
            mainStat.setStarRate(starRate);
            techStatService.save(mainStat);

            return jsonResult;

        } else {
            return new JsonResult(false, "订单未完成或已评论");
        }
    }

    @RequestMapping(value = "/merchant/order/{orderId:\\d+}/status/score", method = RequestMethod.GET)
    public JsonResult getOdrders(HttpServletRequest request,
                                 @PathVariable("orderId") int orderId) {

        List list = orderStatusRecordService.findByOrderId(orderId);
        return new JsonResult(true, list);

    }

}
