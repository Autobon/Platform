package com.autobon.platform.admin;

import com.autobon.platform.MvcTest;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TechnicianService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by dave on 16/3/4.
 */
public class TechnicianControllerTest extends MvcTest {
    @Autowired
    TechnicianService technicianService;
    @Value("${com.autobon.test.adminToken}")
    private String adminToken;
    @Value("${com.autobon.test.token}")
    private String techToken;
    private Technician tech;

    @Before
    public void setup() {
        super.setup();
        tech = technicianService.get(Technician.decodeToken(techToken));
    }


    @Test
    public void verify() throws Exception {
        mockMvcS.perform(post("/api/web/admin/technician/verify/" + tech.getId())
                .param("verified", "true")
                .cookie(new Cookie("autoken", adminToken)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void update() throws Exception {
        mockMvcS.perform(post("/api/web/admin/technician/" + tech.getId())
                .param("phone", "13070705003")
                .param("name", "henry")
                .param("gender", "man")
                .param("idNo", "422111198602020011")
                .param("idPhoto","a/a.jpg")
                .param("bank","0003")
                .param("bankAddress","wuhan guanggu")
                .param("bankCardNo", "99998888")
                .param("skill","1,2,3")
                .cookie(new Cookie("autoken", adminToken)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }


    @Test
    public void search() throws Exception {
        mockMvcS.perform(get("/api/web/admin/technician?query=18812345678")
                .cookie(new Cookie("autoken", adminToken)))
                .andDo(print())
                .andExpect(jsonPath("$.result", is(true)));
    }
}
