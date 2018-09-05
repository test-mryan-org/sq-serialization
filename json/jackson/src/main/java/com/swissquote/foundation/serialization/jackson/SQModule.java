package com.swissquote.foundation.serialization.jackson;

import java.util.Date;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

public class SQModule extends SimpleModule {

	public SQModule() {
		super(PackageVersion.buildVersion());

		setSerializerModifier(new BeanSerializerModifier() {
			@Override
			public JsonSerializer<?> modifySerializer(
					SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
				if (Throwable.class.isAssignableFrom(beanDesc.getBeanClass())) {
					return new ThrowableSerializer(serializer);
				}
				return super.modifySerializer(config, beanDesc, serializer);
			}
		});

		setDeserializerModifier(new BeanDeserializerModifier() {
			@Override
			public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
					JsonDeserializer<?> deserializer) {

				if (Date.class.isAssignableFrom(beanDesc.getBeanClass())) {
					return new DateDeserializer();
				}

				return super.modifyDeserializer(config, beanDesc, deserializer);
			}
		});
	}
}
