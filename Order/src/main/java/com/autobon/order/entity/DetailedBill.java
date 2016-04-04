package com.autobon.order.entity;

import com.autobon.technician.entity.Technician;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dave on 16/4/1.
 */
@Entity
@Table(name = "v_bill")
public class DetailedBill {
    @Id private int id;

    @Column private Date billMonth; // 月帐所属的年,月

    @Column private int count;

    @Column private float sum;

    @Column private boolean paid;

    @Column private Date payAt;

    @ManyToOne
    @JoinColumn(name = "tech_id")
    private Technician technician;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getPayAt() {
        return payAt;
    }

    public void setPayAt(Date payAt) {
        this.payAt = payAt;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }
}
