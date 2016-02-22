package com.autobon.platform.controller.technician;

import com.autobon.platform.utils.JsonMessage;
import com.autobon.platform.utils.RedisCache;
import com.autobon.platform.utils.SmsSender;
import com.autobon.platform.utils.VerifyCode;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired RedisCache redisCache;
    @Autowired SmsSender smsSender;
    @Value("${com.autobon.env:PROD}") String env;
    @Value("${com.autobon.gm-path}") String gmPath;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public JsonMessage handleUploadException(MaxUploadSizeExceededException ex) {
        return new JsonMessage(false, "UPLOAD_SIZE_EXCEED", "上传文件大小不能超过2MB");
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
        if (!verifySms.equals(new String(redisCache.get(("verifySms:" + phone).getBytes())))) {
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
        } else if (!verifySms.equals(new String(redisCache.get(("verifySms:" + phone).getBytes())))) {
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
    public JsonMessage changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {
        JsonMessage msg = new JsonMessage(true);
        if (newPassword.length() < 6) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "密码至少6位");
        } else {
            Technician technician = (Technician) SecurityContextHolder.getContext().getAuthentication().getDetails();
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
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public JsonMessage uploadAvatar(HttpServletRequest request,
        @RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");

        JsonMessage msg = new JsonMessage(true);
        String path = "/uploads/technician/avatar";
        File dir = new File(request.getServletContext().getRealPath(path));
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;

        if (!dir.exists()) dir.mkdirs();
        try (InputStream fis = file.getInputStream()) {
            ConvertCmd cmd = new ConvertCmd(true);
            cmd.setSearchPath(gmPath);
            cmd.setInputProvider(new Pipe(fis, null));
            IMOperation operation = new IMOperation();
            operation.addImage("-");
            operation.gravity("center").thumbnail(200, 200, "^").extent(200, 200);
            operation.addImage(dir.getAbsolutePath() + File.separator + filename);
            cmd.run(operation);
        }
        Technician technician = (Technician) SecurityContextHolder.getContext().getAuthentication().getDetails();
        technician.setAvatar(path + "/" + filename);
        technicianService.save(technician);
        msg.setData(technician.getAvatar());
        return msg;
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
        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");

        JsonMessage msg = new JsonMessage(true);
        String path = "/uploads/technician/idPhoto";
        File dir = new File(request.getServletContext().getRealPath(path));
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;

        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        Technician technician = (Technician) SecurityContextHolder.getContext().getAuthentication().getDetails();
        technician.setIdPhoto(path + "/" + filename);
        technicianService.save(technician);
        msg.setData(technician.getIdPhoto());
        return msg;
    }
}
