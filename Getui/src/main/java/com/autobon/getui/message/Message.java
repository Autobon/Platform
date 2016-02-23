package com.autobon.getui.message;

/**
 * Created by dave on 16/2/22.
 */
public class Message {
    enum NetworkType {
        ALL(0), WIFI(1), SIM(2);

        private int code;
        NetworkType(int i) {
            this.code = i;
        }
        public int getCode() {
            return this.code;
        }
        public NetworkType getType(int i) {
            for (NetworkType t : NetworkType.values()) {
                if (t.getCode() == i) return t;
            }
            return null;
        }
    }
    private boolean offline = true;
    private int offlineExpireSecond = 60*60; //过多久该消息离线失效（单位秒） 支持1-72小时*3600秒，默认1小时 60*60
    private NetworkType pushNetWorkType = NetworkType.ALL;
    private String jsonData;

    public Message() {}

    public Message(boolean isOffline, int offlineExpireSecond, NetworkType pushNetWorkType, String jsonData) {
        this.offline = isOffline;
        this.offlineExpireSecond = offlineExpireSecond;
        this.pushNetWorkType = pushNetWorkType;
        this.jsonData = jsonData;
    }

    public boolean isOffline() {
        return offline;
    }

    public Message setOffline(boolean offline) {
        this.offline = offline;
        return this;
    }

    public int getOfflineExpireSecond() {
        return offlineExpireSecond;
    }

    public Message setOfflineExpireSecond(int offlineExpireSecond) {
        this.offlineExpireSecond = offlineExpireSecond;
        return this;
    }

    public NetworkType getPushNetWorkType() {
        return pushNetWorkType;
    }

    public Message setPushNetWorkType(NetworkType pushNetWorkType) {
        this.pushNetWorkType = pushNetWorkType;
        return this;
    }

    public String getJsonData() {
        return jsonData;
    }

    public Message setJsonData(String jsonData) {
        this.jsonData = jsonData;
        return this;
    }
}
