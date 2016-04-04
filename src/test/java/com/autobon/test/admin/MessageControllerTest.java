package com.autobon.test.admin;

import com.autobon.test.MvcTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by liz on 2016/4/3.
 */
public class MessageControllerTest  extends MvcTest{

    @Value("${com.autobon.test.adminToken}")
    private String adminToken;

    @Test
    public void getMessages() throws Exception {
        mockMvcS.perform(get("/api/web/admin/message?page=1&pageSize=10")
                .cookie(new Cookie("autoken", adminToken)))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void saveMessage() throws Exception {
        mockMvcS.perform(post("/api/web/admin/message")
                .param("title", "四月贴膜季")
                .param("content", "四月贴膜季，贴膜酬劳加倍，balabalabala...")
                .param("sendTo", "1")
                .cookie(new Cookie("autoken", adminToken)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

}
