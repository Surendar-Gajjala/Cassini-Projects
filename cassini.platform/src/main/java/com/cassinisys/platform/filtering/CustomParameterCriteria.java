package com.cassinisys.platform.filtering;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Nageshreddy on 03-08-2021.
 */

@Component
public class CustomParameterCriteria implements Serializable {

    private String field;
    private String operator;
    private String value;
    private String itemClass;
    private String attributeId;

    public CustomParameterCriteria() {
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

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }
}
