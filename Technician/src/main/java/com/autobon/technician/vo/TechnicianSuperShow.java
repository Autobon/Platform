package com.autobon.technician.vo;

import com.autobon.technician.entity.Technician;

/**
 * Created by wh on 2016/12/7.
 */
public class TechnicianSuperShow {

    private Technician technician;
    private String starRate = "4.5" ;
    private String balance = "234.2";
    private String unpaidOrders = "6";
    private String totalOrders = "12";

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public String getStarRate() {
        return starRate;
    }

    public void setStarRate(String starRate) {
        this.starRate = starRate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUnpaidOrders() {
        return unpaidOrders;
    }

    public void setUnpaidOrders(String unpaidOrders) {
        this.unpaidOrders = unpaidOrders;
    }

    public String getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        this.totalOrders = totalOrders;
    }
}