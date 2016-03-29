package com.autobon.test.coop;

import com.autobon.platform.Application;
import com.autobon.test.MvcTest;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by yuh on 2016/3/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class CoopAccountControllerTest{
    @Value("${com.autobon.test.coopToken}")
    String token;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    FilterChainProxy springFilterChain;

    MockMvc mockMvc;
    MockMvc mockMvcS;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockMvcS = MockMvcBuilders.webAppContextSetup(wac).addFilter(springFilterChain).build();
    }


    @Test
    public void register() throws Exception {
        //System.out.println(Cooperator.makeToken(1));

        mockMvc.perform(get("/api/pub/verifySms").param("phone", "13072705335"))
                .andExpect(status().isOk());

        mockMvcS.perform(post("/api/mobile/coop/register")
                .param("shortname","tomcat")
                .param("phone", "13072705335")
                .param("password","123456")
                .param("verifySms","123456"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }
    @Test
    public void login() throws Exception {
        mockMvcS.perform(post("/api/mobile/coop/login")
                .param("shortname","Tom")
                .param("phone", "13072726003")
                .param("password", "123456"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void resetPassword() throws Exception {
        mockMvc.perform(get("/api/pub/verifySms").param("phone", "13072726003"));
        mockMvc.perform(post("/api/mobile/coop/resetPassword")
                .param("phone", "13072726003")
                .param("password", "123456")
                .param("verifySms", "123456"))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void changePassword() throws Exception {
        mockMvcS.perform(post("/api/mobile/coop/changePassword")
                .param("oldPassword", "123456")
                .param("newPassword", "123456")
                .cookie(new Cookie("autoken", token)))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void getSaleList() throws Exception {
        mockMvcS.perform(post("/api/mobile/coop/getSaleList")
                .cookie(new Cookie("autoken", token)))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));

    }

    @Test
    public void saleFired() throws Exception {
        mockMvcS.perform(post("/api/mobile/coop/saleFired")
                .param("coopAccountId", "1")
                .cookie(new Cookie("autoken", token)))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));

    }

    @Test
    public void addAccount() throws Exception {
        mockMvcS.perform(post("/api/mobile/coop/addAccount")
                .param("phone", "13072735003")
                .param("name", "Bush")
                .param("gender", "false")
                .cookie(new Cookie("autoken", token)))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));

    }

    @Test
    public void changeAccountPassword() throws Exception {
        mockMvcS.perform(post("/api/mobile/coop/changeAccountPassword")
                .param("coopAccountId", "1")
                .param("oldPassword", "123456")
                .param("newPassword", "123456")
                .cookie(new Cookie("autoken", token)))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));

    }

}
