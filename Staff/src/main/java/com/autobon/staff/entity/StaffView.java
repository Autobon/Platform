package com.autobon.staff.entity;

/**
 * Created by Administrator on 2017/11/22.
 */
public class StaffView {
    private int id;

    private String username;

    private String password;

    private String name;

    private String email;

    private String phone;

    private int roleId;

    public StaffView(Staff staff) {
        this.id = staff.getId();
        this.username = staff.getUsername();
        this.password = staff.getPassword();
        this.name = staff.getName();
        this.email = staff.getEmail();
        this.phone = staff.getPhone();
    }

    public StaffView() {
    }

    public StaffView(int id, String username, String password, String name, String email, String phone, int roleId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.roleId = roleId;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
