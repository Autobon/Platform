package com.autobon.test.admin;

import com.autobon.test.MvcTest;
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
                .param("statusCode", "1")
                .param("page", "1")
                .param("pageSize", "10")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void checkCoop() throws Exception {
        mockMvcS.perform(post("/api/web/admin/cooperator/checkCoop/1")
                .param("statusCode","2")
                .param("resultDesc", "照片不清楚")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }
}
