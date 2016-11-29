package com.autobon.order.vo;

import com.autobon.technician.entity.LocationStatus;
import com.autobon.technician.vo.LocationStatusShow;
import org.hibernate.engine.transaction.spi.LocalStatus;

import java.util.List;

/**
 * Created by wh on 2016/11/18.
 */
public class CoopTechnicianLocation {

    private String coopLongitude;
    private String coopLatitude;
    private List<LocationStatusShow> localStatuses;

    public String getCoopLongitude() {
        return coopLongitude;
    }

    public void setCoopLongitude(String coopLongitude) {
        this.coopLongitude = coopLongitude;
    }

    public String getCoopLatitude() {
        return coopLatitude;
    }

    public void setCoopLatitude(String coopLatitude) {
        this.coopLatitude = coopLatitude;

    }

    public List<LocationStatusShow> getLocalStatuses() {
        return localStatuses;
    }

    public void setLocalStatuses(List<LocationStatusShow> localStatuses) {
        this.localStatuses = localStatuses;
    }
}


