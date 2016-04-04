package com.autobon.message.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by liz on 2016/4/2.
 */
@Entity
@Table(name = "t_message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String title;

    @Column private String content; // 月帐所属的年,月

    @Column private int sendto;

    @Column private Date updateTime;

    @Column private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSendto() {
        return sendto;
    }

    public void setSendto(int sendto) {
        this.sendto = sendto;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
