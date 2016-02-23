package com.autobon.getui.test;

import com.autobon.getui.GetuiConfig;
import com.autobon.getui.GtPush;
import com.autobon.getui.message.ListMessage;
import com.autobon.getui.template.NotificationTemplate;
import com.autobon.getui.utils.GtConfig;
import com.autobon.getui.utils.Target;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Test
    public void pushToList() throws Exception {
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(gtConfig.getAppId());
        template.setAppkey(gtConfig.getAppKey());
        template.setTitle("请输入通知栏标题");
        template.setText("请输入通知栏内容");
        template.setLogo("icon.png");
        template.setLogoUrl("");
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
        template.setTransmissionType(1);
        template.setTransmissionContent("请输入您要透传的内容");

        ListMessage message = new ListMessage();
        List<Target> targets = new ArrayList<>();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(60*60*1000);

        String contentId = gtPush.getContentId(message, "test_push_list");
        System.out.println("ContentId: " + contentId);
        Assert.assertTrue(contentId != null);

        Target target = new Target();
        target.setAppId(gtConfig.getAppId());
        target.setClientId("");
        targets.add(target);
        Map<String, Object> map = gtPush.pushToList(contentId, targets);
        System.out.println(map);
    }

    @Test
    public void close() throws Exception {
        Assert.assertTrue(gtPush.close());
    }

}
