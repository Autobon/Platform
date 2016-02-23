package com.autobon.getui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by dave on 16/2/22.
 */
public class GtHttp {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String postJson(String host, Map<String, Object> data) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(host).openConnection();
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (data.get("action") != null) {
            connection.setRequestProperty("Gt-Action", data.get("action").toString());
        }
        connection.connect();

        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        out.append(mapper.writeValueAsString(data));
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

    public static Map<String, Object> postBytes(String url, Map<String, Object> data,
            boolean postGZip, boolean acceptGZip) throws IOException {
        byte[] postData = mapper.writeValueAsBytes(data);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (data.get("action") != null) {
            connection.setRequestProperty("Gt-Action", data.get("action").toString());
        }
        if(postGZip) {
            postData = GZip.compress(postData);
            connection.setRequestProperty("Content-Encoding", "gzip");
            connection.setRequestProperty("Content-Length", String.valueOf(postData.length));
        }
        if(acceptGZip) {
            connection.setRequestProperty("Accept-Encoding", "gzip");
        }
        byte[] response = null;
        int tryTime = 0,
            maxHttpTryTime = 3;
        while(true) {
            try {
                response = executePost(connection, postData);
                break;
            } catch (IOException e) {
                ++tryTime;
                if(tryTime >= maxHttpTryTime) throw e;
            }
        }
        connection.disconnect();
        if (acceptGZip) response = GZip.decompress(response);
        return mapper.readValue(response, Map.class);
    }

    private static byte[] executePost(HttpURLConnection connection, byte[] postData) throws IOException {
        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(connection.getOutputStream());
            bos.write(postData);
            bos.flush();
        } finally {
            if(bos != null) bos.close();
        }

        int code = connection.getResponseCode();
        if (code == 200) {
            InputStream inputStream = connection.getInputStream();

            try {
                byte[] buffer = new byte[4096];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    baos.write(buffer, 0, bytesRead);
                }
                return baos.toByteArray();
            } finally {
                if (inputStream != null) inputStream.close();
            }
        } else {
            throw new HttpRetryException("Http Response Error.", code);
        }
    }
}
