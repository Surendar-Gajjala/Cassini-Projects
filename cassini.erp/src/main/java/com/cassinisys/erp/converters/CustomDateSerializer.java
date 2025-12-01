package com.cassinisys.erp.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateSerializer extends JsonSerializer<Date> {
    //public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    //public static final String DATEFORMAT = "EEE, dd MMM yyyy HH:mm:ss";
    public static final String DATEFORMAT = "dd/MM/yyyy, HH:mm:ss";

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws
            IOException, JsonProcessingException {

        SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);
        String formattedDate = formatter.format(value);

        gen.writeString(formattedDate);

    }


}