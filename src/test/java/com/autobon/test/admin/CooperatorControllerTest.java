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

    @Test
    public void update() throws Exception {
        mockMvcS.perform(post("/api/web/admin/cooperator/update/1")
                .param("phone", "13085856332")
                .param("shortname", "A公司")
                .param("fullname", "A汽车美容公司")
                .param("businessLicense", "3335555")
                .param("corporationName", "张三")
                .param("corporationIdNo", "422365196605050001")
                .param("bussinessLicensePic", "a/a.jpg")
                .param("corporationIdPicA", "a/b.jpg")
                .param("corporationIdPicB", "a/c.jpg")
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
}
