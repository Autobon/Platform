package com.autobon.getui.test;

import com.autobon.getui.Application;
import com.autobon.getui.GtPush;
import com.autobon.getui.PushService;
import com.autobon.getui.utils.GtConfig;
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
@SpringApplicationConfiguration(classes = Application.class)
public class GtPushTest {
    @Autowired
    private GtConfig gtConfig;

    @Before
    public void setup() throws Exception {
        GtPush.connect(gtConfig);
    }

    @Test
    public void connect() throws Exception {
        Assert.assertTrue(GtPush.connect(gtConfig));
    }

//    @Test
//    public void pushToApp() throws Exception {
//        Assert.assertTrue(new PushService(gtConfig).pushToApp("this is title", "this is json", 0));
//    }

        /*
    @Test
    public void pushToList() throws Exception {
        GtPush.connect(gtConfig);
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(gtConfig.getAppId());
        template.setAppkey(gtConfig.getAppKey());
        template.setTransmissionType(1);
        template.setTransmissionContent("{\"action\":\"startup\", \"by\":\"list\"}");

        //ios
        APNPayload payload = new APNPayload();
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg("push from list"));
        //payload.setBadge(1);
        template.setAPNInfo(payload);

        Message message = new Message();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(60*60*1000);

        String contentId = GtPush.getContentId(gtConfig, message, "test_push_list");
        Assert.assertTrue(contentId != null);

        List<Target> targets = new ArrayList<>();
        Target target = new Target();
        target.setAppId(gtConfig.getAppId());
        target.setClientId("0f54394e1ccea495b2f3f0b702d69766");
        targets.add(target);
        Assert.assertTrue(GtPush.pushToList(gtConfig, contentId, targets));
    }


    @Test
    public void pushToSingle() throws Exception {
        GtPush.connect(gtConfig);
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(gtConfig.getAppId());
        template.setAppkey(gtConfig.getAppKey());
        template.setTransmissionType(1);
        template.setTransmissionContent("{\"action\":\"startup20\"}");

        //ios
        APNPayload payload = new APNPayload();
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg("this is title5"));
        //payload.setBadge(1);
        template.setAPNInfo(payload);

        Message message = new Message();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(60*60*1000);

        Target target = new Target();
        target.setAppId(gtConfig.getAppId());
        target.setClientId("0f54394e1ccea495b2f3f0b702d69766");
        Assert.assertTrue(GtPush.pushToSingle(gtConfig, message, target));
    }

    */
    @Test
    public void close() throws Exception {
        Assert.assertTrue(GtPush.close(gtConfig));
    }
}
