package com.autobon.platform.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.shared.VerifyCode;
import com.autobon.study.entity.StudyData;
import com.autobon.study.service.StudyDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * Created by dave on 16/3/1.
 */
@RestController("adminStudyController")
@RequestMapping("/api/web/admin/study")
public class StudyController {
    @Autowired
    StudyDataService studyDataService;
    @Value("${com.autobon.uploadPath}") String uploadPath;
    @Value("${com.autobon.gm-path}") String gmPath;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonMessage upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");

        String path = "/uploads/study";
        File dir = new File(new File(uploadPath).getCanonicalPath() + path);
        if (!dir.exists()) dir.mkdirs();

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + VerifyCode.generateVerifyCode(6) + extension;

        InputStream in;
        if (file == null || file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有选择上传文件");
        in = file.getInputStream();

//        ConvertCmd cmd = new ConvertCmd(true);
//        cmd.setSearchPath(gmPath);
//        cmd.setInputProvider(new Pipe(in, null));
//        IMOperation operation = new IMOperation();
//        operation.addImage("-");
//        operation.resize(1200, 1200, ">");
//        operation.addImage(dir.getAbsolutePath() + File.separator + filename);
//        cmd.run(operation);
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileName", filename);
        jsonObject.put("fileLength", String.valueOf(file.getSize()));
        jsonObject.put("realName", originalName);
        jsonObject.put("path", path + "/" + filename);
        return new JsonMessage(true, "", "", path + "/" + filename, jsonObject);
    }

    /**
     *
     * 查询资料列表
     *
     * @param fileName 资料名称
     * @param type 资料类型
     * @param page 当前页
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search(
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        return new JsonMessage(true, "", "", new JsonPage<>(studyDataService.find(
                fileName, type, page, pageSize)));
    }

    /**
     *
     * 添加资料
     *
     * @param fileName 资料名称
     * @param type 资料类型
     * @param fileLength 资料大小
     * @param path 位置
     * @param remark 备注
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonMessage createCoop(
            @RequestParam("fileName") String fileName,
            @RequestParam("type") Integer type,
            @RequestParam("fileLength") Integer fileLength,
            @RequestParam("path") String path,
            @RequestParam("remark") String remark) {

        if(studyDataService.getByName(fileName) != null){
            return  new JsonMessage(false,"资料名称重复","资料名称重复");
        }
        StudyData studyData = new StudyData(type, fileName, fileLength, path, remark);
        StudyData res = studyDataService.save(studyData);
        if(res == null){
            return  new JsonMessage(false,"添加失败","添加失败");
        }
        return  new JsonMessage(true,"","添加成功", res);
    }

    /**
     *
     * 修改资料
     *
     * @param id
     * @param fileName 文件名称
     * @param type 文件类型
     * @param fileLength 文件大小
     * @param path 文件路径
     * @param remark 备注
     * @return
     */
    @RequestMapping(value = "/{id:[\\d]+}", method = RequestMethod.POST)
    public JsonMessage update(
            @PathVariable("id") int id,
            @RequestParam("fileName") String fileName,
            @RequestParam("type") Integer type,
            @RequestParam("fileLength") Integer fileLength,
            @RequestParam("path") String path,
            @RequestParam("remark") String remark) {

        StudyData studyData = studyDataService.get(id);
        if(studyData == null){
            return  new JsonMessage(false,"资料不存在","资料不存在");
        }
        studyData.setFileName(fileName);
        studyData.setType(type);
        studyData.setFileLength(fileLength);
        studyData.setPath(path);
        studyData.setRemark(remark);
        StudyData res = studyDataService.save(studyData);
        if(res == null){
            return  new JsonMessage(false,"修改失败","修改失败");
        }
        return  new JsonMessage(true,"","添加成功", res);
    }

    /**
     *
     * 根据ID查询学习资料
     *
     * @param id 学习资料ID
     * @return
     */
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
    public JsonMessage getData(@PathVariable(value = "id") int id) {
        StudyData studyData = studyDataService.get(id);
        return new JsonMessage(true, "", "", studyData);
    }

    /**
     *
     * 下载文档
     *
     * @param path 文档路径
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(@RequestParam("path") String path, HttpServletResponse response) throws Exception{
        try {
            // path是指欲下载的文件的路径。
            File file = new File(new File(uploadPath).getCanonicalPath() + path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            String newPath = file.getPath();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(newPath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.setHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
