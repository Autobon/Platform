package com.autobon.getui.template;

import com.autobon.getui.payload.IPayload;
import com.autobon.getui.utils.GtReq;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by dave on 16/2/22.
 */
public class BaseTemplate implements ITemplate {
    public enum PushType {
        TransmissionMsg,
        LinkMsg,
        NotifyMsg,
        PopupAppDownLoad,
        NotyPopLoad;

        private PushType() {
        }
    }

    private String appId;
    private String appKey;
    private GtReq.PushInfo.Builder pushInfo = GtReq.PushInfo.newBuilder().setInvalidAPN(true).setInvalidMPN(true);
    private String duration;

    public BaseTemplate() {}

    public GtReq.Transparent getTransparent() {
        return GtReq.Transparent.newBuilder().setId("").setMessageId("").setTaskId("").setAction("pushmessage")
                .addAllActionChain(this.getActionChain()).setPushInfo(this.pushInfo).setAppId(this.appId)
                .setAppKey(this.appKey).addCondition(this.getDurCondition()).build();
    }

    private String getDurCondition() {
        return this.duration != null && this.duration.length() > 0 ? "duration=" + this.duration: "";
    }

    public String getTransmissionContent() {
        return "";
    }

    public String getPushType() {
        return "";
    }

    protected List<GtReq.ActionChain> getActionChain() {
        return null;
    }

    public GtReq.PushInfo getPushInfo() {
        return this.pushInfo.build();
    }

    public void setAPNInfo(IPayload apn) {
        if(apn != null) {
            String payload = apn.getPayload();
            if(payload != null && !payload.isEmpty()) {
                try {
                    int e = payload.getBytes("UTF-8").length;
                    if(e > 2048) {
                        throw new RuntimeException("APN payload size overlength (" + e + ">" + 2048 + ")");
                    }
                } catch (UnsupportedEncodingException var4) {
                    throw new RuntimeException(var4);
                }

                this.pushInfo.setApnJson(payload).setInvalidAPN(false);
            }
        }
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppkey(String appKey) {
        this.appKey = appKey;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(Date begin, Date end) {
        this.duration = begin.getTime() + "-" + end.getTime();
    }
}
