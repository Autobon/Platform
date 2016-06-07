package com.autobon.test.admin;


import com.autobon.test.MvcTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by liz on 2016/2/21.
 */
public class StaffAccountControllerTest extends MvcTest {
    @Value("${com.autobon.test.adminToken}")
    private String token;
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

    @Test
    public void resetPassword() throws Exception {
        mockMvc.perform(get("/api/pub/verifySms").param("phone", "15107116464"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/web/admin/resetPassword")
                .param("phone", "15107116464")
                .param("verifyCode", "123456"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void changePassword() throws Exception {
        mockMvcS.perform(post("/api/web/admin/changePassword")
                .param("oldPassword", "123456")
                .param("newPassword", "654321")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void user()throws Exception{
        mockMvc.perform(get("/api/pub/verifySms").param("phone", "15107116464"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/validate")
                .param("phone", "15107116464")
                .param("verifycode", "123456"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void login2()throws Exception{
        mockMvc.perform(get("/api/pub/verifySms").param("phone", "15107116464"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/loginByPhone")
                .param("phone", "15107116464")
                .param("verifyCode", "123456"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void changePhone()throws Exception{
        mockMvc.perform(get("/api/pub/verifySms").param("phone", "15107116464"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user/changePhone")
                .param("OidPhone", "15107116464")
                .param("phone", "15107116461")
                .param("NewCode", "123456")
                .param("id", "4"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void changePhone2()throws Exception{
        mockMvc.perform(get("/api/pub/verifySms").param("phone", "15107116464"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user/changPassword2")
                .param("phone", "15107116464")
                .param("password", "123456")
                .param("verifyCode", "123456"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

}
