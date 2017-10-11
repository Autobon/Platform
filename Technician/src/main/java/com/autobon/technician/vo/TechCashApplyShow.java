package com.autobon.technician.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/21.
 * 技师提现申请
 */
public class TechCashApplyShow {


    private Date applyDate;              //申请日期
    private BigDecimal applyMoney;         //申请金额
    private Integer techId;                  //技师ID
    private Integer orderId;                //订单ID
    private Date payDate;           //支付日期
    private BigDecimal payment;             //支付金额
    private BigDecimal notPay;          //未支付金额
    private Integer state;            //支付状态  0已申请，1部分扣款，2已扣款

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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public TechCashApplyShow(Date applyDate, BigDecimal applyMoney, Integer techId, Integer orderId, Date payDate, BigDecimal payment, BigDecimal notPay, Integer state) {
        this.applyDate = applyDate;
        this.applyMoney = applyMoney;
        this.techId = techId;
        this.orderId = orderId;
        this.payDate = payDate;
        this.payment = payment;
        this.notPay = notPay;
        this.state = state;
    }

    public TechCashApplyShow() {
    }
}
