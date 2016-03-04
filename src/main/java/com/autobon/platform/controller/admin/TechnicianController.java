package com.autobon.platform.controller.admin;

import com.autobon.getui.PushService;
import com.autobon.shared.JsonMessage;
import com.autobon.shared.JsonPage;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by dave on 16/3/1.
 */
@RestController("adminTechnicianController")
@RequestMapping("/api/web/admin/technician")
public class TechnicianController {
    @Autowired TechnicianService technicianService;
    @Autowired PushService pushService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonMessage list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        return new JsonMessage(true, "", "",
                new JsonPage<>(technicianService.findAll(page, pageSize)));
    }

    @RequestMapping(value = "/{techId}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("techId") int techId) {
        return new JsonMessage(true, "", "", technicianService.get(techId));
    }

    @RequestMapping(value = "/{techId}", method = RequestMethod.POST)
    public JsonMessage update(@PathVariable("techId") int techId) {

        // TODO 更新技师信息
        return null;
    }

    @RequestMapping(value = "/verify/{techId}", method = RequestMethod.POST)
    public JsonMessage verify(
            @PathVariable("techId") int techId,
            @RequestParam("verified") boolean verified) throws IOException {
        Technician tech = technicianService.get(techId);
        if (tech != null) {
            if (verified) {
                tech.setStatus(Technician.Status.VERIFIED);
                String title = "你已通过技师资格认证";
                pushService.pushToSingle(tech.getPushId(), title,
                        "{\"action\":\"VERIFICATION_SUCCEED\",\"title\":\"" + title + "\"}",
                        3*24*3600);
            } else {
                tech.setStatus(Technician.Status.REJECTED);
                String title = "你的技师资格认证失败,请更新信息后重新申请认证";
                pushService.pushToSingle(tech.getPushId(), title,
                        "{\"action\":\"VERIFICATION_FAILED\",\"title\":\"" + title + "\"}",
                        3*24*3600);
            }
            technicianService.save(tech);
            return new JsonMessage(true);
        } else {
            return new JsonMessage(false, "ILLEGAL_PARAM", "没有这个技师");
        }
    }
}
