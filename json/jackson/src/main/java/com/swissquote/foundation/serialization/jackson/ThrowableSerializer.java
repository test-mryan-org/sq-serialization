package com.swissquote.foundation.serialization.jackson;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThrowableSerializer extends StdSerializer<Throwable> {

	private final JsonSerializer<Throwable> defaultSerializer;

	private static Field causeField;
	private static Field suppressedField;
	private static Field stackTraceField;

	static {
		try {
			causeField = Throwable.class.getDeclaredField("cause");
			suppressedField = Throwable.class.getDeclaredField("suppressedExceptions");
			stackTraceField = Throwable.class.getDeclaredField("stackTrace");
			causeField.setAccessible(true);
			suppressedField.setAccessible(true);
			stackTraceField.setAccessible(true);
		}
		catch (NoSuchFieldException e) {
			log.warn("Impossible to retrieve fields cause & suppressedExceptions for Throwable", e);
		}
	}

	protected ThrowableSerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> defaultSerializer) {
		super(Throwable.class);
		this.defaultSerializer = (JsonSerializer<Throwable>) defaultSerializer;
	}

	@Override
	public void serialize(Throwable value, JsonGenerator gen, SerializerProvider provider) throws IOException {

		// loop until the cause is the exception itself
		Throwable root = value;
		try {
			do {
				 // remove stackTrace and suppressedExceptions for each throwable in the chain
				try {
					stackTraceField.set(root, null);
					suppressedField.set(root, null);
				}
				catch (IllegalAccessException | NullPointerException e) {
					log.warn("Impossible to set suppressedExceptions or stackTrace fields to null for {}", root.getClass());
				}
				root = root.getCause();
			}
			while (root.getCause() != null);

			// ends the chain
			causeField.set(root, null);
		}
		catch (IllegalAccessException e) {
			log.warn("Impossible to set the field cause to null for {}", root.getClass());
		}
		catch (NullPointerException e) {
			// means that we face an exception which is last in chain and has already been cleaned
			// --> do nothing
		}

		defaultSerializer.serialize(value, gen, provider);
	}
}
