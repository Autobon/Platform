package com.autobon.test;

import com.autobon.platform.Application;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

/**
 * 需要使用MockMvc的单元测试, 可以继承此类, 就不用在测试类上加一堆注解.
 * 比如: admin.TechnicianControllerTest
 * Created by dave on 16/2/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public abstract class MvcTest {
    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    protected FilterChainProxy springFilterChain;
    protected MockMvc mockMvc;
    protected MockMvc mockMvcS;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockMvcS = MockMvcBuilders.webAppContextSetup(wac).addFilter(springFilterChain).build();
    }
}
