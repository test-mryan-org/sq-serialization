package com.swissquote.foundation.serialization.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class SQModule extends SimpleModule {

	public SQModule() {
		super(PackageVersion.VERSION);

//		addDeserializer(Throwable.class, ThrowableSerializer(objectSerialier));
	}
}
