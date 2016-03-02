package com.autobon.technician.entity;

import javax.persistence.*;

/**
 * Created by yuh on 2016/2/17.
 */
@Entity
@Table(name="sys_codeitem")
public class CodeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Column(name = "code", nullable = false, insertable = true, updatable = true)
    private String code;

    @Column(name = "codemap", nullable = false, insertable = true, updatable = true)
    private String codemap;

    @Column(name = "name", nullable = false, insertable = true, updatable = true)
    private String name;

    @Column(name = "remark", nullable = true, insertable = true, updatable = true)
    private String remark;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodemap() {
        return codemap;
    }

    public void setCodemap(String codemap) {
        this.codemap = codemap;
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
