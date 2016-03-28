package com.autobon.cooperators.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yuh on 2016/3/14.
 */
@Entity
@Table(name = "t_review_cooper")
public class ReviewCooper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private int cooperatorId;

    @Column private Date reviewTime;

    @Column private int reviewerId;

    @Column private String remark;

    @Column private Boolean result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCooperatorId() {
        return cooperatorId;
    }

    public void setCooperatorId(int cooperatorId) {
        this.cooperatorId = cooperatorId;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
