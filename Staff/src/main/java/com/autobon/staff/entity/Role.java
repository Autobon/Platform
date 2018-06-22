package com.autobon.staff.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/11/20.
 */
@Entity
@Table(name="t_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

    private String roleAuthority; //角色权限:t_function_category中id的队列,逗号分隔( 注： 现在保存的是所有function分类的ID 即 t_function_category中id的队列 categoryId的集合)


    @Column
    private String remark;

    public String getRoleAuthority() {
        return roleAuthority;
    }

    public void setRoleAuthority(String roleAuthority) {
        this.roleAuthority = roleAuthority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
