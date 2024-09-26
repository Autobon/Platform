package com.autobon.study.entity;

import javax.persistence.*;

/**
 * 学习园地-学习资料
 * Created by Administrator on 2017/6/14.
 */
@Entity
@Table(name = "t_study_data")
public class StudyData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private int type;   // 1,培训资料   2，施工标准   3，业务规则

    @Column
    private String fileName;

    @Column
    private int fileLength;

    @Column
    private String path;

    @Column
    private String remark;

    public StudyData() {
    }

    public StudyData(int type, String fileName, int fileLength, String path, String remark) {
        this.type = type;
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.path = path;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
