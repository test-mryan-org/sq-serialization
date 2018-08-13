package com.swissquote.foundation.serialization.gson.date;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
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
 * at 01.Sep.2017
 */
public class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {
	@Override
	public JsonElement serialize(ZonedDateTime zonedDateTime, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(zonedDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
	}

	@Override
	public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return ZonedDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
	}
}
