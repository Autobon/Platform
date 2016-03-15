package com.autobon.test.admin;


import com.autobon.test.MvcTest;
import org.junit.Test;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by liz on 2016/2/21.
 */
public class StaffAccountControllerTest extends MvcTest {
    String admin = "admin";

    @Test
    public void register() throws Exception {
        mockMvc.perform(post("/api/web/admin/register")
                .param("username", "admin-test")
                .param("email", "ab@c.com")
                .param("password", "123456"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.result", is(false)));

        mockMvc.perform(post("/api/web/admin/register")
                .param("username", "admin_test")
                .param("email", "ab@c.com")
                .param("password", "123456"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void login() throws Exception {
        mockMvc.perform(post("/api/web/admin/login")
                .param("username", "admin")
                .param("password", "123456"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.result", is(true)));
    }



}
