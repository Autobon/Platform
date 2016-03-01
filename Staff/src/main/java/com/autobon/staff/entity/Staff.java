package com.autobon.staff.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by liz on 2016/2/18.
 */
@Entity
@Table(name="t_staff")
public class Staff {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Date createAt;
    private Date lastLoginAt;
    private String lastLoginIp;
    private int status;
    private String role;


}
