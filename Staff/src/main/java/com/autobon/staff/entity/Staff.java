package com.autobon.staff.entity;

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

    @Column private String password;

    @Column private String email;

    @Column private String phone;

    @Column private Date createAt;

    @Column private Date lastLoginAt;

    @Column private String lastLoginIp;

    @Column private int status; // 0: 禁用， 1： 可用

    @Column private String role; // staff角色

    


}
