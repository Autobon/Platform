package com.autobon.getui.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dave on 16/2/23.
 */
public class AppConditions {
    public static final String PHONE_TYPE = "phoneType";
    public static final String REGION = "region";
    public static final String TAG = "tag";
    private List<Map<String, Object>> condition = new ArrayList();

    public AppConditions() {
    }

    public AppConditions addCondition(String key, List<String> values, int optType) {
        HashMap item = new HashMap();
        item.put("key", key);
        item.put("values", values);
        item.put("optType", Integer.valueOf(optType));
        this.condition.add(item);
        return this;
    }

    public AppConditions addCondition(String key, List<String> values) {
        return this.addCondition(key, values, 0);
    }

    public List<Map<String, Object>> get() {
        return this.condition;
    }
}
