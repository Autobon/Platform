package com.autobon.platform.controller.order;

import com.autobon.order.entity.Construction;
import com.autobon.order.entity.Order;
import com.autobon.order.repository.ConstructionRepository;
import com.autobon.order.service.OrderService;
import com.autobon.platform.utils.JsonMessage;
import com.autobon.platform.utils.VerifyCode;
import com.autobon.technician.entity.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by yuh on 2016/2/22.
 */
@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ConstructionRepository constructionRepository;

    @RequestMapping(value = "/mobile/order/orderList", method = RequestMethod.GET)
    public JsonMessage orderList() throws Exception{
        JsonMessage jsonMessage = new JsonMessage(true,"orderList");
        List<Order> orderList = orderService.getOrderList();
        jsonMessage.setData(orderList);
        return jsonMessage;

    }

    @RequestMapping(value = "/mobile/order/getLocation", method = RequestMethod.GET)
    public JsonMessage getLocation(
            @RequestParam("orderId") int orderId){
        JsonMessage jsonMessage = new JsonMessage(true,"location");
        Order order  = orderService.getLocation(orderId);

        jsonMessage.setData(order);
        return  jsonMessage;
    }

    @RequestMapping(value = "/mobile/construction/signIn", method = RequestMethod.POST)
    public JsonMessage signIn(
            @RequestParam("rtpositionLon") String rtpositionLon,
            @RequestParam("rtpositionLat") String rtpositionLat,
            @RequestParam("technicianId") int technicianId,
            @RequestParam("orderId") int orderId){
        JsonMessage jsonMessage = new JsonMessage(true,"signIn");
        Construction construction = new Construction();
        construction.setOrderId(orderId);
        construction.setTechnicianId(technicianId);
        construction.setRtpositionLon(rtpositionLon);
        construction.setRtpositionLat(rtpositionLat);
        construction.setSigninTime(new Date());
        construction = constructionRepository.save(construction);
        jsonMessage.setData(construction);
        return jsonMessage;
    }

    /**
     * 上传工作前后照片
     * @param request
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mobile/construction/uploadPic", method = RequestMethod.POST)
    public JsonMessage uploadIdPhoto(HttpServletRequest request,
                                     @RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) return new JsonMessage(false, "NO_UPLOAD_FILE", "没有上传文件");

        JsonMessage msg = new JsonMessage(true);
        String path = "/uploads/construction/pic";
        File dir = new File(request.getServletContext().getRealPath(path));
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (VerifyCode.generateRandomNumber(6)) + extension;

        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        String filePath = path + "/" + filename;
        msg.setData(filePath);
        return msg;
    }

    @RequestMapping(value = "/mobile/construction/saveBeforePic", method = RequestMethod.POST)
    public JsonMessage saveBeforePic(@RequestParam("constructionId") int constructionId,
                                     @RequestParam("filePaths") String ... filePaths){
        JsonMessage jsonMessage = new JsonMessage(true,"saveBeforePic");
        Construction construction = constructionRepository.findOne(constructionId);

        int fileLength = filePaths.length;
        if(fileLength<1 || fileLength>3){
            return  new JsonMessage(false,"图片数量有误");
        }else if(fileLength == 1){
            construction.setBeforePicA(filePaths[0]);
        }else if(fileLength == 2){
            construction.setBeforePicA(filePaths[0]);
            construction.setBeforePicB(filePaths[1]);
        }else if(fileLength == 3){
            construction.setBeforePicA(filePaths[0]);
            construction.setBeforePicB(filePaths[1]);
            construction.setBeforePicC(filePaths[2]);
        }
        constructionRepository.save(construction);
        return jsonMessage;
    }

    @RequestMapping(value = "/mobile/construction/saveAfterPic", method = RequestMethod.POST)
    public JsonMessage saveAfterPic(@RequestParam("constructionId") int constructionId,
                                    @RequestParam("filePaths") String ... filePaths){
        JsonMessage jsonMessage = new JsonMessage(true,"saveAfterPic");
        Construction construction = constructionRepository.findOne(constructionId);
        int fileLength = filePaths.length;
        if(fileLength<3 || fileLength>6){
            return  new JsonMessage(false,"图片数量有误");
        }else if(fileLength == 3){
            construction.setAfterPicA(filePaths[0]);
            construction.setAfterPicB(filePaths[1]);
            construction.setAfterPicC(filePaths[2]);
        }else if(fileLength == 4){
            construction.setAfterPicA(filePaths[0]);
            construction.setAfterPicB(filePaths[1]);
            construction.setAfterPicC(filePaths[2]);
            construction.setAfterPicD(filePaths[3]);
        }else if(fileLength == 5){
            construction.setAfterPicA(filePaths[0]);
            construction.setAfterPicB(filePaths[1]);
            construction.setAfterPicC(filePaths[2]);
            construction.setAfterPicD(filePaths[3]);
            construction.setAfterPicE(filePaths[4]);
        }else if(fileLength == 6){
            construction.setAfterPicA(filePaths[0]);
            construction.setAfterPicB(filePaths[1]);
            construction.setAfterPicC(filePaths[2]);
            construction.setAfterPicD(filePaths[3]);
            construction.setAfterPicE(filePaths[4]);
            construction.setAfterPicF(filePaths[5]);
        }
        constructionRepository.save(construction);
        return jsonMessage;
    }



}
