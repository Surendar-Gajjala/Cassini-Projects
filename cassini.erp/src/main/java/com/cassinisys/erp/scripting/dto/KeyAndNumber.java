package com.cassinisys.erp.scripting.dto;

/**
 * Created by reddy on 9/14/15.
 */
public class KeyAndNumber {
    private String key;
    private Number number;

    public KeyAndNumber() {

    }

    public KeyAndNumber(Object s, Number n) {
        this.key = s.toString();
        this.number = n;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }
}
