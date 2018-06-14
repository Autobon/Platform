package com.autobon.platform.controller.app.merchandiser;

import com.autobon.merchandiser.entity.Merchandiser;
import com.autobon.merchandiser.service.MerchandiserCooperatorService;
import com.autobon.merchandiser.service.MerchandiserService;
import com.autobon.shared.JsonResult;
import com.autobon.shared.RedisCache;
import com.autobon.technician.entity.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by wh on 2018/6/1.
 */
@RestController
@RequestMapping("/api/mobile/merchandiser")
public class MerchandiserController {


    @Autowired
    MerchandiserService merchandiserService;
    @Autowired
    MerchandiserCooperatorService merchandiserCooperatorService;
    @Autowired
    RedisCache redisCache;
    @Autowired
    MultipartResolver resolver;

    @Value("${com.autobon.uploadPath}") String uploadPath;



    /**
     * 跟单员登陆
     * @param phone 手机号码
     * @param password 密码
     * @param request HTTP请求
     * @param response HTTP请求
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResult login(@RequestParam("phone")    String phone,
                            @RequestParam("password") String password,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        try {
            JsonResult jsonResult = new JsonResult(true);
            Merchandiser merchandiser = merchandiserService.findByPhone(phone);

            if (merchandiser == null) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("手机号未注册");
            } else if (!merchandiser.getPassword().equals(Merchandiser.encryptPassword(password))) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("密码错误");
            } else {
                response.addCookie(new Cookie("autoken", Merchandiser.makeToken(merchandiser.getId())));
                merchandiser.setLastLoginAt(new Date());
                merchandiser.setLastLoginIp(request.getRemoteAddr());
                merchandiserService.save(merchandiser);
                jsonResult.setMessage(merchandiser);
            }
            return jsonResult;
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }



    /**
     * 跟单员重置密码
     * @param phone 手机号码
     * @param password 密码
     * @param verifySms 验证码
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public JsonResult resetPassword(@RequestParam("phone")     String phone,
                                    @RequestParam("password")  String password,
                                    @RequestParam("verifySms") String verifySms) {

        try {
            JsonResult jsonResult = new JsonResult(true);
            Merchandiser merchandiser = merchandiserService.findByPhone(phone);
            if (merchandiser == null) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("手机号未注册");
            } else if (!verifySms.equals(redisCache.get("verifySms:" + phone))) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("验证码错误");
            } else if (password.length() < 6) {
                jsonResult.setStatus(false);
                jsonResult.setMessage("密码至少6位");
            } else {
                merchandiser.setPassword(Technician.encryptPassword(password));
                merchandiserService.save(merchandiser);
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
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public JsonResult changePassword(@RequestParam("oldPassword") String oldPassword,
                                     @RequestParam("newPassword") String newPassword,
                                     HttpServletRequest request) {
        try {
            Merchandiser merchandiser = (Merchandiser) request.getAttribute("user");
            if (merchandiser == null) {
                return new JsonResult(false, "登陆超时");
            }
            if (newPassword.length() < 6) {
                return new JsonResult(false, "密码至少6位");
            } else {
                if (!merchandiser.getPassword().equals(Merchandiser.encryptPassword(oldPassword))) {
                    return new JsonResult(false, "原密码错误");
                }
                merchandiser.setPassword(Technician.encryptPassword(newPassword));
                merchandiserService.save(merchandiser);
            }
            return new JsonResult(true, "修改密码成功");
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 查询自己信息
     * @param request HTTP请求
     * @return JsonResult 对象
     */
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public JsonResult getInfo(HttpServletRequest request) {
        try {
            Merchandiser merchandiser = (Merchandiser) request.getAttribute("user");
            if (merchandiser == null) {
                return new JsonResult(false, "登陆超时");
            }


            return new JsonResult(true, merchandiser);
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
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public JsonResult uploadAvatarForm(HttpServletRequest request) throws Exception {
        try{
            String path = "/uploads/merchandiser/avatar";
            File dir = new File(new File(uploadPath).getCanonicalPath() + path);
            if (!dir.exists()) dir.mkdirs();
            Merchandiser merchandiser = (Merchandiser) request.getAttribute("user");
            if (merchandiser == null) {
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
            merchandiser.setAvatar(path + "/" + filename);
            merchandiserService.save(merchandiser);
            ((MultipartHttpServletRequest) request).getFile("file").transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
            return new JsonResult(true, merchandiser.getAvatar());
        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }

    /**
     * 更新用户的个推ID
     * @param pushId
     * @return
     */
    @RequestMapping(value = "/pushId", method = RequestMethod.POST)
    public JsonResult savePushId(HttpServletRequest request,
                                 @RequestParam("pushId") String pushId) {

        try{
            Merchandiser merchandiser = (Merchandiser) request.getAttribute("user");
            Merchandiser oTech = merchandiserService.findByPushId(pushId);

            if (oTech != null) {
                if (merchandiser.getId() == oTech.getId()) return new JsonResult(true);
                else {
                    oTech.setPushId(null);
                    merchandiserService.save(oTech);
                }
            }
            merchandiser.setPushId(pushId);
            merchandiserService.save(merchandiser);
            return new JsonResult(true);

        }catch (Exception e){
            return new JsonResult(false, e.getMessage());
        }
    }


    /**
     * 查询自己信息
     * @param request HTTP请求
     * @return JsonResult 对象
     */
    @RequestMapping(value = "/cooperator", method = RequestMethod.GET)
    public JsonResult getCooperator(HttpServletRequest request) {

            Merchandiser merchandiser = (Merchandiser) request.getAttribute("user");
            if (merchandiser == null) {
                return new JsonResult(false, "登陆超时");
            }

            return new JsonResult(true, merchandiserCooperatorService.findByMerchandiserId(merchandiser.getId()));

    }




}
