package com.swissquote.foundation.serialization.gson.date;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Denis Larka
 * at 19.Sep.2017
 */
public class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

	@Override
	public JsonElement serialize(LocalTime localTime, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(DateTimeFormatter.ISO_LOCAL_TIME.format(localTime));
	}

	@Override
	public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		try {
			return LocalTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_TIME);
		}
		catch (DateTimeParseException e) {
			return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalTime();
		}
	}

}
