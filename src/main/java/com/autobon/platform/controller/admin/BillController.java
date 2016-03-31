package com.autobon.platform.controller.admin;

import com.autobon.shared.JsonMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dave on 16/3/31.
 */
@RestController("adminBillController")
@RequestMapping("/api/web/admin/bill")
public class BillController {

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage search() {

        return new JsonMessage(true);
    }
}
