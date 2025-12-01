package com.cassinisys.erp.model.common;

/**
 * Created by reddy on 6/26/15.
 */
public enum DataType {
    STRING("STRING"),
    INTEGER("INTEGER"),
    DOUBLE("DOUBLE"),
    DATE("DATE"),
    BOOLEAN("BOOLEAN"),
    SINGLELIST("SINGLELIST"),
    MULTILIST("MULTILIST");

    private String type;

    DataType(String s) {
        type = s;
    }

    public String getType() {
        return type;
    }
}
