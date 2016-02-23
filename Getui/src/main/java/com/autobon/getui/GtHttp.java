package com.autobon.getui;

import com.autobon.getui.message.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by dave on 16/2/22.
 */
public class GtHttp {
    public static String postJson(String host, HashMap<String, String> data) throws IOException {
        URL url = new URL(host);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (data.get("action") != null) {
            connection.setRequestProperty("Gt-Action", data.get("action"));
        }
        connection.connect();

        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        out.append(new ObjectMapper().writeValueAsString(data));
        out.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        connection.disconnect();
        return sb.toString();
    }

    public static String createPostBody(Message message, Target target, String requestId, String appKey) {
        HashMap<String, String> map = new HashMap<>();
        map.put("requestId", requestId);
        map.put("action", "");

        return "";
    }
}
