package com.autobon.platform.listener;

/**
 * Created by dave on 16/4/14.
 */
public class Event<T> {
    public enum Action {CREATED, VERIFIED, FINISHED};
    private T data;
    private Action action;

    public Event(T data, Action action) {
        this.data = data;
        this.action = action;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
