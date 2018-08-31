package com.swissquote.foundation.serialization.jackson;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThrowableSerializer extends StdSerializer<Throwable> {

	private final JsonSerializer<Throwable> defaultSerializer;

	private static Field causeField;
	private static Field suppressedField;

	static {
		try {
			causeField = Throwable.class.getDeclaredField("cause");
			suppressedField = Throwable.class.getDeclaredField("suppressedExceptions");
			causeField.setAccessible(true);
			suppressedField.setAccessible(true);
		}
		catch (NoSuchFieldException e) {
			log.warn("Impossible to retrieve fields cause & suppressedExceptions for Throwable", e);
		}
	}

	protected ThrowableSerializer(JsonSerializer<?> defaultSerializer) {
		super(Throwable.class);
		this.defaultSerializer = (JsonSerializer<Throwable>) defaultSerializer;
	}

	@Override
	public void serialize(Throwable value, JsonGenerator gen, SerializerProvider provider) throws IOException {

		// loop until the cause is the exception itself
		Throwable root = value;
		while (root.getCause() != null) {
			// remove stackTrace and suppressedExceptions for each throwable in the chain
			root.setStackTrace(null);
			try {
				suppressedField.set(root, null);
			}
			catch (IllegalAccessException | NullPointerException e) {
				log.warn("Impossible to set suppressedExceptions field to null for {}", root.getClass());
			}
			root = root.getCause();
		}

		// ends the chain
		try {
			causeField.set(root, null);
		}
		catch (IllegalAccessException | NullPointerException e) {
			log.warn("Impossible to set the field cause to null for {}", root.getClass());
		}

		defaultSerializer.serialize(value, gen, provider);
	}
}
