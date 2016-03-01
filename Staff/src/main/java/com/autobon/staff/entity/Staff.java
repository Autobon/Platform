package com.autobon.staff.entity;

import com.autobon.shared.Crypto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by liz on 2016/2/18.
 */
@Entity
@Table(name="t_staff")
public class Staff {
    private static Logger log = LoggerFactory.getLogger(Staff.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String username;

    @JsonIgnore
    @Column private String password;

    @Column private String name;

    @Column private String email;

    @Column private String phone;

    @Column private Date createAt;

    @Column private Date lastLoginAt;

    @Column private String lastLoginIp;   @Column private int status; // 0: 禁用， 1： 可用

    @JsonIgnore
    @Column private String role; // staff角色

    private static String Token = "Autobon~!&&2016=";

    public Staff() {
        this.status = 1;
        this.role = "";
        this.createAt = new Date();
    }

    public static String encryptPassword(String password) {
        return Crypto.encryptBySha1(password);
    }

    // 根据用户ID生成token
    public static String makeToken(int id) {
        return "staff:" + Crypto.encryptAesBase64(String.valueOf(id), Token);
    }

    // 从token返回用户Id
    public static int decodeToken(String token) {
        String[] arr = token.split(":");
        if (arr.length < 2 || !arr[0].equals("staff")) return 0;
        else token = arr[1];
        try {
            return Integer.parseInt(Crypto.decryptAesBase64(token, Token));
        } catch (Exception ex) {
            log.info("无效token: " + token);
        }
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
