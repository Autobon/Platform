package com.autobon.platform.controller.coop;

import com.autobon.order.entity.Comment;
import com.autobon.order.entity.Order;
import com.autobon.order.service.CommentService;
import com.autobon.order.service.OrderService;
import com.autobon.shared.JsonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dave on 16/3/3.
 */
@RestController("coopOrderController")
@RequestMapping("/api/mobile/coop/order")
public class OrderController {
    @Autowired OrderService orderService;
    @Autowired CommentService commentService;

    @RequestMapping(value="/comment",method = RequestMethod.POST)
    public JsonMessage comment(@RequestParam("orderId") int orderId,
                               @RequestParam("star") int star,
                               @RequestParam("arriveOnTime") int arriveOnTime,
                               @RequestParam("completeOnTime") int completeOnTime,
                               @RequestParam("professional") int professional,
                               @RequestParam("dressNeatly") int dressNeatly,
                               @RequestParam("carProtect") int carProtect,
                               @RequestParam("goodAttitude") int goodAttitude,
                               @RequestParam("advice") String advice){

        JsonMessage jsonMessage = new JsonMessage(true,"comment");
        Order order = orderService.findOrder(orderId);
        int mainTechId = order.getMainTechId();
        int secondTechId = order.getSecondTechId();
        if(mainTechId == 0){
            return new JsonMessage(false,"此订单未指定技师");
        }

        Comment comment = new Comment();
        comment.setTechnicianId(mainTechId);
        comment.setOrderId(orderId);
        comment.setStar(star);
        comment.setArriveOnTime(arriveOnTime);
        comment.setCompleteOnTime(completeOnTime);
        comment.setProfessional(professional);
        comment.setDressNeatly(dressNeatly);
        comment.setCarProtect(carProtect);
        comment.setGoodAttitude(goodAttitude);
        comment.setAdvice(advice);
        commentService.saveComment(comment);

        if(secondTechId != 0){
            comment.setId(0);
            comment.setTechnicianId(secondTechId);
            commentService.saveComment(comment);
        }

        return jsonMessage;
    }
}
