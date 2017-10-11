package com.autobon.platform.controller.app.technician;

import com.autobon.order.entity.Order;
import com.autobon.order.entity.OrderPartnerView;
import com.autobon.order.entity.OrderView;
import com.autobon.order.entity.WorkDetail;
import com.autobon.order.service.*;
import com.autobon.order.vo.*;
import com.autobon.platform.listener.Event;
import com.autobon.platform.listener.OrderEventListener;
import com.autobon.platform.listener.TechnicianEventListener;
import com.autobon.shared.*;
import com.autobon.technician.entity.Location;
import com.autobon.technician.entity.TechCashApply;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.*;
import com.autobon.technician.vo.LocationShow;
import com.autobon.technician.vo.TechCashApplyShow;
import com.autobon.technician.vo.TechnicianShow;
import com.autobon.technician.vo.TechnicianSuperShow;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 车邻邦二期接口
 * 接口重构
 * 返回JsonResult对象 格式{"status":true, "message":""}
 * Created by wh on 2016/11/9.
 */

@RestController
@RequestMapping("/api/mobile/technician")
public class TechnicianV2Controller {

    @Autowired
    TechnicianService technicianService;
    @Autowired
    LocationService locationService;
    @Autowired
    OrderService orderService;
    @Autowired
    ConstructionService constructionService;
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    WorkDetailService workDetailService;
    @Autowired
    ConstructionProjectService constructionProjectService;
    @Autowired
    DetailedTechnicianService detailedTechnicianService;
    @Autowired
    RedisCache redisCache;
    @Autowired
    SmsSender smsSender;
    @Autowired
    MultipartResolver resolver;

    @Autowired
    DetailedOrderService detailedOrderService;
    @Autowired
    ReassignmentService reassignmentService;
    @Autowired
    CommentService commentService;
    @Autowired
    OrderViewService orderViewService;

    @Autowired
    ConstructionWasteService constructionWasteService;

    @Autowired
    TechStatService techStatService;

    @Autowired
    TechCashApplyService techCashApplyService;

    @Value("${com.autobon.gm-path}") String gmPath;
    @Value("${com.autobon.uploadPath}") String uploadPath;


