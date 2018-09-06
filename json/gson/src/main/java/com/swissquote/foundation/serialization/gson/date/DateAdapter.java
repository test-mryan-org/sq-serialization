package com.swissquote.foundation.serialization.gson.date;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateAdapter implements JsonDeserializer<Date> {

	private String deserializationDateFormatPattern;

	public DateAdapter(String deserializationDateFormatPattern) {
		this.deserializationDateFormatPattern = deserializationDateFormatPattern;
	}

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		try {
			return new SimpleDateFormat(deserializationDateFormatPattern).parse(json.getAsString());
		}
		catch (ParseException e) {
			try {
				return new SimpleDateFormat("yyyy'-'MM'-'dd").parse(json.getAsString());
			}
			catch (ParseException e1) {
				throw new RuntimeException(e1);
			}
		}
	}
}
