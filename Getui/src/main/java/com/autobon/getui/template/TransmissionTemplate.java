package com.autobon.getui.template;

import com.autobon.getui.utils.GtReq;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 16/2/23.
 * 透传消息模板
 * http://docs.getui.com/pages/viewpage.action?pageId=1213592
 */
public class TransmissionTemplate extends BaseTemplate {
    private int transmissionType;
    private String transmissionContent;

    protected List<GtReq.ActionChain> getActionChain() {
        ArrayList<GtReq.ActionChain> chains = new ArrayList<>();

        chains.add(GtReq.ActionChain.newBuilder().setActionId(1)
                .setType(GtReq.ActionChain.Type._goto).setNext(10030).build());
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
        return PushType.TransmissionMsg.toString();
    }

    public void setTransmissionType(int transmissionType) {
        this.transmissionType = transmissionType;
    }

    public void setTransmissionContent(String transmissionContent) {
        this.transmissionContent = transmissionContent;
    }
}
