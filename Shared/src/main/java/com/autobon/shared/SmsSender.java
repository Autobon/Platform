package com.autobon.shared;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dave on 16/2/15.
 */
@Component
public class SmsSender {
    private static Logger log = LoggerFactory.getLogger(SmsSender.class);

    @Value("${com.autobon.sms.appKey}")
    private String appKey;

    @Value("${com.autobon.sms.appSecret}")
    private String appSecret;

    @Value("${com.autobon.sms.templateId}")
    private String templateId;

    @Value("${com.autobon.sms.signature}")
    private String signature;

    @Value("${com.autobon.sms.gateway}")
    private String smsGateway;

    public boolean sendVerifyCode(String sim, String code) throws IOException {
        //POST数据
        HashMap<String, String> map = new HashMap<>();
        map.put("method", "alibaba.aliqin.fc.sms.num.send");
        map.put("app_key", appKey);
        map.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        map.put("format", "json");
        map.put("v", "2.0");
        map.put("sign_method", "md5");
        map.put("sms_type", "normal");
        map.put("sms_free_sign_name", signature);
        map.put("sms_param", "{\"code\":\"" + code + "\", \"product\":\"车邻邦\"}");
        map.put("sms_template_code", templateId);
        map.put("rec_num", sim);
        map.put("sign", computeSignature(map, appSecret));
        byte[] content = buildQueryString(map, "UTF-8").getBytes();

        URL url = new URL("http://gw.api.taobao.com/router/rest");
        HttpURLConnection connection = null;
        String response;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            connection.connect();
            try (OutputStream out = connection.getOutputStream()) {
                out.write(content);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();
            }

        } finally {
            if (connection != null) connection.disconnect();
        }
        JsonNode rootNode = new ObjectMapper().readTree(response);
        String status = rootNode.path("alibaba_aliqin_fc_sms_num_send_response")
                        .path("result").path("success").asText();
        boolean result = "true".equals(status);
        if (!result) log.error("向" + sim + "发送验证短信失败: " + response);
        return result;
    }

    public static String computeSignature(Map<String, String> params, String appSecret) throws IOException {
        String[] sortedKeys = params.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);

        StringBuilder queryString = new StringBuilder(appSecret);
        for (String key : sortedKeys) {
            queryString.append(key).append(params.get(key));
        }
        queryString.append(appSecret);
        return Crypto.encryptByMd5(queryString.toString()).toUpperCase();
    }

    public static String buildQueryString(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) return "";

        StringBuilder query = new StringBuilder();
        boolean firstParam = true;
        for (Map.Entry<String, String> entry: params.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (!"".equals(name) && value != null && !"".equals(value)) {
                if (!firstParam) {
                    query.append("&");
                } else {
                    firstParam = false;
                }

                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }
        return query.toString();
    }

    public boolean send(String sim, String content) throws IOException {
        //建立连接
        URL url = new URL(smsGateway);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        connection.connect();
        //POST请求
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        HashMap<String, String> m = new HashMap<>();
        m.put("sim", sim);
        m.put("content", content);
        String requestJson = new ObjectMapper().writeValueAsString(m);
        log.info(requestJson);
        out.append(requestJson);
        out.flush();
        out.close();
        //读取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String line;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        log.info(sb.toString());
        reader.close();
        // 断开连接
        connection.disconnect();
        if (!sb.equals("")) {
            JsonNode rootNode = new ObjectMapper().readTree(sb.toString());
            String status = rootNode.path("status").asText();
            if (status != null && status.equals("success")) {
                return true;
            }
        }
        return false;
    }
}
