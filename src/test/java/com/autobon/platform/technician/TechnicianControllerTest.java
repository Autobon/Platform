package com.autobon.platform.technician;

import com.autobon.getui.PushService;
import com.autobon.platform.MvcTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
/**
 * Created by dave on 16/2/26.
 */
public class TechnicianControllerTest extends MvcTest {
    @Autowired
    private PushService pushService;
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
    public void pushToSingle() throws Exception {
        Assert.assertTrue(pushService.pushToSingle("0f54394e1ccea495b2f3f0b702d69766",
                "你的认证申请已获通过。", "{\"action\":\"certificate_passed\"}", 60*60));
    }

    @Test
    public void pushToList() throws Exception {
        Assert.assertTrue(pushService.pushToList(new String[]{"0f54394e1ccea495b2f3f0b702d69766"},
                "你的认证申请已获通过。", "{\"action\":\"certificate_passed\"}", 60*60));
    }
}
