package com.autobon.getui;

import com.autobon.getui.message.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dave on 16/2/22.
 */
public class GtPush {
    private GtConfig config;

    public GtPush(GtConfig config) {
        this.config = config;
    }

    public boolean connect() throws IOException {
        Long timestamp = new Date().getTime();
        String sign = encryptByMd5(config.getAppKey() + timestamp + config.getMasterSecret());
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "connect");
        map.put("appkey", config.getAppKey());
        map.put("timeStamp", timestamp.toString());
        map.put("sign", sign);
        String json = GtHttp.postJson(config.getHost(), map);
        return new ObjectMapper().readTree(json).path("result").asText().equals("success");
    }

    public boolean close() throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "close");
        map.put("appkey", config.getAppKey());
        String json = GtHttp.postJson(config.getHost(), map);
        return new ObjectMapper().readTree(json).path("result").asText().equals("success");
    }

    public int getContentId(Message message, String taskGroupName) {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "getContentIdAction");
        map.put("appkey", config.getAppKey());
        return 0;
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
