package com.autobon.technician.entity;

import com.autobon.technician.vo.TechCashApplyShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/21.
 * 技师提现申请
 */
@Entity
@Table(name = "t_tech_cash_apply")
public class TechCashApply {
    private static Logger log = LoggerFactory.getLogger(Technician.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "apply_date")
    private Date applyDate;              //申请日期

    @Column(name = "apply_money")
    private BigDecimal applyMoney;         //申请金额

    @Column(name = "tech_id")
    private Integer techId;                  //技师ID

    @Column(name = "pay_date")
    private Date payDate;           //支付日期

    @Column(name = "payment")
    private BigDecimal payment;             //支付金额

    @Column(name = "not_pay")
    private BigDecimal notPay;          //未支付金额

    @Column(name = "state")
    private Integer state;            //支付状态  0已申请，1部分扣款，2已扣款, 3已被取消

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getTechId() {
        return techId;
    }

    public void setTechId(Integer techId) {
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public TechCashApply(TechCashApplyShow techCashApplyShow) {
        this.applyDate = techCashApplyShow.getApplyDate();
        this.applyMoney = techCashApplyShow.getApplyMoney();
        this.techId = techCashApplyShow.getTechId();
        this.payDate = techCashApplyShow.getPayDate();
        this.payment = techCashApplyShow.getPayment();
        this.notPay = techCashApplyShow.getNotPay();
        this.state = techCashApplyShow.getState();
    }

    public TechCashApply() {
    }
}
