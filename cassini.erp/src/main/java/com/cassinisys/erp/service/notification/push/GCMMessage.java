package com.cassinisys.erp.service.notification.push;

/**
 * Created by reddy on 18/02/16.
 */
public class GCMMessage {
    private String to;
    private Object data;

    public GCMMessage() {

    }

    public GCMMessage(String to, Object data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
