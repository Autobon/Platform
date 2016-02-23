package com.autobon.getui;

/**
 * Created by dave on 16/2/22.
 */
public class GtConfig {
    private String host;
    private String appKey;
    private String masterSecret;
    private String sdkVersion = "4.0.1.0";
    private int connectionTimeout = 60000;

    public GtConfig(String host, String appKey, String masterSecret) {
        this.host = host;
        this.appKey = appKey;
        this.masterSecret = masterSecret;
    }

    public GtConfig(String host, String appKey, String masterSecret, String sdkVersion) {
        this(host, appKey, masterSecret);
        this.sdkVersion = sdkVersion;
    }

    public String getAppKey() {
        return appKey;
    }

    public GtConfig setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getHost() {
        return host;
    }

    public GtConfig setHost(String host) {
        this.host = host;
        return this;
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
