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

    @Column private Date yearMonth;

    @Column private int count;

    @Column private float sum;

    @Column private boolean transfered;

    @Column private Date transferAt;

    public Bill(int techId, Date yearMonth) {
        this.techId = techId;
        this.yearMonth = yearMonth;
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

    public Date getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(Date yearMonth) {
        this.yearMonth = yearMonth;
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

    public boolean isTransfered() {
        return transfered;
    }

    public void setTransfered(boolean transfered) {
        this.transfered = transfered;
    }

    public Date getTransferAt() {
        return transferAt;
    }

    public void setTransferAt(Date transferAt) {
        this.transferAt = transferAt;
    }
}
