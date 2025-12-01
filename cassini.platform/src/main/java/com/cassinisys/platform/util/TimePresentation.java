/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cassinisys.platform.util;

/**
 * @author Suresh Cassini
 */
public enum TimePresentation {


    hhmm_colon(1, "HH:mm", ":"),
    hhmm_dot(2, "HH.mm", "."),
    hhmm_comma(3, "HH,mm", ","),
    hhmm_slash(4, "HH/mm", "/"),
    hhmm_minus(5, "HH-mm", "-");

    private final int id;
    private final String value;
    private final String delimiter;

    private TimePresentation(int id, String value, String delimiter) {
        this.id = id;
        this.value = value;
        this.delimiter = delimiter;
    }

    public static TimePresentation GetById(int id) {
        for (TimePresentation data : TimePresentation.values())
            if (id == data.getId())
                return data;
        return hhmm_colon;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
