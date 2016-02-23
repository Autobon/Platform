package com.autobon.order.Util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yuh on 2016/2/16.
 */
@Component
public class GpsTool {
    double pi = 3.14159265358979324;

    @Value("${com.autobon.baiduKey}")
    private String baiduKey;

    private Logger _logger= LoggerFactory.getLogger(GpsTool.class);


    /**
     * WGS84转百度坐标系 调用百度转换api
     * @param gpsData
     * @return 转换后的数据实体
     */
    public  GpsData convertToBaiDuGps(GpsData gpsData)
    {

        return getDataFromBaidu(gpsData);
    }


    private boolean  outOfChina(double lat, double lon)
    {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
    private double transformLat(double x, double y)
    {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }
    private double transformLon(double x,double y){
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
    public GpsData getDataFromBaidu(GpsData gpsData){
        StringBuilder sb=new StringBuilder();
        sb.append("http://api.map.baidu.com/geoconv/v1/?");
        sb.append("&coords=").append(gpsData.getLongitude()).append(",").append(gpsData.getLatitude());
        sb.append("&from=1&to=5");
        sb.append("&ak=").append(baiduKey);
        String ADD_URL=sb.toString();

        BufferedReader reader = null;
        try {
            //建立连接
            URL url = new URL(ADD_URL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.connect();
            //POST请求
//            OutputStreamWriter out = new  OutputStreamWriter(connection.getOutputStream(), "UTF-8");

            //读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer re = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                re.append(lines);
            }
            String result=re.toString();
            JSONObject json = JSON.parseObject(result);
            String status=json.get("status").toString();
            if(status.equals("0")){
                JSONArray aa=json.getJSONArray("result");
                JSONObject gps =JSON.parseObject(aa.get(0).toString());
                String x=gps.get("x").toString();
                String y=gps.get("y").toString();
                gpsData.setLongitude((Double.parseDouble(x)));
                gpsData.setLatitude(Double.parseDouble(y));
            }
            reader.close();
            // 断开连接
            connection.disconnect();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(reader!=null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return gpsData;//success返回1 faild返回0
    }
}
