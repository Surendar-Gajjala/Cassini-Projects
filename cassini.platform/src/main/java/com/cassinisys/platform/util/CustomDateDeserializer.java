package com.cassinisys.platform.util;

import com.cassinisys.platform.service.security.SessionWrapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CustomDateDeserializer extends JsonDeserializer<Date> {
    @Autowired
    private SessionWrapper sessionWrapper;
    public static final String DATEFORMAT = "dd/MM/yyyy, HH:mm:ss";

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
        String df = DATEFORMAT;

        if (System.getProperty("cassini.timestamp.format") != null) {
            df = System.getProperty("cassini.timestamp.format");
        }

        String preferredDateFormat = null;
        if (sessionWrapper != null && sessionWrapper.getSession() != null && sessionWrapper.getSession().getPreferredDateFormat() != null) {
            preferredDateFormat = sessionWrapper.getSession().getPreferredDateFormat();
        }
        SimpleDateFormat format = new SimpleDateFormat(df);
        String date = jsonparser.getText();
        if (preferredDateFormat != null) {
            Date preferredDate = null;
            try {
                preferredDate = new SimpleDateFormat(preferredDateFormat).parse(date);
                date = new SimpleDateFormat(df).format(preferredDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}