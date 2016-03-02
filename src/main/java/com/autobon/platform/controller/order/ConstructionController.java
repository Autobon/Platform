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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dave on 16/3/1.
 */
@RestController
@RequestMapping("/api/mobile/technician/construct")
public class ConstructionController {
    @Autowired OrderService orderService;
    @Autowired ConstructionService constructionService;

    @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
    public JsonMessage uploadPhoto(HttpServletRequest request,
            @RequestParam("file")     MultipartFile file,
            @RequestParam("no")       int no, // 图片序号
            @RequestParam("orderId")  int orderId,
            @RequestParam("isBefore") boolean isBefore) throws IOException {
        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.findOrder(orderId);
        if (order == null || (tech.getId() != order.getMainTechId() && tech.getId() != order.getSecondTechId())) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "你没有这个订单");
        } else if (order.getStatus() != Order.Status.IN_PROGRESS) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "非施工中订单, 不允许上传照片");
        }

        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");
        List<Construction> list = constructionService.findByOrderIdAndTechnicianId(orderId, tech.getId());
        if (list.size() < 1) return new JsonMessage(false, "SYSTEM_CORRUPT", "系统没有你的施工单");
        Construction construction = list.get(0);

        String path = "/uploads/order";
        File dir = new File(request.getServletContext().getRealPath(path));
        if (!dir.exists()) dir.mkdirs();
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = "" + orderId + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
                + "-" + no + extension;

        if (isBefore) {
            if (no < 1 || no > 3) return new JsonMessage(false, "ILLEGAL_PARAM", "施工前照片序号只能为1,2,3");
            filename = "b" + filename;

            String photos = construction.getBeforePhotos();
            List<String> urlList = (photos == null || "".equals(photos)) ?
                    Arrays.asList("", "", "") : Arrays.asList(photos.split(","));
            urlList.set(no - 1, path + "/" + filename);
            construction.setBeforePhotos(urlList.stream().collect(Collectors.joining(",")));
        } else {
            if (no < 1 || no > 6) return new JsonMessage(false, "ILLEGAL_PARAM", "施工后照片序号只能为1-6");
            filename = "a" + filename;

            String photos = construction.getAfterPhotos();
            List<String> urlList = (photos == null || "".equals(photos)) ?
                        Arrays.asList("", "", "", "", "", "") : Arrays.asList(photos.split(","));
            urlList.set(no - 1, path + "/" + filename);
            construction.setAfterPhotos(urlList.stream().collect(Collectors.joining(",")));
        }
        file.transferTo(new File(dir.getAbsoluteFile() + File.separator + filename));
        constructionService.save(construction);

        return new JsonMessage(true, "", "", path + "/" + filename);
    }

    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public JsonMessage finish(HttpServletRequest request,
            @RequestParam("orderId")   int orderId,
            @RequestParam("carSeat")   int carSeat,
            @RequestParam("workItems") String workItems) {
        Technician tech = (Technician) request.getAttribute("user");
        Order order = orderService.findOrder(orderId);
        if (order == null || (tech.getId() != order.getMainTechId() && tech.getId() != order.getSecondTechId())) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "你没有这个订单");
        } else if (order.getStatus() != Order.Status.IN_PROGRESS) {
            return new JsonMessage(false, "ILLEGAL_OPERATION", "非施工中订单, 不允许上传照片");
        }

        return null;
    }
}
