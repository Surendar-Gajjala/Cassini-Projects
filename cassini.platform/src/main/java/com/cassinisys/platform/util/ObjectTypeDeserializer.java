package com.cassinisys.platform.util;

import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public class ObjectTypeDeserializer extends JsonDeserializer<Enum> {
	@Override
	@Transactional
	public ObjectType deserialize(JsonParser jsonparser,
			DeserializationContext deserializationcontext) throws IOException,
			JsonProcessingException {
		return ObjectType.valueOf(jsonparser.getText());
	}

}