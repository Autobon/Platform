package com.autobon.getui.template;

import com.autobon.getui.utils.GtReq;
import com.autobon.getui.utils.PushType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 16/2/23.
 */
public class NotificationTemplate extends BaseTemplate {
    private String text;
    private String title;
    private String logo = "";
    private String logoUrl = "";
    private int transmissionType;
    private String transmissionContent;
    private boolean isRing = true;
    private boolean isVibrate = true;
    private boolean isClearable = true;

    protected List<GtReq.ActionChain> getActionChain() {
        ArrayList<GtReq.ActionChain> chains = new ArrayList<>();
        chains.add(GtReq.ActionChain.newBuilder().setActionId(1)
                .setType(GtReq.ActionChain.Type._goto).setNext(10000).build());
        chains.add(GtReq.ActionChain.newBuilder().setActionId(10000).setType(GtReq.ActionChain.Type.notification)
                .setTitle(this.title).setText(this.text).setLogo(this.logo)
                .setLogoURL(this.logoUrl).setRing(this.isRing).setClearable(this.isClearable)
                .setBuzz(this.isVibrate).setNext(10010).build());
        chains.add(GtReq.ActionChain.newBuilder().setActionId(10010).setType(GtReq.ActionChain.Type._goto)
                .setNext(10030).build());
        GtReq.AppStartUp appStartUp = GtReq.AppStartUp.newBuilder().setAndroid("").setSymbia("").setIos("").build();
        chains.add(GtReq.ActionChain.newBuilder().setActionId(10030).setType(GtReq.ActionChain.Type.startapp)
                .setAppid("").setAutostart(1 == this.transmissionType).setAppstartupid(appStartUp)
                .setFailedAction(100).setNext(100).build());
        chains.add(GtReq.ActionChain.newBuilder().setActionId(100).setType(GtReq.ActionChain.Type.eoa).build());

        return chains;
    }

    public String getTransmissionContent() {
        return this.transmissionContent;
    }

    public String getPushType() {
        return PushType.NotifyMsg.toString();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setTransmissionType(int transmissionType) {
        this.transmissionType = transmissionType;
    }

    public void setIsRing(boolean isRing) {
        this.isRing = isRing;
    }

    public void setIsVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
    }

    public void setIsClearable(boolean isClearable) {
        this.isClearable = isClearable;
    }

    public void setTransmissionContent(String transmissionContent) {
        this.transmissionContent = transmissionContent;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
