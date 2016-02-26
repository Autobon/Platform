package com.autobon.getui.payload;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dave on 16/2/23.
 */
public class APNPayload implements IPayload {
    public static final int PAYLOAD_MAX_BYTES = 2048;
    private static final ObjectMapper JSON = new ObjectMapper();
    private AlertMsg alertMsg = null;
    private int badge = -1;
    private String sound = "default";
    private int contentAvailable = 0;
    private String category;
    private HashMap<String, Object> customMsg = new HashMap<>();

    public APNPayload() {}

    @Override
    public String getPayload() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.alertMsg != null) map.put("alert", this.alertMsg.getAlertMsg());
        if (this.badge >= 0) map.put("badge", this.badge);
        map.put("sound", this.sound);
        if (this.contentAvailable > 0) map.put("content-available", this.contentAvailable);
        if (this.category != null && this.category.length() > 0) map.put("category" ,this.category);

        HashMap<String, Object> wrapper = new HashMap<>();
        for (String key : customMsg.keySet()) {
            wrapper.put(key, customMsg.get(key));
        }
        wrapper.put("aps", map);
        try {
            return JSON.writeValueAsString(wrapper);
        } catch (IOException e) {
            throw new RuntimeException("生成JSON字符串失败");
        }

    }

    public void addCustomMsg(String key, Object value) {
        if (key != null && key.length() > 0 && value != null) this.customMsg.put(key, value);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBadge(int badge) {
        if (badge >= 0) this.badge = badge;
    }

    public void setSound(String sound) {
        if (sound != null && !sound.isEmpty()) this.sound = sound;
        else this.sound = "default";
    }

    public void setContentAvailable(int contentAvailable) {
        if (contentAvailable > 0) this.contentAvailable = contentAvailable;
    }

    public void setAlertMsg(AlertMsg alertMsg) {
        this.alertMsg = alertMsg;
    }

    public interface AlertMsg {
        public Object getAlertMsg();
    }

    public static class DictionaryAlertMsg implements AlertMsg {
        private String title;
        private String body;
        private String titleLocKey;
        private List<String> titleLocArgs = new ArrayList<>();
        private String actionLocKey;
        private String locKey;
        private List<String> locArgs = new ArrayList<>();
        private String launchImage;

        public DictionaryAlertMsg() {}

        public void setTitle(String title) {
            this.title = title;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public void setTitleLocKey(String titleLocKey) {
            this.titleLocKey = titleLocKey;
        }

        public void addTitleLocArg(String titleLocArg) {
            if(titleLocArg != null && titleLocArg.length() > 0) {
                this.titleLocArgs.add(titleLocArg);
            }
        }

        public void setActionLocKey(String actionLocKey) {
            this.actionLocKey = actionLocKey;
        }

        public void setLocKey(String locKey) {
            this.locKey = locKey;
        }

        public void addLocArg(String locArg) {
            if(locArg != null && locArg.length() > 0) {
                this.locArgs.add(locArg);
            }
        }

        public void setLaunchImage(String launchImage) {
            this.launchImage = launchImage;
        }

        @Override
        public Object getAlertMsg() {
            HashMap<String, Object> alertMap = new HashMap();
            if(this.title != null && this.title.length() > 0) alertMap.put("title", this.title);
            if(this.body != null && this.body.length() > 0) alertMap.put("body", this.body);
            if(this.titleLocKey != null && this.titleLocKey.length() > 0) alertMap.put("title-loc-key", this.titleLocKey);
            if(this.titleLocArgs.size() > 0) alertMap.put("title-loc-args", this.titleLocArgs);
            if(this.actionLocKey != null && this.actionLocKey.length() > 0) alertMap.put("action-loc-key", this.actionLocKey);
            if(this.locKey != null && this.locKey.length() > 0) alertMap.put("loc-key", this.locKey);
            if(this.locArgs.size() > 0) alertMap.put("loc-args", this.locArgs);
            if(this.launchImage != null && this.launchImage.length() > 0) alertMap.put("launch-image", this.launchImage);
            return alertMap;
        }
    }

    public static class SimpleAlertMsg implements AlertMsg {
        private String alertMsg;

        public SimpleAlertMsg(String alertMsg) {
            this.alertMsg = alertMsg;
        }

        @Override
        public Object getAlertMsg() {
            return this.alertMsg;
        }
    }
}
