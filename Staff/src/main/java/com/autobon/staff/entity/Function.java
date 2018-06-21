package com.autobon.staff.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Liujingguo91year on 2016/2/23.
 */
@Entity
@Table(name="t_function")
public class Function implements Serializable {
    private Integer id;
    private String functionName; //功能名称
    private String functionDir; //功能路径
    private String functionType; //接口方式
    private Integer menuId;  // 关联菜单id
    private Integer categoryId; //分类id
    private String remark;  //备注
    private char isEnable; //是否可用

    public Function() {
    }

    public Function(Integer id, String functionName, String functionDir, String functionType, String remark, Date createDate, Date modifyDate, String createUser, String modifyUser, Integer vendorId, char isEnable) {
        this.id = id;
        this.functionName = functionName;
        this.functionDir = functionDir;
        this.functionType = functionType;
        this.remark = remark;
        this.isEnable = isEnable;
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

    @Column(name = "fun_name", nullable = false, insertable = true, updatable = true)
    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Column(name = "fun_dir", nullable = false, insertable = true, updatable = true)
    public String getFunctionDir() {
        return functionDir;
    }

    public void setFunctionDir(String functionDir) {
        this.functionDir = functionDir;
    }

    @Column(name = "fun_type", nullable = false, insertable = true, updatable = true)
    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    @Column(name = "menu_id", nullable = false, insertable = true, updatable = true)
    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @Column(name = "category_id", nullable = false, insertable = true, updatable = true)
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Column(name = "remark", nullable = false, insertable = true, updatable = true)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Column(name = "is_enable", nullable = false, insertable = true, updatable = true)
    public char getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(char isEnable) {
        this.isEnable = isEnable;
    }

}
