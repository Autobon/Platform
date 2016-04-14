package com.autobon.platform.controller.admin;

import com.autobon.shared.JsonMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dave on 16/4/14.
 */
@RestController
@RequestMapping("/api/web/admin/stat")
public class StatController {

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage getLast() {

        return new JsonMessage(true);
    }
}
