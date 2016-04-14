package com.autobon.order.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lu on 2016/3/21.
 */
@Entity
@Table(name="sys_stat")
public class SysStat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column private int id;

    @Column private int newOrderCount; //新增订单数

    @Column private int finishedOrderCount; //完成订单数

    @Column private int newCoopCount; //新增商户数

    @Column private int verifiedCoopCount; //认证商户数

    @Column private int newTechCount; //新增技师数

    @Column private int verifiedTechCount; // 认证技师数

    @Column private Date statTime; //统计时间

    @Column private int statType; //统计类型 (1-按天统计  2-按月统计)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewOrderCount() {
        return newOrderCount;
    }

    public void setNewOrderCount(int newOrderCount) {
        this.newOrderCount = newOrderCount;
    }

    public int getFinishedOrderCount() {
        return finishedOrderCount;
    }

    public void setFinishedOrderCount(int finishedOrderCount) {
        this.finishedOrderCount = finishedOrderCount;
    }

    public int getNewCoopCount() {
        return newCoopCount;
    }

    public void setNewCoopCount(int newCoopCount) {
        this.newCoopCount = newCoopCount;
    }

    public int getVerifiedCoopCount() {
        return verifiedCoopCount;
    }

    public void setVerifiedCoopCount(int verifiedCoopCount) {
        this.verifiedCoopCount = verifiedCoopCount;
    }

    public int getNewTechCount() {
        return newTechCount;
    }

    public void setNewTechCount(int newTechCount) {
        this.newTechCount = newTechCount;
    }

    public int getVerifiedTechCount() {
        return verifiedTechCount;
    }

    public void setVerifiedTechCount(int verifiedTechCount) {
        this.verifiedTechCount = verifiedTechCount;
    }

    public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }

    public int getStatType() {
        return statType;
    }

    public void setStatType(int statType) {
        this.statType = statType;
    }
}
