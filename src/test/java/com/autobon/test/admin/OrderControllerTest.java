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
 * Created by dave on 16/3/2.
 */
public class OrderControllerTest extends MvcTest {
    @Value("${com.autobon.test.adminToken}")
    private String token;

    @Test
    public void createOrder() throws Exception {
        mockMvcS.perform(post("/api/web/admin/order")
                .param("orderType", "1")
                .param("orderTime", "2016-03-01 12:02")
                .param("positionLon", "25.22342")
                .param("positionLat", "36.45485")
                .param("contactPhone", "18812345678")
                .param("contact", "小李子")
                .param("photo", "https://ss2.bdstatic.com/lfoZeXSm1A5BphGlnYG/skin/37.jpg?2")
                .param("remark", "remark is here")
                .cookie(new Cookie("autoken", token)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.result", is(true)));
    }
}
