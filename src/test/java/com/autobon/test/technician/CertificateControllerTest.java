package com.autobon.test.technician;

import com.autobon.platform.Application;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by yuh on 2016/2/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CertificateControllerTest {
    @Autowired
    WebApplicationContext wac;
    @Autowired
    TechnicianService technicianService;
    @Autowired
    FilterChainProxy springFilterChain;
    @Value("${com.autobon.test.token}")
    String token;

    MockMvc mockMvc;
    MockMvc mockMvcS;
    String phone = "18827075300";
    String password = "123456";
    String[] stringArray = {"1","6"};

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockMvcS = MockMvcBuilders.webAppContextSetup(wac).addFilter(springFilterChain).build();
    }

    @Test
    public void commitCertificate() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/certificate")
                .param("name", "tom")
                .param("idNo","422302198608266313")
                .param("skills", "1,2")
                .param("bank","工商银行")
                .param("bankCardNo","88888888888")
                .cookie(new Cookie("autoken", token)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void changeBankCard() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/changeBankCard")
                .param("bank","026")
                .param("bankCardNo", "999999999222")
                .cookie(new Cookie("autoken",token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

}
