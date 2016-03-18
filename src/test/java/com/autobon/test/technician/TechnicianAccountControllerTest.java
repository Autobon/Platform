package com.autobon.test.technician;

import com.autobon.test.MvcTest;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Created by dave on 16/2/15.
 */
public class TechnicianAccountControllerTest extends MvcTest {
    @Autowired TechnicianService technicianService;
    @Value("${com.autobon.test.token}")
    String token;
    String phoneT = "18812345678";
    String phone = "18827075300";
    String password = "123456";

    @Test
    public void register() throws Exception {
        mockMvc.perform(get("/api/pub/verifySms").param("phone", phone))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/mobile/technician/register")
                .param("phone", phone)
                .param("password", password)
                .param("verifySms", "123456"))
            .andDo(print())
            .andExpect(jsonPath("$.data.phone", is(phone)));

        Technician technician = technicianService.getByPhone(phone);
        Assert.assertNotNull(technician);

        mockMvc.perform(post("/api/mobile/technician/register")
                .param("phone", phone)
                .param("password", password)
                .param("verifySms", "123456"))
            .andDo(print())
            .andExpect(jsonPath("$.error", is("OCCUPIED_ID")));
    }

    @Test
    public void response403() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/changePassword")
                .param("password", "123456"))
            .andExpect(status().is(403))
            .andDo(print());
    }

    @Test
    public void changeAndResetPassword() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/changePassword")
                .param("oldPassword", "123456")
                .param("newPassword", "221234")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));

        mockMvc.perform(post("/api/mobile/technician/login")
                .param("phone", phoneT)
                .param("password", "221234"))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));

        mockMvc.perform(get("/api/pub/verifySms").param("phone", phoneT));
        mockMvc.perform(post("/api/mobile/technician/resetPassword")
                .param("phone", phoneT)
                .param("password", "123456")
                .param("verifySms", "123456"))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));
    }


}
