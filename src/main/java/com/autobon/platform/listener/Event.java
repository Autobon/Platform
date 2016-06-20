package com.autobon.platform.listener;

/**
 * Created by dave on 16/4/14.
 */
public class Event<T> {
    public enum Action {CREATED, VERIFIED, APPOINTED, FINISHED, TAKEN, CANCELED, GIVEN_UP};
    private T payload;
    private Action action;

    public Event(T data, Action action) {
        this.payload = data;
        this.action = action;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
