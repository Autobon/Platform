package com.autobon.merchandiser.entity;

import javax.persistence.*;

/**
 * Created by wh on 2018/6/5.
 */
@Entity
@Table(name="t_merchandiser_cooperator")
public class MerchandiserCooperator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int merchandiserId;

    private int cooperatorId;


    public MerchandiserCooperator(){}

    public MerchandiserCooperator(int mid , int cid){

        this.merchandiserId = mid;
        this.cooperatorId = cid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMerchandiserId() {
        return merchandiserId;
    }

    public void setMerchandiserId(int merchandiserId) {
        this.merchandiserId = merchandiserId;
    }

    public int getCooperatorId() {
        return cooperatorId;
    }

    public void setCooperatorId(int cooperatorId) {
        this.cooperatorId = cooperatorId;
    }
}
