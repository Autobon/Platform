package com.autobon.platform.technician.order;

import com.autobon.platform.MvcTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by dave on 16/3/15.
 */
public class BillControllerTest extends MvcTest {
    @Value("${com.autobon.test.token}")
    String token;

    @Before
    public void setup() {
        super.setup();

    }


    @Test
    public void list() throws Exception {
        mockMvcS.perform(get("/api/mobile/technician/bill")
                .cookie(new Cookie("autoken", token)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test void showOrders() throws Exception {

    }
}
