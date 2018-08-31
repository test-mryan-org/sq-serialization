package com.swissquote.foundation.serialization.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

public class SQModule extends SimpleModule {

	public SQModule() {
		super(PackageVersion.VERSION);

		setSerializerModifier(new BeanSerializerModifier() {
			@Override
			public JsonSerializer<?> modifySerializer(
					SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
				if (beanDesc.getBeanClass() == Throwable.class)
					return new ThrowableSerializer(serializer);
				return super.modifySerializer(config, beanDesc, serializer);
			}
		});
	}
}
