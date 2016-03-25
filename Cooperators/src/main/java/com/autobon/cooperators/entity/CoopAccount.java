package com.autobon.cooperators.entity;

import com.autobon.shared.Crypto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by yuh on 2016/3/23.
 */

@Entity
@Table(name = "t_coop_account")
public class CoopAccount implements UserDetails {
    private static Logger log = LoggerFactory.getLogger(CoopAccount.class);
    private static String Token = "Autobon~!@#ABCD=";

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
    private String shortname; //企业简称

    @Column
    private String phone;

    @Column
    private String name;

    @Column
    private boolean gender;

    @JsonIgnore
    @Column
    private String password;

    @Column
    private Date lastLoginTime; //上次登录时间

    @Column
    private String lastLoginIp; //上次登录IP

    @Column
    private Date createTime; //注册时间

    @Column
    private String pushId; // 个推客户端ID, 由手机端更新

    @Column private int statusCode; //状态 0-未审核 1-审核成功 2-审核失败 3-账号禁用

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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
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

    public static String makeToken(int id) {
        return "cooperator:" + Crypto.encryptAesBase64(String.valueOf(id), Token);
    }

    public static int decodeToken(String token) {
        String[] arr = token.split(":");
        if (arr.length < 2 || !arr[0].equals("cooperator")) return 0;
        else token = arr[1];
        try {
            return Integer.parseInt(Crypto.decryptAesBase64(token, Token));
        } catch (Exception ex) {
            log.info("无效token: " + token);
        }
        return 0;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "COOPERATOR";
            }
        });
        return list;
    }

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return phone;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return this.statusCode != 3;
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

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
