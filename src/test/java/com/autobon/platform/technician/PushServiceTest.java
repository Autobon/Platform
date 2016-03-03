package com.autobon.platform.technician;

import com.autobon.getui.PushService;
import com.autobon.platform.MvcTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by dave on 16/3/2.
 */
public class PushServiceTest extends MvcTest {
    @Autowired @Qualifier("PushServiceA")
    private PushService pushService;

    @Test
    public void pushToSingle() throws Exception {
        Assert.assertTrue(pushService.pushToSingle("0f54394e1ccea495b2f3f0b702d69766",
                "你的认证申请已获通过。",
                "{\"action\":\"certificate_passed\", \"title\":\"你的认证申请已获通过。\"}",
                60 * 60));
    }

    @Test
    public void pushToList() throws Exception {
        Assert.assertTrue(pushService.pushToList(new String[]{"0f54394e1ccea495b2f3f0b702d69766",
                        "114d241a51cc8540346622de3f36c9f2"},
                "你的认证申请已获通过。",
                "{\"action\":\"certificate_passed\", \"title\":\"你的认证申请已获通过。\"}",
                60 * 60));
    }

    @Test
    public void pushToApp() throws Exception {
        Assert.assertTrue(pushService.pushToApp("你的认证申请已获通过。", "can you see me", 3600));
    }
}
