package com.cassinisys.pdm.filtering;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Nageshreddy on 11-06-2016.
 */

@Component
public class ParameterCriteria implements Serializable{


    private String field;
    private String operator;
    private String value;

    public ParameterCriteria() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
