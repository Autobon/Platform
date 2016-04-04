package com.autobon.platform.controller.admin;

import com.autobon.getui.PushService;
import com.autobon.message.entity.Message;
import com.autobon.message.service.MessageService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by liz on 2016/4/3.
 */
@RestController
@RequestMapping("/api/web/admin/message")
public class MessageController {
    private static Logger log = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    @Qualifier("PushServiceA")
    PushService pushServiceA;
    @Autowired
    @Qualifier("PushServiceB")
    PushService pushServiceB;

    /**
     * 查找分页通知
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage getMessages(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize){
        JsonPage msgs =  new JsonPage<>(messageService.findAll(page,pageSize));
        return new JsonMessage(true,"","",msgs);
    }

    /**
     * 添加一条通知
     * @param title
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonMessage saveMessage(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("sendTo") Integer sendTo) throws Exception{
        Message message = new Message();
        if(title.equals("")) return new JsonMessage(false,"","通知标题不能为空",null);
        message.setTitle(title);
        if(content.equals("")) return new JsonMessage(false,"","通知内容不能为空",null);
        message.setContent(content);
        if(sendTo == 0) return new JsonMessage(false,"","通知发送人不能为空",null);
        message.setSendto(sendTo);
        message.setUpdateTime(new Date());
        message.setStatus(0);
        Message msg = messageService.saveMsg(message);
        return new JsonMessage(true,"","添加成功",msg);
    }

    /**
     * 修改或者发布通知
     * @param id
     * @param title
     * @param content
     * @param sendTo
     * @param status
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public JsonMessage updateMessage(
            @PathVariable("id") Integer id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("sendTo") Integer sendTo,
            @RequestParam("status") Integer status) throws Exception{
        Message message = messageService.getById(id);
        if(!title.equals(""))message.setTitle(title);
        if(!content.equals(""))message.setContent(content);
        if(sendTo != 0)message.setSendto(sendTo);
        message.setUpdateTime(new Date());
        message.setStatus(status);
        messageService.saveMsg(message);
        if(status == 1){//发布
            HashMap<String, Object> map = new HashMap<>();
            map.put("action", "NEW_MESSAGE");
            map.put("order", message);
            map.put("title", title);
            boolean result;
            if(sendTo == 1){//发送给技师端
                 result = pushServiceA.pushToApp(title, new ObjectMapper().writeValueAsString(map), 0);
            }else{//发送给合作商户端
                 result = pushServiceB.pushToApp(title, new ObjectMapper().writeValueAsString(map), 0);
            }
            if (!result) log.info("通知: " + message.getId() + "的推送消息发送失败");
            publisher.publishEvent(message);
        }
        return new JsonMessage(true,"","修改成功",message);
    }


    /**
     * 删除通知
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public JsonMessage deleteMessage(@RequestParam("id") int id){
        Message msg = messageService.getById(id);
        if(msg != null) {
            if(msg.getStatus() == 0) {
                messageService.deleteMsg(id);
                return new JsonMessage(true, "", "删除成功", null);
            }else{
                return new JsonMessage(false, "", "已发布的通知不能删除", null);
            }
        }
        else{
            return new JsonMessage(false, "", "没有找到对应的通知记录", null);
        }
    }

}
