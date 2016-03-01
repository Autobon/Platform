package com.autobon.order.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yuh on 2016/3/1.
 */
@Entity
@Table(name="t_location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Column(name = "get_time", nullable = false, insertable = true, updatable = true)
    private Date getTime;

    @Column(name = "rtposition_lon", nullable = true, insertable = true, updatable = true)
    private String rtpositionLon;

    @Column(name = "rtposition_lat", nullable = true, insertable = true, updatable = true)
    private String rtpositionLat;

    @Column(name = "technician_id", nullable = true, insertable = true, updatable = true)
    private int technicianId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    public String getRtpositionLon() {
        return rtpositionLon;
    }

    public void setRtpositionLon(String rtpositionLon) {
        this.rtpositionLon = rtpositionLon;
    }

    public String getRtpositionLat() {
        return rtpositionLat;
    }

    public void setRtpositionLat(String rtpositionLat) {
        this.rtpositionLat = rtpositionLat;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }
}
