package com.autobon.getui.message;

import com.autobon.getui.template.ITemplate;

/**
 * Created by dave on 16/2/22.
 */
public class Message {
    public enum NetworkType {
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
    private int offlineExpireTime = 60*60*1000; //过多久该消息离线失效（单位毫秒） 支持1-72小时，默认1小时
    private NetworkType netWorkType = NetworkType.ALL;
    private ITemplate data;

    public Message() {}

    public Message(boolean isOffline, int offlineExpireTime,
                   NetworkType pushNetWorkType, ITemplate data) {
        this.offline = isOffline;
        this.offlineExpireTime = offlineExpireTime;
        this.netWorkType = pushNetWorkType;
        this.data = data;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public int getOfflineExpireTime() {
        return offlineExpireTime;
    }

    public void setOfflineExpireTime(int offlineExpireTime) {
        this.offlineExpireTime = offlineExpireTime;
    }

    public NetworkType getNetWorkType() {
        return netWorkType;
    }

    public void setNetWorkType(NetworkType netWorkType) {
        this.netWorkType = netWorkType;
    }

    public ITemplate getData() {
        return data;
    }

    public void setData(ITemplate data) {
        this.data = data;
    }
}
