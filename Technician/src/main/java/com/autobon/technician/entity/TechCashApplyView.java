package com.autobon.technician.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/21.
 * 技师提现申请  视图
 */
@Entity
@Table(name = "t_tech_cash_apply_view")
public class TechCashApplyView {
    private static Logger log = LoggerFactory.getLogger(Technician.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "apply_date")
    private Date applyDate;              //申请日期

    @Column(name = "apply_money")
    private BigDecimal applyMoney;         //申请金额

    @Column(name = "tech_id")
    private int techId;                  //技师ID

    @Column(name = "tech_name")
    private String techName;                  //技师姓名

    @Column(name = "pay_date")
    private Date payDate;           //支付日期

    @Column(name = "payment")
    private BigDecimal payment;             //支付金额

    @Column(name = "not_pay")
    private BigDecimal notPay;          //未支付金额

    @Column(name = "state")
    private int state;            //支付状态  0已申请，1部分扣款，2已扣款, 3已被取消

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public BigDecimal getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getNotPay() {
        return notPay;
    }

    public void setNotPay(BigDecimal notPay) {
        this.notPay = notPay;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }
}
