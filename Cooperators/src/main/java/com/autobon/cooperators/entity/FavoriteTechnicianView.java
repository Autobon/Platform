package com.autobon.cooperators.entity;

import com.autobon.technician.entity.Technician;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wh on 2017/5/16.
 */
@Entity
@Table(name = "t_favorite_technician_view")
public class FavoriteTechnicianView {

    @Id
    private int id;
    private int cooperatorId;
    private Date createTime; //创建时间


    @OneToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCooperatorId() {
        return cooperatorId;
    }

    public void setCooperatorId(int cooperatorId) {
        this.cooperatorId = cooperatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }
}
