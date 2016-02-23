package com.autobon.getui.template;

import com.autobon.getui.utils.GtReq;

/**
 * Created by dave on 16/2/23.
 */
public interface ITemplate {
    GtReq.Transparent getTransparent();

    String getTransmissionContent();

    String getPushType();

    GtReq.PushInfo getPushInfo();
}
