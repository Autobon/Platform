package com.autobon.shared;

/**
 * Created by wh on 2016/11/2.
 */
public class JsonResult {

    private boolean status;
    private Object message;


    public JsonResult(){}
    public JsonResult(boolean status){
        this.status = status;
    }
    public JsonResult(boolean status, Object message){
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
