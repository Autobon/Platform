package com.autobon.getui.test;

import com.autobon.getui.GetuiConfig;
import com.autobon.getui.GtPush;
import com.autobon.getui.message.Message;
import com.autobon.getui.template.NotificationTemplate;
import com.autobon.getui.template.TransmissionTemplate;
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

        Message message = new Message();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(60*60*1000);

        String contentId = gtPush.getContentId(message, "test_push_list");
        Assert.assertTrue(contentId != null);

        List<Target> targets = new ArrayList<>();
        Target target = new Target();
        target.setAppId(gtConfig.getAppId());
        target.setClientId("9a05f0154913d57ef537fdf51ffc14bd");
        targets.add(target);
        Assert.assertTrue(gtPush.pushToList(contentId, targets));
    }

    @Test
    public void pushToSingle() throws Exception {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(gtConfig.getAppId());
        template.setAppkey(gtConfig.getAppKey());
        template.setTransmissionType(1);
        template.setTransmissionContent("os-toApp11111");

        Message message = new Message();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(60*60*1000);

        Target target = new Target();
        target.setAppId(gtConfig.getAppId());
        target.setClientId("9a05f0154913d57ef537fdf51ffc14bd");
        Assert.assertTrue(gtPush.pushToSingle(message, target));
    }

    @Test
    public void close() throws Exception {
        Assert.assertTrue(gtPush.close());
    }

}
