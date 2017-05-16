package com.autobon.cooperators.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wh on 2017/5/16.
 */

@Entity
@Table(name = "t_favorite_cooperator")
public class FavoriteCooperator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int technicianId;
    private int cooperatorId;
    private Date createTime; //创建时间

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
}
