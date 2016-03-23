package com.autobon.cooperators.entity;

import com.autobon.shared.Crypto;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yuh on 2016/3/23.
 */

@Entity
@Table(name = "t_coop_account")
public class CoopAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column private int id;

    @Column
    private int cooperatorId;

    @Column
    private boolean isMain;

    @Column
    private boolean fired;

    @Column
    private String phone;

    @Column
    private String name;

    @Column
    private boolean gender;

    @Column
    private String password;

    @Column
    private Date lastLoginTime; //上次登录时间

    @Column
    private String lastLoginIp; //上次登录IP

    @Column
    private Date createTime; //注册时间

    public static String encryptPassword(String password) {
        return Crypto.encryptBySha1(password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCooperatorId() {
        return cooperatorId;
    }

    public void setCooperatorId(int cooperatorId) {
        this.cooperatorId = cooperatorId;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public boolean isFired() {
        return fired;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
