package com.autobon.staff.entity;

import com.autobon.share.Crypto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by liz on 2016/2/18.
 */
@Entity
@Table(name="t_staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String username;

    @JsonIgnore
    @Column private String password;

    @Column private String email;

    @Column private String phone;

    @Column private Date createAt;

    @Column private Date lastLoginAt;

    @Column private String lastLoginIp;

    @Column private int status; // 0: 禁用， 1： 可用

    @JsonIgnore
    @Column private String role; // staff角色

    public Staff() {
        this.status = 1;
        this.role = "";
        this.createAt = new Date();
    }

    public static String encryptPassword(String password) {
        return Crypto.encryptBySha1(password);
    }


}
