package com.autobon.getui.utils;

/**
 * Created by dave on 16/2/22.
 */
public class GtConfig {
    private String host;
    private String appId;
    private String appKey;
    private String appSecret;
    private String masterSecret;
    private String sdkVersion = "4.0.1.0";
    private int connectionTimeout = 60000;

    public GtConfig(String host, String appId, String appKey,
                    String appSecret, String masterSecret) {
        this.host = host;
        this.appId = appId;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.masterSecret = masterSecret;
    }

    public GtConfig(String host, String appId, String appKey,
                    String appSecret, String masterSecret, String sdkVersion) {
        this(host, appId, appKey, appSecret, masterSecret);
        this.sdkVersion = sdkVersion;
    }

    public String getHost() {
        return host;
    }

    public GtConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public GtConfig setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getMasterSecret() {
        return masterSecret;
    }

    public GtConfig setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
        return this;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public GtConfig setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }
}
