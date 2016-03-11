package com.autobon.platform.admin;

import com.autobon.platform.MvcTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by yuh on 2016/3/10.
 */
public class CooperatorControllerTest extends MvcTest {

    @Value("${com.autobon.test.adminToken}")
    private String token;

    @Test
    public void coopList() throws Exception {
        mockMvcS.perform(post("/api/web/admin/cooperator/coopList")
                .param("fullname", "tom")
                .param("businessLicense", "0001")
                .param("contactPhone", "13075755002")
                .param("page", "1")
                .param("pageSize", "10")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }
}
