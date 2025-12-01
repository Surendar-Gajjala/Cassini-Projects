package com.cassinisys.platform.util;

import com.cassinisys.platform.service.security.SessionWrapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CustomShortDateSerializer extends JsonSerializer<Date> {
    @Autowired
    private SessionWrapper sessionWrapper;
    public static final String DATEFORMAT = "dd/MM/yyyy";

    @Override
    @Transactional
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {

        String df = DATEFORMAT;

        if (System.getProperty("cassini.date.format") != null) {
            df = System.getProperty("cassini.date.format");
        }
        if (sessionWrapper != null && sessionWrapper.getSession() != null && sessionWrapper.getSession().getPreferredShortDateFormat() != null) {
            df = sessionWrapper.getSession().getPreferredShortDateFormat();
        }

        SimpleDateFormat formatter = new SimpleDateFormat(df);
        String formattedDate = formatter.format(value);

        gen.writeString(formattedDate);

    }

}
