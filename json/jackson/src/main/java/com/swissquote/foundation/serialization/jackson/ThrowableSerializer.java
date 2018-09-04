package com.swissquote.foundation.serialization.jackson;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThrowableSerializer extends StdSerializer<Throwable> {

	private static FieldUpdater causeField = FieldUpdater.forClass(Throwable.class, "cause");
	private static FieldUpdater suppressedField = FieldUpdater.forClass(Throwable.class, "suppressedExceptions");
	private static FieldUpdater stackTraceField = FieldUpdater.forClass(Throwable.class, "stackTrace");

	private final JsonSerializer<Throwable> defaultSerializer;

	protected ThrowableSerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> defaultSerializer) {
		super(Throwable.class);
		this.defaultSerializer = (JsonSerializer<Throwable>) defaultSerializer;
	}

	@Override
	public void serialize(Throwable value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		stackTraceField.setNull(value);
		suppressedField.setNull(value);
		if (value.getCause() == null) {
			// Set 'cause' of root cause to null to break the cycle
			// | It looks strange to set this field to null when its getter 'getCause()' returns null
			// | but the field is probably equals to it-self
			causeField.setNull(value);
		}
		defaultSerializer.serialize(value, gen, provider);
	}

	private interface FieldUpdater {
		FieldUpdater VOID = new FieldUpdater() {
			@Override
			public void setNull(Object object) {
				// null
			}
		};

		static FieldUpdater forClass(Class<?> cls, String name) {
			try {
				Field field = cls.getDeclaredField(name);
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				return new ValidUpdater(name, field);
			}
			catch (NoSuchFieldException e) {
				log.warn("Impossible to retrieve field {} for class {}", name, cls);
				return VOID;
			}
		}

		void setNull(Object object);
	}

	@RequiredArgsConstructor
	private static class ValidUpdater implements FieldUpdater {
		private final String name;
		private final Field field;

		@Override
		public void setNull(Object object) {
			try {
				if (log.isDebugEnabled()) {
					log.debug("Setting field '" + name + "' to null on object " + object);
				}
				field.set(object, null);
			}
			catch (IllegalAccessException e) {
				log.warn("Impossible to set field {} to null on object {}", name, object);
			}
		}
	}
}
