package com.cassinisys.platform.util;

import com.cassinisys.platform.service.security.SessionWrapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CustomDateSerializer extends JsonSerializer<Date> {

    @Autowired
    private SessionWrapper sessionWrapper;

    //public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    //public static final String DATEFORMAT = "EEE, dd MMM yyyy HH:mm:ss";
    public static final String DATEFORMAT = "dd/MM/yyyy, HH:mm:ss";


    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws
            IOException, JsonProcessingException {
        String df = DATEFORMAT;

        if (System.getProperty("cassini.timestamp.format") != null) {
            df = System.getProperty("cassini.timestamp.format");
        }

        if (sessionWrapper != null && sessionWrapper.getSession() != null && sessionWrapper.getSession().getPreferredDateFormat() != null) {
            df = sessionWrapper.getSession().getPreferredDateFormat();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(df);
        String formattedDate = formatter.format(value);

        gen.writeString(formattedDate);

    }


}