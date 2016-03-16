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
    @Column
    private int id;

    @Column
    private int cooperatorsId;

    @Column
    private Date reviewTime;

    @Column
    private String checkedBy;

    @Column
    private String resultDesc;

    @Column
    private Boolean result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCooperatorsId() {
        return cooperatorsId;
    }

    public void setCooperatorsId(int cooperatorsId) {
        this.cooperatorsId = cooperatorsId;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
