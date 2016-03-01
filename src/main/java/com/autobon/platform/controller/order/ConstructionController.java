package com.autobon.platform.controller.order;

import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.service.ConstructionService;
import com.autobon.order.service.OrderService;
import com.autobon.shared.JsonMessage;
import com.autobon.technician.entity.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dave on 16/3/1.
 */
@RestController
@RequestMapping("/api/mobile/technician/construct")
public class ConstructionController {
    @Autowired
    OrderService orderService;
    @Autowired
    ConstructionService constructionService;

    @RequestMapping(value = "/construction/uploadBeforePhotos", method = RequestMethod.POST)
    public JsonMessage uploadBeforePhotos(HttpServletRequest request,
              @RequestParam("orderId") int orderId,
              @RequestParam(value = "file", required = false) MultipartFile[] files,
              @RequestParam(value = "append", defaultValue = "true") boolean append ) throws IOException {
        return _savePhotos(files, orderId, request, true, append);
    }

    @RequestMapping(value = "/construction/uploadAfterPhotos", method = RequestMethod.POST)
    public JsonMessage uploadAfterPhotos(HttpServletRequest request,
             @RequestParam("orderId") int orderId,
             @RequestParam(value = "file", required = false) MultipartFile[] files,
             @RequestParam(value = "append", defaultValue = "true") boolean append ) throws IOException {
        return _savePhotos(files, orderId, request, false, append);
    }


    private JsonMessage _savePhotos(MultipartFile[] files, int orderId, HttpServletRequest request,
                boolean isBeforePhoto, boolean append) throws IOException {
        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.findOrder(orderId);
        if (order == null || (tech.getId() != order.getMainTechId() && tech.getId() != order.getSecondTechId())) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "你没有这个订单");
        } else if (order.getEnumStatus() != Order.EnumStatus.IN_PROGRESS) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "非施工中订单, 不允许上传照片");
        } else if (files.length > 3) {
            return new JsonMessage(false, "ILLEGAL_PARAM", "施工前照片只能上传1至3张");
        }

        List<Construction> list = constructionService.findByOrderIdAndTechnicianId(orderId, tech.getId());
        if (list.size() < 1) return new JsonMessage(false, "SYSTEM_CORRUPT", "系统没有你的施工单");
        Construction construction = list.get(0);

        if (files == null || files.length == 0) {
            if (!append) { // 清空上传文件操作
                construction.setBeforePhotos("");
                constructionService.save(construction);
            } else {
                return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");
            }
        }

        String path = "/uploads/order";
        File dir = new File(request.getServletContext().getRealPath(path));
        if (!dir.exists()) dir.mkdirs();
        ArrayList<String> urls = new ArrayList<>();
        if (append) urls.addAll(Arrays.asList(construction.getBeforePhotos().split(",")));
        for (int i = 0; i < files.length; i++) {
            if (files[i].isEmpty()) continue;
            String originalFilename = files[i].getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
            String filename = "" + orderId + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
                    + "-" + i + extension;
            if (isBeforePhoto) filename = "b" + filename;
            else filename = "a" + filename;
            files[i].transferTo(new File(dir.getAbsoluteFile() + File.separator + filename));
            urls.add(path + "/" + filename);
        }

        String urlString = urls.stream().collect(Collectors.joining(","));
        if (isBeforePhoto) construction.setBeforePhotos(urlString);
        else construction.setAfterPhotos(urlString);
        constructionService.save(construction);
        return new JsonMessage(true, "", "", urls);
    }
}
