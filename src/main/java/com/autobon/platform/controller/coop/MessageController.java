package com.autobon.platform.controller.coop;

import com.autobon.message.service.MessageService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dave on 16/4/12.
 */
@RestController("coopMessageController")
@RequestMapping("/api/mobile/coop/message")
public class MessageController {
    @Autowired MessageService messageService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage list(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return new JsonMessage(true, "", "", new JsonPage<>(messageService.find(2, 1, page, pageSize)));
    }
}
