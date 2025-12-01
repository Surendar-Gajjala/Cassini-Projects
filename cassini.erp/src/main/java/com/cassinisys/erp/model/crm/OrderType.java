package com.cassinisys.erp.model.crm;

/**
 * Created by reddy on 10/29/15.
 */
public enum OrderType {
    PRODUCT("PRODUCT"),
    SAMPLE("SAMPLE"),
    QUESTIONPAPER("QUESTIONPAPER"),
    PROD("PROD"),
    MPO("MPO");

    private String type;

    public String getType() {
        return type;
    }

    OrderType(String s) {
        type = s;
    }
}
