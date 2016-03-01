package com.autobon.platform.controller.admin;

import com.autobon.shared.JsonMessage;
import com.autobon.technician.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dave on 16/3/1.
 */
@RestController
@RequestMapping("/api/admin/technician")
public class TechnicianController {
    @Autowired TechnicianService technicianService;

    @RequestMapping(value = "/{techId}", method = RequestMethod.GET)
    public JsonMessage show(@PathVariable("techId") int techId) {
        return new JsonMessage(true, "", "", technicianService.get(techId));
    }

    @RequestMapping(value = "/{techId}", method = RequestMethod.POST)
    public JsonMessage update(@PathVariable("techId") int techId) {

        return null;
    }
}
