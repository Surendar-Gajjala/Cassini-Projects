package com.cassinisys.erp.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang.StringUtils;

/**
 * Created by reddy on 7/16/15.
 */
@Converter
public class ArrayToStringConverter implements AttributeConverter<String[], String> {


    public String convertToDatabaseColumn(String[] attribute) {
        if (attribute == null || attribute.length == 0) {
            return "";
        }
        return StringUtils.join(attribute, ",");
    }


    public String[] convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().length() == 0) {
            return new String[0];
        }

        String[] data = dbData.split(",");
        return data;
    }
}
