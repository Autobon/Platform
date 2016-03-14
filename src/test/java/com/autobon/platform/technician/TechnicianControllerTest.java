package com.autobon.platform.technician;

import com.autobon.platform.MvcTest;
import org.hamcrest.Matchers;
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
 * Created by dave on 16/2/26.
 */
public class TechnicianControllerTest extends MvcTest {
    @Value("${com.autobon.test.token}")
    String token;
    String phone = "18812345678";
    String name = "tom";

    @Test
    public void query() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/search")
                .param("query", phone)
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.data.count", Matchers.is(1)));
        mockMvcS.perform(get("/api/mobile/technician/search")
                .param("query", name)
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.data.count", Matchers.greaterThanOrEqualTo(1)));
    }

    @Test
    public void show() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician")
                .param("query", phone)
                .cookie(new Cookie("autoken", token)))
                .andDo(print())
                .andExpect(jsonPath("$.result", Matchers.is(true)));
    }

    @Test
    public void reportLocation() throws Exception {
        mockMvcS.perform(post("/api/mobile/technician/reportLocation")
                .param("rtpostionLon","144.4")
                .param("rtpositionLat", "34.4")
                .cookie(new Cookie("autoken",token)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.result", is(true)));
    }

}
