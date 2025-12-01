package com.cassinisys.is.model.store;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;

public class AttributeSearchDto {

    private ObjectTypeAttribute objectTypeAttribute;

    private String text;

    private String longText;

    private String integer;

    private String list;

    private String aBoolean;

    private Boolean booleanSearch = Boolean.FALSE;

    private Double aDouble = 0.0;

    private Boolean doubleSearch = Boolean.FALSE;

    private String currency;

    private String date;

    private String time;

	/*private List<String> mListValue = new ArrayList<>();*/

    private String[] mListValue;

    public ObjectTypeAttribute getObjectTypeAttribute() {
        return objectTypeAttribute;
    }

    public void setObjectTypeAttribute(ObjectTypeAttribute objectTypeAttribute) {
        this.objectTypeAttribute = objectTypeAttribute;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    public String getInteger() {
        return integer;
    }

    public void setInteger(String integer) {
        this.integer = integer;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(String aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    public Boolean getBooleanSearch() {
        return booleanSearch;
    }

    public void setBooleanSearch(Boolean booleanSearch) {
        this.booleanSearch = booleanSearch;
    }

    public Boolean getDoubleSearch() {
        return doubleSearch;
    }

    public void setDoubleSearch(Boolean doubleSearch) {
        this.doubleSearch = doubleSearch;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String[] getmListValue() {
        return mListValue;
    }

    public void setmListValue(String[] mListValue) {
        this.mListValue = mListValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
