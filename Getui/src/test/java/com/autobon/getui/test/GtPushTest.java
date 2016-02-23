package com.autobon.getui.test;

import com.autobon.getui.GetuiConfig;
import com.autobon.getui.GtConfig;
import com.autobon.getui.GtPush;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dave on 16/2/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GetuiConfig.class)
public class GtPushTest {
    @Autowired
    private GtConfig gtConfig;
    private GtPush gtPush;

    @Before
    public void setUp() {
        gtPush = new GtPush(gtConfig);
    }

    @Test
    public void connect() throws Exception {
        Assert.assertTrue(gtPush.connect());
    }
}
