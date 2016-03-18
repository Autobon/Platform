package com.autobon.test.admin;

import com.autobon.order.entity.Construction;
import com.autobon.order.service.ConstructionService;
import com.autobon.test.MvcTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by lu on 2016/3/18.
 */

public class ConstructionControllerTest extends MvcTest {
    @Value("${com.autobon.test.adminToken}")
    private String token;
    @Autowired
    ConstructionService constructionService;
    private  Construction construction;
    @Before
    public  void setup() throws  Exception{
        super.setup();
        construction=new Construction();
        constructionService.save(construction);
    }
    @Test
    public void update() throws Exception{
        mockMvcS.perform(post("/api/web/admin/construction/update/"+construction.getId())
                .param("payment", "2000.1")
                .param("workItems", "车身改色")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }
    @Test
    public void getConstruction() throws Exception{
        mockMvcS.perform(get("/api/web/admin/construction/"+construction.getId())
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

}
