package com.autobon.technician.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/9/13.
 */
@Entity
@Table(name = "t_tech_finance")
public class TechFinance {
    private static Logger log = LoggerFactory.getLogger(Technician.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "tech_id")
    private int techId;

    @Column(name = "sum_income")
    private BigDecimal sumIncome;

    @Column(name = "sum_cash")
    private BigDecimal sumCash;

    @Column(name = "not_cash")
    private BigDecimal notCash;

    @Column(name = "already_apply")
    private BigDecimal alreadyApply;

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

    public BigDecimal getSumIncome() {
        return sumIncome;
    }

    public void setSumIncome(BigDecimal sumIncome) {
        this.sumIncome = sumIncome;
    }

    public BigDecimal getSumCash() {
        return sumCash;
    }

    public void setSumCash(BigDecimal sumCash) {
        this.sumCash = sumCash;
    }

    public BigDecimal getNotCash() {
        return notCash;
    }

    public void setNotCash(BigDecimal notCash) {
        this.notCash = notCash;
    }

    public BigDecimal getAlreadyApply() {
        return alreadyApply;
    }

    public void setAlreadyApply(BigDecimal alreadyApply) {
        this.alreadyApply = alreadyApply;
    }
}
