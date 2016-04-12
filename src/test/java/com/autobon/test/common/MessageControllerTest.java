package com.autobon.test.common;

import com.autobon.test.MvcTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.Cookie;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dave on 16/4/12.
 */
public class MessageControllerTest extends MvcTest {
    @Value("${com.autobon.test.token}")
    String techToken;
    @Value("${com.autobon.test.coopToken}")
    String coopToken;

    @Test
    public void listForCoop() throws Exception {
        this.mockMvcS.perform(get("/api/mobile/coop/message").cookie(new Cookie("autoken", coopToken)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void listForTech() throws Exception {
        this.mockMvcS.perform(get("/api/mobile/technician/message").cookie(new Cookie("autoken", techToken)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }
}
