package com.swissquote.foundation.serialization.gson.date;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Denis Larka
 * at 31.Aug.2017
 */
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

	@Override
	public JsonElement serialize(LocalDate localDate, Type typeOfSrc, JsonSerializationContext context) {
		// "yyyy-mm-dd"
		return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
	}

	@Override
	public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
	}
}
