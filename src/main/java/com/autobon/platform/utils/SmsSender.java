package com.autobon.platform.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by dave on 16/2/15.
 */
@Component
public class SmsSender {
    private static Logger log = LoggerFactory.getLogger(SmsSender.class);

    @Value("${com.autobon.sms.gateway}")
    private String smsGateway;

    public boolean send(String sim, String content) throws IOException {
        try {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
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
        } catch (IOException e) {
            throw e;
        }
        return false;
    }
}
