package com.autobon.merchandiser.vo;


import java.util.List;

/**
 * Created by wh on 2018/6/5.
 */

public class MerchandiserCooperatorShow {


    private int id;

    private int merchandiserId;

    private int cooperatorId;

    private String cooperatorName;


    public MerchandiserCooperatorShow(Object[] o){

        this.setId(Integer.valueOf(o[0].toString()));
        this.setMerchandiserId(Integer.valueOf(o[1].toString()));
        this.setCooperatorId(Integer.valueOf(o[2].toString()));
        this.setCooperatorName(o[3].toString());
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

    public String getCooperatorName() {
        return cooperatorName;
    }

    public void setCooperatorName(String cooperatorName) {
        this.cooperatorName = cooperatorName;
    }
}
