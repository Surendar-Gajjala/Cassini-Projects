package com.cassinisys.erp.converters;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomShortDateSerializer extends JsonSerializer<Date> {
	public static final String DATEFORMAT = "dd/MM/yyyy";

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
			throws IOException, JsonProcessingException {

		SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);
		String formattedDate = formatter.format(value);

		gen.writeString(formattedDate);

	}

}
