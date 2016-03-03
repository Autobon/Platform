package com.autobon.getui;

import com.autobon.getui.message.AppMessage;
import com.autobon.getui.message.Message;
import com.autobon.getui.payload.APNPayload;
import com.autobon.getui.template.TransmissionTemplate;
import com.autobon.getui.utils.AppConditions;
import com.autobon.getui.utils.GtConfig;
import com.autobon.getui.utils.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dave on 16/2/27.
 */
public class PushService {
    private GtConfig config;

    public PushService(GtConfig config) {
        this.config = config;
    }

    /**
     * 打开与个推服务器的连接. 在开始使用个推推送消息前应调用此方法.
     * @return
     * @throws IOException
     */
    public boolean connect() throws IOException {
        return GtPush.connect(config);
    }

    /**
     * 关闭与个推服务器的连接
     * @return
     * @throws IOException
     */
    public boolean close() throws IOException {
        return GtPush.close(config);
    }

    /**
     * 向指定用户发送透传推送消息
     * @param pushId 用户个推ID
     * @param notice 通知栏显示文字
     * @param json 透传给应用的json数据
     * @param expireInSeconds 在指定秒数过期. 个推支持最多72小时, 当小于等于0时,只有用户在线时才能收到推送消息
     * @return
     * @throws IOException
     */
    public boolean pushToSingle(String pushId, String notice, String json, int expireInSeconds) throws IOException {
        System.out.println(json);
        Message message = buildTransmissionMessage(notice, json, expireInSeconds, false);

        Target target = new Target();
        target.setAppId(config.getAppId());
        target.setClientId(pushId);
        return GtPush.pushToSingle(config, message, target);
    }

    /**
     * 向一组用户推送相同的消息. 个推文档没有明确一次群推支持的人数,建议一次群推至多1000人
     * @param pushIds 群推的用户个推ID数组
     * @param notice 通知栏显示文字
     * @param json 透传给应用的json数据
     * @param expireInSeconds 在指定秒数过期. 个推支持最多72小时, 当小于等于0时,只有用户在线时才能收到推送消息
     * @return
     * @throws IOException
     */
    public boolean pushToList(String[] pushIds, String notice, String json, int expireInSeconds) throws IOException {
        Message message = buildTransmissionMessage(notice, json, expireInSeconds, false);
        String contentId = GtPush.getContentId(config, message, "push_list");

        ArrayList<Target> targets = new ArrayList<>();
        for (String pushId : pushIds) {
            Target target = new Target();
            target.setAppId(config.getAppId());
            target.setClientId(pushId);
            targets.add(target);
        }
        return GtPush.pushToList(config, contentId, targets);
    }

    /**
     * 向配置的APP的所有用户发送推送消息
     * @param notice
     * @param json
     * @param expireInSeconds
     * @return
     * @throws IOException
     */
    public boolean pushToApp(String notice, String json, int expireInSeconds) throws IOException {
        Message message = buildTransmissionMessage(notice, json, expireInSeconds, true);
        String contentId = GtPush.getContentId(config, message, "push_list");
        return GtPush.pushToApp(config, contentId);
    }

    /**
     * 生成透传消息实例
     * @param notice
     * @param json
     * @param expireInSeconds
     * @return
     */
    protected Message buildTransmissionMessage(String notice, String json, int expireInSeconds, boolean isAppMessage) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(config.getAppId());
        template.setAppkey(config.getAppKey());
        template.setTransmissionType(1);
        template.setTransmissionContent(json);

        //ios
        APNPayload payload = new APNPayload();
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(notice));
        //payload.setBadge(1);
        template.setAPNInfo(payload);

        Message message;
        if (isAppMessage) {
            AppMessage appmsg = new AppMessage();
            appmsg.setAppIdList(Arrays.asList(config.getAppId()));
            appmsg.setConditions(new AppConditions());
            message = appmsg;
        } else {
            message = new Message();
        }
        message.setData(template);
        message.setOffline(expireInSeconds > 0);
        message.setOfflineExpireTime(expireInSeconds*1000);
        return message;
    }

}
