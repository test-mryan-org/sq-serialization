package com.swissquote.foundation.serialization.json;

import java.io.Writer;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonJsonObjectMapper implements JsonObjectMapper<Map<String, Object>, Object> {

	private final Gson gson;

	public GsonJsonObjectMapper() {
		this(new GsonBuilder().create());
	}

	public GsonJsonObjectMapper(Gson gson) {
		this.gson = gson;
	}

	@Override
	public String toJson(Object value) throws Exception {
		return gson.toJson(value);
	}

	@Override
	public void toJson(Object value, Writer writer) throws Exception {
		gson.toJson(value, writer);
	}

	@Override
	public <T> T fromJson(Object json, Class<T> valueType) throws Exception {
		return gson.fromJson((String) json, valueType);
	}

	@Override
	public <T> T fromJson(Object json, Map<String, Object> javaTypes) throws Exception {
		return null;
	}
}
