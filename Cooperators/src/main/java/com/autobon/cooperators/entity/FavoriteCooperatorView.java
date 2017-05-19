package com.autobon.cooperators.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wh on 2017/5/16.
 */
@Entity
@Table(name = "t_favorite_cooperator_view")
public class FavoriteCooperatorView {

    @Id
    private int id;
    private int technicianId;
    private Date createTime; //创建时间

    @OneToOne
    @JoinColumn(name = "cooperator_id")
    private Cooperator cooperator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Cooperator getCooperator() {
        return cooperator;
    }

    public void setCooperator(Cooperator cooperator) {
        this.cooperator = cooperator;
    }
}
