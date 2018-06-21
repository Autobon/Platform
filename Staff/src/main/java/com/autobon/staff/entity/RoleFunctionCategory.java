package com.autobon.staff.entity;

import com.hpe.authority.RoleFunctionCategoryRelativeShow;

import javax.persistence.*;

/**
 * Created by wh on 2016/8/9.
 */

@Entity
@Table(name="t_role_function_category_relative")
public class RoleFunctionCategory {

    private Integer id;
    private Integer roleId;
    private String categoryId;


    public RoleFunctionCategory(){}

    public RoleFunctionCategory(RoleFunctionCategoryRelativeShow roleFunctionCategoryRelativeShow){
        this.id = roleFunctionCategoryRelativeShow.getId();
        this.roleId = roleFunctionCategoryRelativeShow.getRoleId();
        this.categoryId = roleFunctionCategoryRelativeShow.getCategoryId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "role_id", nullable = false, insertable = true, updatable = true)
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }


    @Column(name = "category_id", nullable = false, insertable = true, updatable = true)
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
