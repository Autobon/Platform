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

    @Column private int sendto; //发送目标: 1 技师端, 2 合作商户

    @Column private Date createTime;

    @Column private Date publishTime;

    @Column private int status; // 发布状态: 1-已发布, 0-未发布

    public Message() {
        this.createTime = new Date();
        this.status = 0;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
}
