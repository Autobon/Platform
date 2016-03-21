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

    @Column private int orderCount; //新增订单数

    @Column private int coopCount; //新增商户数

    @Column private int techCount; //新增技师数

    @Column private Date statTime; //统计时间

    @Column private String statType; //统计类型 (day-按天统计  month-按月统计)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getCoopCount() {
        return coopCount;
    }

    public void setCoopCount(int coopCount) {
        this.coopCount = coopCount;
    }

    public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }

    public int getTechCount() {
        return techCount;
    }

    public void setTechCount(int techCount) {
        this.techCount = techCount;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }
}
