package com.autobon.getui;

import com.autobon.getui.message.AppMessage;
import com.autobon.getui.message.ListMessage;
import com.autobon.getui.message.Message;
import com.autobon.getui.message.SingleMessage;
import com.autobon.getui.utils.AppConditions;
import com.autobon.getui.utils.GtConfig;
import com.autobon.getui.utils.GtHttp;
import com.autobon.getui.utils.Target;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by dave on 16/2/22.
 */
public class GtPush {
    private final Logger LOG = LoggerFactory.getLogger(GtPush.class);
    private GtConfig config;

    public GtPush(GtConfig config) {
        this.config = config;
    }

    public boolean connect() throws IOException {
        Long timestamp = new Date().getTime();
        String sign = encryptByMd5(config.getAppKey() + timestamp + config.getMasterSecret());
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "connect");
        map.put("appkey", config.getAppKey());
        map.put("timeStamp", timestamp.toString());
        map.put("sign", sign);
        String json = GtHttp.postJson(config.getHost(), map);
        LOG.info(json);
        return new ObjectMapper().readTree(json).path("result").asText().equals("success");
    }

    public boolean close() throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "close");
        map.put("appkey", config.getAppKey());
        String json = GtHttp.postJson(config.getHost(), map);
        LOG.info(json);
        return new ObjectMapper().readTree(json).path("result").asText().equals("success");
    }

    public String getContentId(Message message, String taskGroupName) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        if (taskGroupName != null) {
            map.put("taskGroupName", taskGroupName);
        }
        map.put("action", "getContentIdAction");
        map.put("appkey", config.getAppKey());
        map.put("clientData", Base64.getEncoder().encodeToString(message.getData().getTransparent().toByteArray()));
        map.put("isOffline", message.isOffline());
        map.put("offlineExpireTime", message.getOfflineExpireTime());
        map.put("pushType", message.getData().getPushType());
        map.put("type", 2);
        if (message instanceof ListMessage) {
            map.put("contentType", 1);
        } else if (message instanceof AppMessage) {
            map.put("contentType", 2);
            AppMessage appMessage = (AppMessage) message;
            map.put("appIdList", appMessage.getAppIdList());
            List phoneTypeList = null;
            List provinceList = null;
            List tagList = null;
            ArrayList customTags = new ArrayList();
            List<Map<String, Object>> conditions = appMessage.getConditions().get();
            for (Map<String, Object> c : conditions) {
                if (AppConditions.PHONE_TYPE.equals(c.get("key"))) {
                    phoneTypeList = (List) c.get("values");
                } else if (AppConditions.REGION.equals(c.get("key"))) {
                    provinceList = (List) c.get("values");
                } else if (AppConditions.TAG.equals(c.get("key"))) {
                    tagList = (List) c.get("values");
                } else {
                    HashMap<String, Object> customTag = new HashMap<>();
                    customTag.put("tag", c.get("key"));
                    customTag.put("codes", c.get("values"));
                    customTags.add(customTag);
                }
            }
            map.put("phoneTypeList", phoneTypeList);
            map.put("provinceList", provinceList);
            map.put("tagList", tagList);
            map.put("personaTags", customTags);
            map.put("speed", appMessage.getSpeed());
        }
        map.put("pushNetWorkType", message.getNetWorkType().getCode());
        String json = GtHttp.postJson(config.getHost(), map);
        LOG.debug(json);
        JsonNode rootNode = new ObjectMapper().readTree(json);
        if (rootNode.path("result").asText().equals("ok")) {
            return rootNode.path("contentId").asText();
        } else throw new IOException("获取contentId失败");
    }

    public boolean cancelContentId(String contentId) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "cancleContentIdAction");
        map.put("appkey", config.getAppKey());
        map.put("contentId", contentId);
        Map<String, Object> response = GtHttp.postBytes(config.getHost(), map, false, false);
        return "ok".equals(response.get("result"));
    }

    public Map<String, Object> pushToSingle(SingleMessage message, Target target) throws IOException {
        return pushToSingle(message, target, (String)null);
    }

    public Map<String, Object> pushToSingle(SingleMessage message, Target target, String requestId)
            throws IOException {
        if(requestId == null || "".equals(requestId.trim())) {
            requestId = UUID.randomUUID().toString();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "pushMessageToSingleAction");
        map.put("appkey", config.getAppKey());
        if(requestId != null) {
            map.put("requestId", requestId);
        }
        map.put("clientData", Base64.getEncoder().encodeToString(message.getData().getTransparent().toByteArray()));
        map.put("transmissionContent", message.getData().getTransmissionContent());
        map.put("isOffline", message.isOffline());
        map.put("offlineExpireTime", message.getOfflineExpireTime());
        map.put("appId", target.getAppId());
        map.put("clientId", target.getClientId());
        map.put("alias", target.getAlias());
        map.put("type", 2);
        map.put("pushType", message.getData().getPushType());
        map.put("pushNetWorkType", message.getNetWorkType().getCode());

        return GtHttp.postBytes(config.getHost(), map, false, false);
    }

    public Map<String, Object> pushToList(String contentId, List<Target> targets) throws IOException {
        HashMap<String, Object> map = new HashMap<>();

        map.put("action", "pushMessageToListAction");
        map.put("appkey", config.getAppKey());
        map.put("contentId", contentId);
        map.put("needDetails", false);
        map.put("async", false);
        map.put("needAliasDetails", false);

        ArrayList<String> clientIdList = new ArrayList<>();
        ArrayList<String> aliasList = new ArrayList<>();
        String appId = null;

        for (Target t : targets) {
            appId = t.getAppId();
            String cid = t.getClientId();
            String alias = t.getAlias();
            if (cid != null) clientIdList.add(cid);
            if (alias != null) aliasList.add(alias);
        }
        map.put("appId", appId);
        map.put("clientIdList", clientIdList);
        map.put("aliasList", aliasList);
        map.put("type", 2);

        return GtHttp.postBytes(config.getHost(), map, true, true);
    }

    public Map<String, Object> pushToApp(AppMessage message, String taskGroupName) throws IOException {
        String contentId = this.getContentId(message, taskGroupName);
        return pushToApp(contentId);
    }

    public Map<String, Object> pushToApp(String contentId) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", "pushMessageToAppAction");
        map.put("appkey", config.getAppKey());
        map.put("contentId", contentId);
        map.put("type", 2);
        return GtHttp.postBytes(config.getHost(), map, false, false);
    }

    public static String encryptByMd5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            return bytesToHex(md.digest());
        } catch (Exception ex) {
            return "";
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (byte b : bytes) {
            sb.append(digit[(b >>> 4) & 0X0F]);
            sb.append(digit[b & 0X0F]);
        }
        return sb.toString();
    }


}
