package com.swissquote.foundation.serialization.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ThrowableSerializer extends StdSerializer<Throwable> {

//	static Field causeField = Throwable.class.getDeclaredField("cause");
//
//	static {
//		causeField.setAccessible(true);
//	}

	//
	protected ThrowableSerializer(Class<Throwable> t) {
		super(t);
	}

	protected ThrowableSerializer(JavaType type) {
		super(type);
	}

	protected ThrowableSerializer(Class<?> t, boolean dummy) {
		super(t, dummy);
	}

	protected ThrowableSerializer(StdSerializer<?> src) {
		super(src);
	}

	@Override
	public void serialize(Throwable value, JsonGenerator gen, SerializerProvider provider) throws IOException {

		// loop value and remove the cause = this by cause = null
		Throwable root = value;
		while (root.getCause() != null) {
			// remove stackTrace on all chain
			// remove suppressedExceptions on all chain
			root = root.getCause();
		}

		// find the root cause
		//	causeField.set(root, null);

		// retrieve std object serialier
		// objectSerializer.serialize(value, gen, provider);
	}
}
