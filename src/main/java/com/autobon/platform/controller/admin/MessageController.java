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

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by liz on 2016/4/3.
 */
@RestController("adminMessageController")
@RequestMapping("/api/web/admin/message")
public class MessageController {
    private static Logger log = LoggerFactory.getLogger(MessageController.class);
    @Autowired MessageService messageService;
    @Autowired ApplicationEventPublisher publisher;
    @Autowired @Qualifier("PushServiceA") PushService pushServiceA;
    @Autowired @Qualifier("PushServiceB") PushService pushServiceB;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage getMessages(
            @RequestParam(value = "sendTo", required = false) Integer sendTo,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return new JsonMessage(true, "", "", new JsonPage<>(messageService.find(sendTo, status, page, pageSize)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonMessage saveMessage(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("sendTo") Integer sendTo) {
        if (sendTo != 1 && sendTo != 2) return new JsonMessage(false, "ILLEGAL_PARAM_VALUE", "sendTo取值只能为1或2");

        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setSendto(sendTo);
        Message msg = messageService.save(message);
        return new JsonMessage(true, "", "添加成功", msg);
    }

    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.POST)
    public JsonMessage updateMessage(
            @PathVariable("id") int id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("sendTo") int sendTo) {
        if (sendTo != 1 && sendTo != 2) return new JsonMessage(false, "ILLEGAL_PARAM_VALUE", "sendTo取值只能为1或2");
        Message message = messageService.getById(id);
        if (message == null) return new JsonMessage(false, "NO_SUCH_RECORD", "没有这条记录");

        message.setTitle(title);
        message.setContent(content);
        message.setSendto(sendTo);
        messageService.save(message);
        return new JsonMessage(true);
    }

    @RequestMapping(value = "/publish/{id:\\d+}", method = RequestMethod.POST)
    public JsonMessage publishMessage(@PathVariable("id") int id) throws IOException {
        Message msg = messageService.getById(id);
        if (msg.getStatus() == 1) {
            return new JsonMessage(false, "ALREADY_PUBLISHED", "通知消息已发布");
        } else {
            msg.setPublishTime(new Date());
            msg.setStatus(1);
            messageService.save(msg);

            HashMap<String, Object> map = new HashMap<>();
            map.put("action", "NEW_MESSAGE");
            map.put("message", msg);
            map.put("title", msg.getTitle());
            boolean pushResult;
            if (msg.getSendto() == 1) {
                pushResult = pushServiceA.pushToApp(msg.getTitle(), new ObjectMapper().writeValueAsString(map), 72 * 3600);
            } else {
                pushResult = pushServiceB.pushToApp(msg.getTitle(), new ObjectMapper().writeValueAsString(map), 72 * 3600);
            }
            if (!pushResult) log.error("通知【id=" + msg.getId() + "】推送失败");
            return new JsonMessage(true);
        }
    }

    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public JsonMessage deleteMessage(@PathVariable("id") Integer id) {
        Message msg = messageService.getById(id);
        if (msg != null) {
            if (msg.getStatus() == 0) {
                messageService.delete(id);
                return new JsonMessage(true);
            } else {
                return new JsonMessage(false, "CANNOT_DELETE", "已发布的通知不能删除");
            }
        } else {
            return new JsonMessage(false, "NO_SUCH_RECORD", "没有此条记录");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonMessage getMessage(@PathVariable("id") Integer id) {
        return new JsonMessage(true, "", "", messageService.getById(id));
    }
}
