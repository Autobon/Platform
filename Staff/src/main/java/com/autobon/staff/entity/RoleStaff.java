package com.autobon.staff.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/11/20.
 */
@Entity
@Table(name="t_role_staff")
public class RoleStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private Integer roleId;

    @Column
    private Integer staffId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }
}
