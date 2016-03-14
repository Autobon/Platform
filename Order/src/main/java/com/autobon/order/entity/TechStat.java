package com.autobon.order.entity;

import javax.persistence.*;

/**
 * Created by dave on 16/3/14.
 */
@Entity
@Table(name = "t_tech_stat")
public class TechStat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private int techId;

    @Column private float starRate; // 星级

    @Column private float balance; // 帐户余额, 未支付工单总额

    @Column private int unpaidOrders; // 未付账订单条数

    @Column private int totalOrders; // 订单总数

    @Column private int commentCount; // 评论条数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public float getStarRate() {
        return starRate;
    }

    public void setStarRate(float starRate) {
        this.starRate = starRate;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getUnpaidOrders() {
        return unpaidOrders;
    }

    public void setUnpaidOrders(int unpaidOrders) {
        this.unpaidOrders = unpaidOrders;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
