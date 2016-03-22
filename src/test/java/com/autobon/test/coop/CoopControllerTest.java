package com.autobon.test.coop;

import com.autobon.test.MvcTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by yuh on 2016/3/15.
 */
public class CoopControllerTest extends MvcTest {

    @Value("${com.autobon.test.coopToken}")
    private String token;

    @Test
    public void check() throws Exception {
        mockMvcS.perform(post("/api/mobile/coop/check")
                .param("fullname", "A汽车美容公司")
                .param("businessLicense", "3335555")
                .param("corporationName", "张三")
                .param("corporationIdNo", "422365196605050001")
                .param("bussinessLicensePic", "a/a.jpg")
                .param("corporationIdPicA", "a/b.jpg")
                .param("longitude", "120.34")
                .param("latitude", "35.55")
                .param("invoiceHeader", "武汉市A科技公司")
                .param("taxIdNo", "362362236")
                .param("postcode", "430000")
                .param("province", "湖北省")
                .param("city", "武汉市")
                .param("district", "光谷")
                .param("address", "中山路3号")
                .param("contact", "李四")
                .param("contactPhone", "13025523002")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void getCoop() throws Exception {
        mockMvcS.perform(get("/api/mobile/coop/getCoop")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

}
