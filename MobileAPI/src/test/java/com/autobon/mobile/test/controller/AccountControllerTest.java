package com.autobon.mobile.test.controller;

import com.autobon.mobile.MobileApiApplication;
import com.autobon.mobile.entity.Technician;
import com.autobon.mobile.service.TechnicianService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dave on 16/2/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MobileApiApplication.class)
@WebAppConfiguration
public class AccountControllerTest {
    @Autowired WebApplicationContext wac;
    @Autowired MockHttpSession session;
    @Autowired TechnicianService technicianService;

    MockMvc mockMvc;
    String phone = "18827075338";
    String password = "123456";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void sendVerifySms() throws Exception {
        this.mockMvc.perform(get("/api/mobile/verifySms").session(session).param("phone", phone))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.result", is(true)))
            .andExpect(request().sessionAttribute("verifySms", "123456"));
    }

    @Test
    @Transactional
    public void register() throws Exception {
        this.mockMvc.perform(get("/api/mobile/verifySms").session(session).param("phone", phone))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/api/mobile/technician/register").session(session)
                .param("phone", phone)
                .param("password", password)
                .param("verifySms", "123456"))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.data.phone", is(phone)));

        Technician technician = technicianService.getByPhone(phone);
        Assert.assertNotNull(technician);

        this.mockMvc.perform(post("/api/mobile/technician/register").session(session)
                .param("phone", phone)
                .param("password", password)
                .param("verifySms", "123456"))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.error", is("OCCUPIED_ID")));
    }



}
