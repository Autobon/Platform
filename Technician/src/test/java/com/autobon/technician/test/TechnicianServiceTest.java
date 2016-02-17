package com.autobon.technician.test;

import com.autobon.technician.Application;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

/**
 * Created by dave on 16/2/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TechnicianServiceTest {
    @Autowired
    private TechnicianService technicianService;

    private Technician technician;

    @Before
    public void setUp() {
        technician = new Technician();
        technician.setPhone("abc@def.com");
        technician.setPassword(Technician.encryptPassword("123456"));
    }

    @Test
    @Transactional
    public void testAdd() {
        Assert.assertEquals(technician.getId(), 0);
        technicianService.save(technician);
        Assert.assertTrue(technician.getId() > 0);
    }
}