    /**
     * 技师查询自己信息
     * @param request HTTP请求
     * @return JsonResult 对象
     */
    @RequestMapping(value = "/v2/me", method = RequestMethod.GET)
    public JsonResult getTechnicianInfo(HttpServletRequest request) {
        try {
            Technician technician = (Technician) request.getAttribute("user");
            if (technician == null) {
                return new JsonResult(false, "登陆超时");
            }

            TechnicianSuperShow technicianSuperShow  = new TechnicianSuperShow();
            technicianSuperShow.setTechnician(technicianService.get(technician.getId()));
            TechStat techStat = techStatService.getByTechId(technician.getId());
            if(techStat != null){
                technicianSuperShow.setStarRate(String.valueOf(techStat.getStarRate()));
                technicianSuperShow.setTotalOrders(String.valueOf(techStat.getTotalOrders()));
                technicianSuperShow.setBalance(String.valueOf(techStat.getBalance()));
                technicianSuperShow.setUnpaidOrders(String.valueOf(techStat.getUnpaidOrders()));
            }
            return new JsonResult(true, technicianSuperShow);
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 技师注册
     * @param phone 登陆名（手机格式）
     * @param password 密码
     * @param verifySms 验证码
     * @return
     */
    @RequestMapping(value = "/v2/register", method = RequestMethod.POST)
    public JsonResult register(@RequestParam("phone")     String phone,
                               @RequestParam("password")  String password,
                               @RequestParam("verifySms") String verifySms) {

        try {
            JsonResult jsonResult = new JsonResult();
            ArrayList<String> messages = new ArrayList<>();

            if (!Pattern.matches("^\\d{11}$", phone)) {
                messages.add("手机号格式错误");
            } else if (technicianService.getByPhone(phone) != null) {
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
                Technician technician = new Technician();
                technician.setPhone(phone);
                technician.setWorkStatus(3);
                technician.setPassword(Technician.encryptPassword(password));
                technicianService.save(technician);
                publisher.publishEvent(new TechnicianEventListener.TechnicianEvent(technician, Event.Action.CREATED));

                TechStat techStat = new TechStat();
                techStat.setTechId(technician.getId());
                techStatService.save(techStat);
                jsonResult.setStatus(true);
                jsonResult.setMessage(technician);
            }
            return jsonResult;
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 技师登陆
     * @param phone 手机号码
     * @param password 密码
     * @param request HTTP请求
     * @param response HTTP请求
     * @return
     */
    @RequestMapping(value = "/v2/login", method = RequestMethod.POST)
    public JsonResult login(@RequestParam("phone")    String phone,
                            @RequestParam("password") String password,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        try {
            JsonResult jsonResult = new JsonResult(true);
            Technician technician = technicianService.getByPhone(phone);

            if (technician == null) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("手机号未注册");
            } else if (!technician.getPassword().equals(Technician.encryptPassword(password))) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("密码错误");
            } else {
                response.addCookie(new Cookie("autoken", Technician.makeToken(technician.getId())));
                technician.setLastLoginAt(new Date());
                technician.setLastLoginIp(request.getRemoteAddr());
                technicianService.save(technician);
                jsonResult.setMessage(technician);
            }
            return jsonResult;
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 技师重置密码
     * @param phone 手机号码
     * @param password 密码
     * @param verifySms 验证码
     * @return
     */
    @RequestMapping(value = "/v2/resetPassword", method = RequestMethod.PUT)
    public JsonResult resetPassword(@RequestParam("phone")     String phone,
                                    @RequestParam("password")  String password,
                                    @RequestParam("verifySms") String verifySms) {

        try {
            JsonResult jsonResult = new JsonResult(true);
            Technician technician = technicianService.getByPhone(phone);
            if (technician == null) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("手机号未注册");
            } else if (!verifySms.equals(redisCache.get("verifySms:" + phone))) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("验证码错误");
            } else if (password.length() < 6) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("密码至少6位");
            } else {
                technician.setPassword(Technician.encryptPassword(password));
                technicianService.save(technician);
            }
            return jsonResult;
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     *
     * @param oldPassword
     * @param newPassword
     * @param request
     * @return
     */
    @RequestMapping(value = "/v2/changePassword", method = RequestMethod.PUT)
    public JsonResult changePassword(@RequestParam("oldPassword") String oldPassword,
                                     @RequestParam("newPassword") String newPassword,
                                     HttpServletRequest request) {
        try {
            Technician technician = (Technician) request.getAttribute("user");
            if (technician == null) {
                return new JsonResult(false, "登陆超时");
            }
            if (newPassword.length() < 6) {
                return new JsonResult(false, "密码至少6位");
            } else {
                if (!technician.getPassword().equals(Technician.encryptPassword(oldPassword))) {
                    return new JsonResult(false, "原密码错误");
                }
                technician.setPassword(Technician.encryptPassword(newPassword));
                technicianService.save(technician);
            }
            return new JsonResult(true, "修改密码成功");
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 上传头像
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/v2/avatar", method = RequestMethod.POST)
    public JsonResult uploadAvatarForm(HttpServletRequest request) throws Exception {
        try{
            String path = "/uploads/technician/avatar";
            File dir = new File(new File(uploadPath).getCanonicalPath() + path);
            if (!dir.exists()) dir.mkdirs();
            Technician technician = (Technician) request.getAttribute("user");
            if (technician == null) {
                return new JsonResult(false, "登陆超时");
            }
            String filename = UUID.randomUUID() + ".jpg";

      //      InputStream in;
            if (request.getContentLengthLong() >= 2*1024*1024) {
                throw new MaxUploadSizeExceededException(2*1024*1024);
            }
            if (resolver.isMultipart(request)) {
                MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
                if (file == null || file.isEmpty()) return new JsonResult(false, "没有选择上传文件");
         //       in = file.getInputStream();
            } else {
                String ctype = request.getHeader("content-type");
                if (!ctype.equals("image/jpeg") && !ctype.equals("image/png")) {
                    return new JsonResult(false, "没有上传文件，或非jpg、png类型文件");
                }
                if (request.getContentLengthLong() <= 0) {
                    return new JsonResult(false, "没有选择上传文件");
                }
           //     in = request.getInputStream();
            }

//            ConvertCmd cmd = new ConvertCmd(true);
//            cmd.setSearchPath(gmPath);
//            cmd.setInputProvider(new Pipe(in, null));
//            IMOperation operation = new IMOperation();
//            operation.addImage("-");
//            operation.resize(200, 200, "^").gravity("center").extent(200, 200);
//            operation.addImage(dir.getAbsolutePath() + File.separator + filename);
//            cmd.run(operation);
//            in.close();
            technician.setAvatar(path + "/" + filename);
            technicianService.save(technician);
            ((MultipartHttpServletRequest) request).getFile("file").transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
            return new JsonResult(true, technician.getAvatar());
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 更新用户的个推ID
     * @param pushId
     * @return
     */
    @RequestMapping(value = "/v2/pushId", method = RequestMethod.POST)
    public JsonResult savePushId(HttpServletRequest request,
                                 @RequestParam("pushId") String pushId) {

        try{
            Technician technician = (Technician) request.getAttribute("user");
            Technician oTech = technicianService.getByPushId(pushId);

            if (oTech != null) {
                if (technician.getId() == oTech.getId()) return new JsonResult(true);
                else {
                    oTech.setPushId(null);
                    technicianService.save(oTech);
                }
            }
            technician.setPushId(pushId);
            technicianService.save(technician);
            return new JsonResult(true);

        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 上传身份证照片
     * @param request
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/v2/idPhoto", method = RequestMethod.POST)
    public JsonResult uploadIdPhoto(HttpServletRequest request,
                                    @RequestParam("file") MultipartFile file) throws Exception {
        try{

            if (file == null || file.isEmpty()) return new JsonResult(false, "没有上传文件");

            String path = "/uploads/technician/idPhoto";
            File dir = new File(new File(uploadPath).getCanonicalPath() + path);
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
            String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    + VerifyCode.generateRandomNumber(6) + extension;

            if (!dir.exists()) dir.mkdirs();
            file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
            return new JsonResult(true,  path + "/" + filename);
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }

    }


    /**
     * 查询技师
     * @param query 查询内容 纯数字则查询手机 反之查询姓名
     * @param page 页码
     * @param pageSize 页面大小
     * @return JsonResult对象
     */
    @RequestMapping(value = "/v2",method = RequestMethod.GET)
    public JsonResult getTechnician(@RequestParam(value = "query",required = false) String query,
                                    @RequestParam(value = "page",  defaultValue = "1" )  int page,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    HttpServletRequest request){

        try {
            Technician tech = (Technician) request.getAttribute("user");
            if (tech == null) {
                return new JsonResult(false, "登陆过期");
            }
            Integer techId = tech.getId();

            Page<Technician> technicians;
            if(query != null) {
                String query1 = "%" + query + "%";
                if (Pattern.matches("\\d+", query)) {
                    technicians = technicianService.findTech(query1, null,techId, page, pageSize);

                } else {
                    technicians = technicianService.findTech(null, query1, techId, page, pageSize);
                }

            }else{
                technicians = technicianService.findTech(null, null,techId, page, pageSize);
            }
            return new JsonResult(true, new JsonPage<>(technicians));
        }catch (Exception e){
            return  new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 技师上报位置
     * @param locationShow 技师位置信息（接收JSON）
     * @param request HTTP请求
     * @return  JsonResult对象
     */
    @RequestMapping(value="/v2/location", method = RequestMethod.POST)
    public JsonResult addLocation(@RequestBody LocationShow locationShow,
                                  HttpServletRequest request) {

        try {
            Technician technician = (Technician) request.getAttribute("user");
            if (technician == null) {
                return new JsonResult(false, "登陆过期");
            }
            //查询最近的位置信息
            Page<Location> locations = locationService.findByTechId(technician.getId(), 1, 1);
            if (locations.getNumberOfElements() > 0) {
                Location l = locations.getContent().get(0);
                if (new Date().getTime() - l.getCreateAt().getTime() < 60 * 1000) {
                    return new JsonResult(false, "请求间隔不得少于1分钟");
                }
            }
            Location location = new Location(technician.getId(), locationShow);
            locationService.save(location);
            return new JsonResult(true, "上报位置信息成功");
        }catch (Exception e){
            return  new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 技师认证
     * @param technicianShow
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/v2/certificate", method = RequestMethod.POST)
    public JsonResult authentication(@RequestBody TechnicianShow technicianShow,
                                     HttpServletRequest request)throws Exception {

        try{
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
            technician.setSkill(technicianShow.getSkill());
            technician.setBankCardNo(technicianShow.getBankCardNo());
            technician.setReference(technicianShow.getReference());
            technician.setBankAddress(technicianShow.getBankAddress());
            technician.setFilmLevel(technicianShow.getFilmLevel());
            technician.setFilmWorkingSeniority(technicianShow.getFilmWorkingSeniority());
            technician.setCarCoverLevel(technicianShow.getCarCoverLevel());
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
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 技师抢单
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/v2/order/take", method = RequestMethod.POST)
    public JsonResult takeUpOrder(@RequestParam("orderId") int orderId,
                                  HttpServletRequest request) {
        try{
            Technician technician = (Technician) request.getAttribute("user");
            if(technician == null){
                return new JsonResult(false, "请重新登陆");
            }
            Order order = orderService.get(orderId);
            if (order == null) {
                return new JsonResult(false,  "没有这个订单");
            } else if (technician.getStatus() != Technician.Status.VERIFIED) {
                return new JsonResult(false, "你没有通过认证, 不能抢单");
            } else if (order.getStatus() == Order.Status.CANCELED) {
                return new JsonResult(false, "订单已取消");
            } else if (order.getStatus() != Order.Status.NEWLY_CREATED) {
                return new JsonResult(false, "已有人接单");
            }
            //技能判断待定
            order.setMainTechId(technician.getId());
            order.setTakenTime(new Date());
            order.setStatus(Order.Status.TAKEN_UP);
            orderService.save(order);
            publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.TAKEN));
            OrderView orderShow = orderViewService.findById(orderId);
            return new JsonResult(true, orderShow);
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 查询订单详情
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/v2/order/{orderId:\\d+}", method = RequestMethod.GET)
    public JsonResult getOrder(@PathVariable("orderId") int orderId,
                               HttpServletRequest request) {

        Order order = orderService.get(orderId);

        if (order == null) {
            return new JsonResult(false,  "没有这个订单");
        }


        OrderView orderView = orderViewService.findById(orderId);
        Technician technician = (Technician) request.getAttribute("user");
        if(technician == null){
            return new JsonResult(false, "登陆过期");
        }

        orderView.setComment(commentService.getByOrderIdAndTechId(orderId, order.getMainTechId()));
        List<WorkDetailShow> workDetailShowList = workDetailService.getByOrderId(orderId);
        Map<Integer, String> projectMap = constructionProjectService.getProject();
        Map<Integer, String> positionMap = constructionProjectService.getPosition();

        if(workDetailShowList != null && workDetailShowList.size()>0) {
            List<OrderConstructionShow> constructionShowList = new ArrayList<>();
            for (WorkDetailShow workDetailShow : workDetailShowList) {
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
            orderView.setOrderConstructionShow(constructionShowList);
            List<ConstructionWasteShow> constructionWasteShows = constructionWasteService.getByOrderId(orderId);

            for(ConstructionWasteShow constructionWasteShow: constructionWasteShows){
                constructionWasteShow.setProjectName(projectMap.get(constructionWasteShow.getProject()));
                constructionWasteShow.setPostitionName(positionMap.get(constructionWasteShow.getPosition()));
            }
            orderView.setConstructionWasteShows(constructionWasteShows);
        }
            if (order.getStatusCode() <= Order.Status.NEWLY_CREATED.getStatusCode()) {
                return new JsonResult(true, orderView);
            } else if (order.getStatusCode() > Order.Status.NEWLY_CREATED.getStatusCode() && order.getStatusCode() < Order.Status.FINISHED.getStatusCode()) {
                if (order.getMainTechId() != technician.getId()) {
                    return new JsonResult(false, "订单已被其他技师抢单，无法查看别的技师订单信息");
                }
            } else if (order.getStatusCode() >= Order.Status.FINISHED.getStatusCode()) {
                List<WorkDetail> workDetails = workDetailService.findByOrderId(orderId);
                if (workDetails != null) {
                    for (WorkDetail workDetail : workDetails) {
                        if (workDetail.getTechId() == technician.getId()) {
                            return new JsonResult(true, orderView);
                        }
                    }
                }
            }
        return new JsonResult(true, orderView);


    }


    /**
     * 查询本人订单
     * @param status 1 所有订单  2 未完成  3 已完成
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/v2/order", method = RequestMethod.GET)
    public JsonResult getOrders(
            @RequestParam(value = "status", defaultValue = "1") int status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            HttpServletRequest request) {
        try{
            Technician technician = (Technician) request.getAttribute("user");
            if(technician == null){
                return new JsonResult(false, "登陆过期");
            }
            Page<OrderView> orders;
            orders = orderViewService.find(technician.getId(), status, page, pageSize);
            return new JsonResult(true, orders);
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 查询作为合作技师订单
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/v2/partner/order", method = RequestMethod.GET)
    public JsonResult getOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            HttpServletRequest request) {
        try{
            Technician technician = (Technician) request.getAttribute("user");
            if(technician == null){
                return new JsonResult(false, "登陆过期");
            }
            Page<OrderPartnerView> orders;
            orders = orderViewService.find(technician.getId(), page, pageSize);
            return new JsonResult(true, orders);
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 技师取消订单
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/v2/order/{orderId:\\d+}/cancel", method = RequestMethod.PUT)
    public JsonResult cancel(@PathVariable("orderId") int orderId,
                             HttpServletRequest request) {

        try{
            Technician tech = (Technician) request.getAttribute("user");
            Order order = orderService.get(orderId);
            if (order == null) return new JsonResult(false,   "没有此订单");
            if (order.getMainTechId() != tech.getId()) return new JsonResult(false,   "只有接单人可以进行弃单操作");

            if(order.getStatusCode() >= Order.Status.IN_PROGRESS.getStatusCode() ){
                order.setReassignmentStatus(1);
                orderService.save(order);
                return new JsonResult(true,   "订单进入工作状态，已发送申请改派");
            }else {
                order.setStatusCode(Order.Status.NEWLY_CREATED.getStatusCode());
                order.setMainTechId(0);
                orderService.save(order);
                publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.CREATED));
                return new JsonResult(true, "订单已放弃，已重新释放");
            }
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }






    /**
     * 订单进入施工环节中
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/order/start", method = RequestMethod.POST)
    public JsonResult start(@RequestParam("orderId") int orderId,
                            HttpServletRequest request)throws Exception{

        try{
            Technician tech = (Technician) request.getAttribute("user");
            if(tech == null){
                return new JsonResult(false, "登陆过期");
            }
            Order order = orderService.get(orderId);
            if (order == null || (tech.getId() != order.getMainTechId())) {
                return new JsonResult(false, "你没有这个订单");
            }
            if (order.getStatus() == Order.Status.CANCELED) {
                return new JsonResult(false,  "订单已取消");
            }
            if (order.getStatusCode() >= Order.Status.FINISHED.getStatusCode()) {
                return new JsonResult(false, "订单已结束");
            }

            order.setStatus(Order.Status.IN_PROGRESS); // 订单状态进入IN_PROGRESS状态; 订单所有技师完成工作时,订单结束
            orderService.save(order);
            OrderView orderShow = orderViewService.findById(orderId);
            return new JsonResult(true, orderShow);
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 订单签到
     * @param request
     * @param positionLon
     * @param positionLat
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/v2/order/signIn", method = RequestMethod.POST)
    public JsonResult sign(@RequestParam("positionLon") String positionLon,
                           @RequestParam("positionLat") String positionLat,
                           @RequestParam("orderId") int orderId,
                           HttpServletRequest request)throws Exception {

        try{
            Technician tech = (Technician) request.getAttribute("user");
            if(tech == null){
                return new JsonResult(false, "登陆过期");
            }
            Order order = orderService.get(orderId);

            if (order == null || order.getMainTechId() !=tech.getId() ) {
                return new JsonResult(false,  "没有这个订单");
            }
            if (order.getStatus() == Order.Status.CANCELED) {
                return new JsonResult(false, "订单已取消");
            }
            if (order.getStatus() != Order.Status.IN_PROGRESS) {
                return new JsonResult(false, "订单还未开始或已结束");
            }
            if (order.getSignTime() != null) {
                return new JsonResult(false, "你已签到, 请不要重复操作");
            }
            if (order.getStatusCode() >= Order.Status.SIGNED_IN.getStatusCode()) {
                return new JsonResult(false, "你已签到, 请不要重复操作");
            }
            order.setStatus(Order.Status.SIGNED_IN);
            order.setSignTime(new Date());
            orderService.save(order);
            return new JsonResult(true,"签到成功");

        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }

    }




//    /**
//     * 开始工作
//     * @param request
//     * @param orderId
//     * @return
//     */
//    @RequestMapping(value = "/v2/order/working", method = RequestMethod.PUT)
//    public JsonResult construction(@RequestParam("orderId") int orderId,
//                                   HttpServletRequest request){
//
//        try{
//        Technician t = (Technician) request.getAttribute("user");
//        Order order = orderService.get(orderId);
//        if (order == null || (t.getId() != order.getMainTechId())) {
//            return new JsonResult(false, "你没有这个订单");
//        }
//        if (order.getStatus() == Order.Status.CANCELED) {
//            return new JsonResult(false,  "订单已取消");
//        }
//        if (order.getStatusCode() != Order.Status.SIGNED_IN.getStatusCode()) {
//            return new JsonResult(false, "签到以后才能施工");
//        }
//
//        if (order.getStatusCode() >= Order.Status.FINISHED.getStatusCode()) {
//            return new JsonResult(false, "订单已结束");
//        }
//
//        order.setStatus(Order.Status.AT_WORK); // 订单状态进入IN_PROGRESS状态; 订单所有技师完成工作时,订单结束
//        order.setStartTime(new Date());
//        orderService.save(order);
//
//        OrderShow orderShow = orderService.getByOrderId(orderId);
//        return new JsonResult(true, orderShow);
//
//        }catch (Exception e){
//            return new JsonResult(false, e.getMessage());
//        }
//    }




    /**
     * 上传照片
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/v2/uploadPhoto", method = RequestMethod.POST)
    public JsonResult upload(@RequestParam("file")MultipartFile file) {

        try{

            if (file.isEmpty()) {
                return new JsonResult(false, "没有上传文件");
            }
            String path = "/uploads/order";
            File dir = new File(new File(uploadPath).getCanonicalPath() + path);
            if (!dir.exists()) dir.mkdirs();
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
            String filename = "c-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
                    + "-" + VerifyCode.generateVerifyCode(8) + extension;
            file.transferTo(new File(dir.getAbsoluteFile() + File.separator + filename));
            return new JsonResult(true,  path + "/" + filename);

        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 通过订单编号获取施工项目及对应的施工部位
     * @param request
     * @param orderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/v2/order/project/position/{orderId}", method = RequestMethod.GET)
    public JsonResult getProject(HttpServletRequest request,
                                 @PathVariable("orderId") int orderId) {

        try{
            Technician tech = (Technician) request.getAttribute("user");
            if(tech == null){
                return new JsonResult(false, "登陆过期");
            }
            Order order = orderService.get(orderId);

            if (order == null || order.getMainTechId() !=tech.getId() ) {
                return new JsonResult(false,  "没有这个订单");
            }
            return new JsonResult(true, orderService.getProject(orderId));

        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 通过订单编号获取施工项目及对应的施工部位
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/v2/order/project/position", method = RequestMethod.GET)
    public JsonResult getAllProject(HttpServletRequest request) throws IOException {
        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }

        return new JsonResult(true, orderService.getAllProject());
    }



    /**
     * 提供施工前图片
     * @param request
     * @param orderId
     * @param urls
     * @return
     */
    @RequestMapping(value = "/v2/order/beforePhoto", method = RequestMethod.PUT)
    public JsonResult uploadBeforePhoto(HttpServletRequest request,
                                        @RequestParam("orderId") int orderId,
                                        @RequestParam("urls") String urls){

        try {


            Technician tech = (Technician) request.getAttribute("user");
            if(tech == null){
                return new JsonResult(false, "登陆过期");
            }
            Order order = orderService.get(orderId);

            if (order == null || order.getMainTechId() !=tech.getId() ) {
                return new JsonResult(false,  "没有这个订单");
            }
            if (!Pattern.matches("^([^,\\s]+)(,[^,\\s]+)*$", urls)) {
                return new JsonResult(false,  "图片地址格式错误, 请查阅urls参数说明");
            }
            if (urls.split(",").length > 9) {
                return new JsonResult(false,  "图片数量超出限制, 最多9张");
            }

            if (order.getStatusCode() != Order.Status.SIGNED_IN.getStatusCode()) {
                return new JsonResult(false, "签到以后才能施工");
            }

            if (order.getSignTime() == null) {
                return new JsonResult(false, "签到前不可上传照片");
            }
            if (order.getEndTime() != null) {
                return new JsonResult(false, "你已完成施工,不可再次上传照片");
            }
            order.setStatus(Order.Status.AT_WORK); // 进入工作状态
            order.setStartTime(new Date());
            order.setBeforePhotos(urls);
            orderService.save(order);
            return new JsonResult(true,"上传施工前照片成功");

        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 完成工作
     * @param constructionShow
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/v2/order/finish", method = RequestMethod.PUT)
    public JsonResult finish(@RequestBody ConstructionShow constructionShow,
                             HttpServletRequest request)  {
        try{

            if (!Pattern.matches("^([^,\\s]+)(,[^,\\s]+){2,8}$", constructionShow.getAfterPhotos())) {
                return new JsonResult(false, "afterPhotos参数格式错误, 施工完成后照片应至少3张, 至多9张");
            }

            Technician tech = (Technician) request.getAttribute("user");
            if(tech == null){
                return new JsonResult(false, "登陆过期");
            }
            Order order = orderService.get(constructionShow.getOrderId());
            if (order == null || order.getMainTechId() !=tech.getId()) {
                return new JsonResult(false, "没有这个订单");
            }
            if (order.getStatus() != Order.Status.AT_WORK) {
                return new JsonResult(false,  "订单不在工作中，无法完成订单");
            }
            if (order.getStatus() == Order.Status.FINISHED) {
                return new JsonResult(false,  "订单已经提交，不能再次提交");
            }
            if (order.getBeforePhotos() == null || "".equals(order.getBeforePhotos())) {
                return new JsonResult(false,  "没有上传施工前照片");
            }


            order.setStatus(Order.Status.FINISHED);
            order.setFinishTime(new Date());
            order.setEndTime(new Date());
            order.setAfterPhotos(constructionShow.getAfterPhotos());
            orderService.save(order, constructionShow);

          //  Order order1 = orderService.get(constructionShow.getOrderId());

            publisher.publishEvent(new OrderEventListener.OrderEvent(order, Event.Action.FINISHED));


            if(order.getProductStatus() == 1){
                workDetailService.balance(order.getId());
            }
            //合作技师施工部位推送

            return new JsonResult(true, "施工完成");

        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }

    public OrderShow get(OrderShow orderShow){

        List<WorkDetailShow> workDetailShowList = workDetailService.getByOrderId(orderShow.getId());
        Map<Integer, String> projectMap = constructionProjectService.getProject();
        Map<Integer, String> positionMap = constructionProjectService.getPosition();

        List<OrderConstructionShow> constructionShowList = new ArrayList<>();
        for(WorkDetailShow workDetailShow: workDetailShowList) {
            OrderConstructionShow orderConstructionShow = new OrderConstructionShow();
            List<ProjectPositionShow> projectPositionShows = new ArrayList<>();
            orderConstructionShow.setTechId(workDetailShow.getTechId());
            orderConstructionShow.setTechName(workDetailShow.getTechName());
            orderConstructionShow.setIsMainTech(workDetailShow.getTechId() == orderShow.getTechId()?1:0);
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

        return orderShow;
    }


    /**
     * 技师修改银行卡号
     * @param request
     * @param bank
     * @param bankCardNo
     * @return
     */
    @RequestMapping(value = "/v2/changeBankCard", method = RequestMethod.PUT)
    public JsonResult changeBankCard(HttpServletRequest request,
                                     @RequestParam("bank") String bank,
                                     @RequestParam("bankAddress") String bankAddress,
                                     @RequestParam("bankCardNo") String bankCardNo) {
        Technician technician = (Technician) request.getAttribute("user");
        technician.setBank(bank);
        technician.setBankAddress(bankAddress);
        technician.setBankCardNo(bankCardNo);
        technicianService.save(technician);
        return new JsonResult(true);
    }


    /**
     * 获取可抢订单列表
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/v2/order/listNew", method = RequestMethod.GET)
    public JsonResult getNewCreateOrder(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        return new JsonResult(true, new JsonPage<>(orderViewService.findByStatusCode(0, page, pageSize)));
    }

    /**
     * 查询提现申请列表
     * @param techId
     * @param techName
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/v2/cash/apply", method = RequestMethod.GET)
    public JsonResult getListApply(HttpServletRequest request,
            @RequestParam(value = "techId", defaultValue = "1") int techId,
            @RequestParam(value = "techName", defaultValue = "1") String techName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize){
        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }

        return new JsonResult(true, new JsonPage<>(techCashApplyService.find(techName, techId, page, pageSize)));
    }

    /**
     * 新增提现申请
     * @param techCashApplyShow
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/v2/cash/apply", method = RequestMethod.POST)
    public JsonResult addApply(@RequestBody TechCashApplyShow techCashApplyShow,
                                     HttpServletRequest request)throws Exception {
        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }

        TechCashApply techCashApply = new TechCashApply(techCashApplyShow);
        techCashApply.setState(0);
        return new JsonResult(true, techCashApplyService.save(techCashApply));
    }

    /**
     * 修改提现申请
     * @param techCashApplyShow
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/v2/cash/apply/{id:\\d+}", method = RequestMethod.PUT)
    public JsonResult updateApply(@PathVariable("id") int id,@RequestBody TechCashApplyShow techCashApplyShow,
                          HttpServletRequest request)throws Exception {
        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }
        TechCashApply techCashApply = techCashApplyService.findById(id);
        if(techCashApply == null){
            return new JsonResult(false, "申请单不存在");
        }
        techCashApply.setApplyDate(techCashApplyShow.getApplyDate() == null ? techCashApply.getApplyDate() : techCashApplyShow.getApplyDate());
        techCashApply.setApplyMoney(techCashApplyShow.getApplyMoney() == null ? techCashApply.getApplyMoney() : techCashApplyShow.getApplyMoney());
        techCashApply.setTechId(techCashApplyShow.getTechId() == null ? techCashApply.getTechId() : techCashApplyShow.getTechId());
        techCashApply.setOrderId(techCashApplyShow.getOrderId() == null ? techCashApply.getOrderId() : techCashApplyShow.getOrderId());
        techCashApply.setPayDate(techCashApplyShow.getPayDate() == null ? techCashApply.getPayDate() : techCashApplyShow.getPayDate());
        techCashApply.setPayment(techCashApplyShow.getPayment() == null ? techCashApply.getPayment() : techCashApplyShow.getPayment());
        techCashApply.setNotPay(techCashApplyShow.getNotPay() == null ? techCashApply.getNotPay() : techCashApplyShow.getNotPay());
        techCashApply.setState(techCashApplyShow.getState() == null ? techCashApply.getState() : techCashApplyShow.getState());
        return new JsonResult(true, techCashApplyService.save(techCashApply));
    }

    /**
     * 删除申请单
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/v2/cash/apply/{id:\\d+}", method = RequestMethod.DELETE)
    public JsonResult updateApply(@PathVariable("id") int id,
                                  HttpServletRequest request)throws Exception {
        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }
        TechCashApply techCashApply = techCashApplyService.findById(id);
        if(techCashApply == null){
            return new JsonResult(false, "申请单不存在");
        }
        if(techCashApply.getState() > 0){
            return new JsonResult(false, "已提过现，不能删除");
        }
        techCashApplyService.deleteById(id);
        return new JsonResult(true, "删除成功");
    }

    /**
     * 执行支付
     * @param id
     * @param techCashApplyShow  存放提现金额（payment）和提现时间(payDate)
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/v2/cash/apply/{id:\\d+}/to", method = RequestMethod.PUT)
    public JsonResult toApply(@PathVariable("id") int id,@RequestBody TechCashApplyShow techCashApplyShow,
                               HttpServletRequest request)throws Exception {
        Technician tech = (Technician) request.getAttribute("user");
        if(tech == null){
            return new JsonResult(false, "登陆过期");
        }
        TechCashApply techCashApply = techCashApplyService.findById(id);
        if(techCashApply == null){
            return new JsonResult(false, "申请单不存在");
        }
        if(techCashApply.getState() == 2){
            return new JsonResult(false, "已全部提现，无法支付");
        }
        if(techCashApplyShow.getPayment().compareTo(techCashApply.getNotPay()) == 1){
            return new JsonResult(false, "金额超出未提现金额，无法支付");
        }
        BigDecimal left = techCashApply.getNotPay().subtract(techCashApplyShow.getPayment());
        techCashApply.setNotPay(left);
        if(left.compareTo(new BigDecimal(0)) == 0){
            techCashApply.setState(2);
        }else{
            techCashApply.setState(1);
        }
        techCashApply.setPayment(techCashApply.getPayment().add(techCashApplyShow.getPayment()));
        techCashApply.setPayDate(techCashApplyShow.getPayDate());
        return new JsonResult(true, techCashApplyService.save(techCashApply));
    }
}
