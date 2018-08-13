package com.swissquote.foundation.serialization.gson.date;

import java.lang.reflect.Type;
import java.time.Instant;
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
 * at 06.Sep.2017
 */
public class InstantAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

	@Override
	public JsonElement serialize(Instant instant, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(instant));
	}

	@Override
	public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return Instant.parse(json.getAsString());
	}


}
