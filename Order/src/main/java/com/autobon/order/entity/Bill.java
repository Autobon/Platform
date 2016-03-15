package com.autobon.order.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 技师每月完成施工订单结算帐单
 * Created by dave on 16/3/11.
 */
@Entity
@Table(name = "t_bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private int techId;

    @Column private Date billMonth; // 月帐所属的年,月

    @Column private int count;

    @Column private float sum;

    @Column private boolean payed;

    @Column private Date payAt;

    public Bill() {}

    public Bill(int techId, Date yearMonth) {
        this.techId = techId;
        this.billMonth = yearMonth;
    }

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

    public Date getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(Date billMonth) {
        this.billMonth = billMonth;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public Date getPayAt() {
        return payAt;
    }

    public void setPayAt(Date payAt) {
        this.payAt = payAt;
    }
}
