package com.autobon.platform.order;

import com.autobon.platform.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by yuh on 2016/2/22.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class OrderControllerTest {
    @Value("${com.autobon.test.token}")
    String token;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    FilterChainProxy springFilterChain;

    MockMvc mockMvc;
    MockMvc mockMvcS;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockMvcS = MockMvcBuilders.webAppContextSetup(wac).addFilter(springFilterChain).build();
    }

    @Test
    public void orderList() throws Exception {
        mockMvcS.perform(get("/api/mobile/order/orderList")
                .cookie(new Cookie("autoken",token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void getLocation() throws Exception {
        mockMvcS.perform(get("/api/mobile/order/getLocation?orderId=1")
                .cookie(new Cookie("autoken",token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void signIn() throws Exception {
        mockMvcS.perform(post("/api/mobile/construction/signIn")
                .param("rtpositionLon","111.11111111")
                .param("rtpositionLat","55.555555")
                .param("technicianId","1")
                .param("orderId", "1")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    String[] filePaths = {"a/a.jpg","a/b.jpg","a/c.jpg"};
    @Test
    public void saveBeforePic() throws Exception {
        mockMvcS.perform(post("/api/mobile/construction/saveBeforePic")
                .param("constructionId","3")
                .param("filePaths",filePaths)
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    String[] filePathss = {"a/a.jpg","a/b.jpg","a/c.jpg","a/e.jpg","a/f.jpg"};
    @Test
    public void saveAfterPic() throws Exception {
        mockMvcS.perform(post("/api/mobile/construction/saveAfterPic")
                .param("constructionId", "3")
                .param("filePaths",filePathss)
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }


    @Test
    public void addSecondTechId() throws Exception {
        mockMvcS.perform(post("/api/mobile/order/addSecondTechId")
                .param("orderId", "1")
                .param("technicianId","2")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }


    @Test
    public void comment() throws Exception {
        mockMvcS.perform(post("/api/mobile/coop/order/comment")
                .param("orderId","1")
                .param("star", "5")
                .param("arriveOnTime","true")
                .param("completeOnTime","true")
                .param("professional","true")
                .param("dressNeatly","true")
                .param("carProtect","true")
                .param("goodAttitude","true")
                .param("advice","贴膜技术不错")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

}
