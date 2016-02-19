package com.autobon.staff.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by liz on 2016/2/18.
 */
@Entity
@Table(name="t_staff")
public class Staff implements Serializable {
    private static final long serialVersionUID = 3176972228965536016L;
    private int id;
    private String username;
    private String password;
    private String salt;
    private Date registTime;
    private int roleId;

    public Staff() {
    }

    public Staff(String username, String password, String salt, Date registTime) {
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.registTime = registTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "username", nullable = false, insertable = true, updatable = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false, insertable = true, updatable = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "identify_salt", nullable = false, insertable = true, updatable = true)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(name = "register_time", nullable = false, insertable = true, updatable = true)
    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    @Column(name = "role_id", nullable = false, insertable = true, updatable = true)
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

}
