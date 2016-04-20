package com.autobon.test.admin;

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
 * Created by yuh on 2016/3/10.
 */
public class CooperatorControllerTest extends MvcTest {

    @Value("${com.autobon.test.adminToken}")
    private String token;

    @Test
    public void search() throws Exception {
        mockMvcS.perform(get("/api/web/admin/cooperator")
                .param("fullname", "tom")
                .param("statusCode", "1")
                .param("page", "1")
                .param("pageSize", "10")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void verify() throws Exception {
        mockMvcS.perform(post("/api/web/admin/cooperator/verify/1")
                .param("verified","false")
                .param("remark", "照片不清楚")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void update() throws Exception {
        mockMvcS.perform(post("/api/web/admin/cooperator/update/1")
                .param("fullname", "A汽车美容公司")
                .param("businessLicense", "3335555")
                .param("corporationName", "张三")
                .param("corporationIdNo", "422365196605050001")
                .param("bussinessLicensePic", "/uploads/coop/20160420001122.jpg")
                .param("corporationIdPicA", "/uploads/coop/20160420001123.jpg")
                .param("corporationIdPicB", "/uploads/coop/20160420001124.jpg")
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
    public void createCoop() throws Exception {
        mockMvcS.perform(post("/api/web/admin/cooperator/createCoop")
                .param("fullname", "A汽车美容公司")
                .param("businessLicense", "3335555")
                .param("corporationName", "张三")
                .param("corporationIdNo", "422130199202030016")
                .param("bussinessLicensePic", "/uploads/coop/20160420001122.jpg")
                .param("corporationIdPicA", "/uploads/coop/20160420001123.jpg")
                .param("invoiceHeader", "武汉市A科技公司")
                .param("taxIdNo", "362362236")
                .param("postcode", "430000")
                .param("address", "中山路3号")
                .param("contact", "李四")
                .param("contactPhone", "13025523002")
                .param("phone", "13085856335")
                .param("shortname", "Corpration ABC")
                .param("password","123456")
                .param("rpassword","123456")
                .cookie(new Cookie("autoken", token)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }
}
