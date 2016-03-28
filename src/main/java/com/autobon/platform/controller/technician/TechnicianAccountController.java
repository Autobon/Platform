package com.autobon.platform.controller.technician;

import com.autobon.shared.JsonMessage;
import com.autobon.shared.RedisCache;
import com.autobon.shared.SmsSender;
import com.autobon.shared.VerifyCode;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.DetailedTechnicianService;
import com.autobon.technician.service.TechnicianService;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
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
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by dave on 16/2/18.
 */
@RestController
@RequestMapping("/api/mobile/technician")
public class TechnicianAccountController {
    @Autowired TechnicianService technicianService;
    @Autowired DetailedTechnicianService detailedTechnicianService;
    @Autowired RedisCache redisCache;
    @Autowired SmsSender smsSender;
    @Autowired MultipartResolver resolver;
    @Value("${com.autobon.env:PROD}") String env;
    @Value("${com.autobon.gm-path}") String gmPath;
    @Value("${com.autobon.uploadPath}") String uploadPath;

    /**
     * 获取用户信息
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage getTechnicianInfo(HttpServletRequest request) {
        Technician technician = (Technician) request.getAttribute("user");
        return new JsonMessage(true, "", "", detailedTechnicianService.get(technician.getId()));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonMessage register(
            @RequestParam("phone")     String phone,
            @RequestParam("password")  String password,
            @RequestParam("verifySms") String verifySms) {

        JsonMessage msg = new JsonMessage(true);
        ArrayList<String> messages = new ArrayList<>();

        if (!Pattern.matches("^\\d{11}$", phone)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("手机号格式错误");
        } else if (technicianService.getByPhone(phone) != null) {
            msg.setError("OCCUPIED_ID");
            messages.add("手机号已被注册");
        }

        if (password.length() < 6) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("密码至少6位");
        }
        String code = redisCache.get("verifySms:" + phone);
        if (!verifySms.equals(code)) {
            msg.setError("ILLEGAL_PARAM");
            messages.add("验证码错误");
        }

        if (messages.size() > 0) {
            msg.setResult(false);
            msg.setMessage(messages.stream().collect(Collectors.joining(",")));
        } else {
            Technician technician = new Technician();
            technician.setPhone(phone);
            technician.setPassword(Technician.encryptPassword(password));
            technicianService.save(technician);
            msg.setData(technician);
        }
        return msg;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonMessage login(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam("phone")    String phone,
            @RequestParam("password") String password) {

        JsonMessage msg = new JsonMessage(true);
        Technician technician = technicianService.getByPhone(phone);

        if (technician == null) {
            msg.setResult(false);
            msg.setError("NO_SUCH_USER");
            msg.setMessage("手机号未注册");
        } else if (!technician.getPassword().equals(Technician.encryptPassword(password))) {
            msg.setResult(false);
            msg.setError("PASSWORD_MISMATCH");
            msg.setMessage("密码错误");
        } else {
            response.addCookie(new Cookie("autoken", Technician.makeToken(technician.getId())));
            technician.setLastLoginAt(new Date());
            technician.setLastLoginIp(request.getRemoteAddr());
            technicianService.save(technician);
            msg.setData(technician);
        }
        return msg;
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public JsonMessage resetPassword(
            @RequestParam("phone")     String phone,
            @RequestParam("password")  String password,
            @RequestParam("verifySms") String verifySms) throws Exception {

        JsonMessage msg = new JsonMessage(true);
        Technician technician = technicianService.getByPhone(phone);
        if (technician == null) {
            msg.setResult(false);
            msg.setError("NO_SUCH_USER");
            msg.setMessage("手机号未注册");
        } else if (!verifySms.equals(redisCache.get("verifySms:" + phone))) {
            msg.setResult(false);
            msg.setError("ILLEGAL_PARAM");
            msg.setMessage("验证码错误");
        } else if (password.length() < 6) {
            msg.setResult(false);
            msg.setError("ILLEGAL_PARAM");
            msg.setMessage("密码至少6位");
        } else {
            technician.setPassword(Technician.encryptPassword(password));
            technicianService.save(technician);
        }
        return msg;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public JsonMessage changePassword(HttpServletRequest request,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {
        if (newPassword.length() < 6) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "密码至少6位");
        } else {
            Technician technician = (Technician) request.getAttribute("user");
            if (!technician.getPassword().equals(Technician.encryptPassword(oldPassword))) {
                return new JsonMessage(false, "ILLEGAL_PARAM", "原密码错误");
            }
            technician.setPassword(Technician.encryptPassword(newPassword));
            technicianService.save(technician);
        }
        return new JsonMessage(true);
    }

    /**
     * 上传头像
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public JsonMessage uploadAvatarForm(HttpServletRequest request) throws Exception {
        String path = "/uploads/technician/avatar";
        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
        if (!dir.exists()) dir.mkdirs();
        Technician technician = (Technician) request.getAttribute("user");
        String filename = technician.getId() + ".jpg";

        InputStream in;
        if (request.getContentLengthLong() >= 2*1024*1024) {
            throw new MaxUploadSizeExceededException(2*1024*1024);
        }
        if (resolver.isMultipart(request)) {
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            if (file == null || file.isEmpty()) return new JsonMessage(false, "没有选择上传文件");
            in = file.getInputStream();
        } else {
            String ctype = request.getHeader("content-type");
            if (!ctype.equals("image/jpeg") && !ctype.equals("image/png")) {
                return new JsonMessage(false, "没有上传文件，或非jpg、png类型文件");
            }
            if (request.getContentLengthLong() <= 0) {
                return new JsonMessage(false, "没有选择上传文件");
            }
            in = request.getInputStream();
        }

        ConvertCmd cmd = new ConvertCmd(true);
        cmd.setSearchPath(gmPath);
        cmd.setInputProvider(new Pipe(in, null));
        IMOperation operation = new IMOperation();
        operation.addImage("-");
        operation.gravity("center").thumbnail(200, 200, "^").extent(200, 200);
        operation.addImage(dir.getAbsolutePath() + File.separator + filename);
        cmd.run(operation);
        in.close();
        technician.setAvatar(path + "/" + filename);
        technicianService.save(technician);
        return new JsonMessage(true, "", "", technician.getAvatar());
    }

    /**
     * 更新用户的个推ID
     * @param pushId
     * @return
     */
    @RequestMapping(value = "/pushId", method = RequestMethod.POST)
    public JsonMessage savePushId(HttpServletRequest request,
            @RequestParam("pushId") String pushId) {
        Technician technician = (Technician) request.getAttribute("user");
        Technician oTech = technicianService.getByPushId(pushId);

        if (oTech != null) {
            if (technician.getId() == oTech.getId()) return new JsonMessage(true);
            else {
                oTech.setPushId(null);
                technicianService.save(oTech);
            }
        }

        technician.setPushId(pushId);
        technicianService.save(technician);
        return new JsonMessage(true);
    }

    /**
     * 上传身份证照片
     * @param request
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/idPhoto", method = RequestMethod.POST)
    public JsonMessage uploadIdPhoto(HttpServletRequest request,
        @RequestParam("file") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");

        JsonMessage msg = new JsonMessage(true);
        String path = "/uploads/technician/idPhoto";
        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + VerifyCode.generateRandomNumber(6) + extension;

        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        Technician technician = (Technician) request.getAttribute("user");
        technician.setIdPhoto(path + "/" + filename);
        technicianService.save(technician);
        msg.setData(technician.getIdPhoto());
        return msg;
    }
}
