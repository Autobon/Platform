package com.autobon.technician.entity;

import javax.persistence.*;

/**
 * Created by dave on 16/3/2.
 */
@Entity
@Table(name = "sys_codemap")
public class CodeMap {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String code;

    @Column private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
