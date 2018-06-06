package com.autobon.merchandiser.vo;

import javax.persistence.*;

/**
 * Created by wh on 2018/6/5.
 */

public class MerchandiserCooperatorShow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int merchandiserId;

    private int cooperatorId;

    private String cooperatorName;


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

    public String getCooperatorName() {
        return cooperatorName;
    }

    public void setCooperatorName(String cooperatorName) {
        this.cooperatorName = cooperatorName;
    }
}
