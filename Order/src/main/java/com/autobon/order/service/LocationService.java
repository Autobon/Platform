package com.autobon.order.service;

import com.autobon.order.Util.GpsData;
import com.autobon.order.Util.GpsTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuh on 2016/2/23.
 */
@Service
public class LocationService {

    @Autowired
    GpsTool gpsTool;

    public GpsData getLocation(GpsData gpsData) {
        gpsData = gpsTool.convertToBaiDuGps(gpsData);
        return gpsData;
    }
}
