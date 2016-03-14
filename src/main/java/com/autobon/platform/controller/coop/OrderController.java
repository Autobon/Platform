package com.autobon.platform.controller.coop;

import com.autobon.order.entity.Comment;
import com.autobon.order.entity.Order;
import com.autobon.technician.entity.TechStat;
import com.autobon.order.service.CommentService;
import com.autobon.order.service.OrderService;
import com.autobon.technician.service.TechStatService;
import com.autobon.shared.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by dave on 16/3/3.
 */
@RestController("coopOrderController")
@RequestMapping("/api/mobile/coop/order")
public class OrderController {
    @Autowired OrderService orderService;
    @Autowired CommentService commentService;
    @Autowired TechStatService techStatService;

    @PersistenceContext
    private EntityManager entityManager;


    @RequestMapping(value="/comment",method = RequestMethod.POST)
    public JsonMessage comment(@RequestParam("orderId") int orderId,
                               @RequestParam("star") int star,
                               @RequestParam(value = "arriveOnTime",defaultValue = "false") boolean arriveOnTime,
                               @RequestParam(value = "completeOnTime",defaultValue = "false") boolean completeOnTime,
                               @RequestParam(value = "professional",defaultValue = "false") boolean professional,
                               @RequestParam(value = "dressNeatly",defaultValue = "false") boolean dressNeatly,
                               @RequestParam(value = "carProtect",defaultValue = "false") boolean carProtect,
                               @RequestParam(value = "goodAttitude",defaultValue = "false") boolean goodAttitude,
                               @RequestParam("advice") String advice){

        JsonMessage jsonMessage = new JsonMessage(true,"comment");
        Order order = orderService.get(orderId);
        int mainTechId = order.getMainTechId();
        int secondTechId = order.getSecondTechId();
        if(mainTechId == 0){
            return new JsonMessage(false,"此订单未指定技师");
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

        // 写入技师星级统计
        TechStat mainStat = techStatService.getByTechId(mainTechId);
        if (mainStat == null) {
            mainStat = new TechStat();
            mainStat.setTechId(mainTechId);
        }
        mainStat.setStarRate(commentService.calcStarRateByTechId(mainTechId,
                Date.from(LocalDate.now().minusMonths(12).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
        techStatService.save(mainStat);


        if(secondTechId != 0){
            entityManager.detach(comment);
            comment.setId(0);
            comment.setTechId(secondTechId);
            commentService.save(comment);

            // 写入技师星级统计
            TechStat secondStat = techStatService.getByTechId(secondTechId);
            if (secondStat == null) {
                secondStat = new TechStat();
                secondStat.setTechId(secondTechId);
            }
            secondStat.setStarRate(commentService.calcStarRateByTechId(secondTechId,
                    Date.from(LocalDate.now().minusMonths(12).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
            techStatService.save(secondStat);
        }

        return jsonMessage;
    }
}
