package com.autobon.staff.entity;


import javax.persistence.*;


/**
 * Created by wh on 2016/8/9.
 */

@Entity
@Table(name="t_function_category")
public class FunctionCategory {

    private Integer id ;
    private String category;
    private Integer menuId;
    private Integer isDefault; // 是否默认  0非默认 1默认

    private int isEnable; //是否可用




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "category", nullable = false, insertable = true, updatable = true)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "menuId", nullable = false, insertable = true, updatable = true)
    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @Column(name = "is_default", nullable = false, insertable = true, updatable = true)
    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }


    @Column(name = "is_enable", nullable = false, insertable = true, updatable = true)
    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }
}
