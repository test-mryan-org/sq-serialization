package com.swissquote.foundation.serialization.jackson;

import java.util.Date;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.type.MapType;

public class SQModule extends SimpleModule {

	private boolean enableComplexMapKeySerialization;

	public SQModule() {
		super(PackageVersion.buildVersion());
	}

	public SQModule enableComplexMapKeySerialization() {
		enableComplexMapKeySerialization = true;
		return this;
	}

	@Override
	public void setupModule(SetupContext context) {
		super.setupModule(context);

		context.addBeanSerializerModifier(new BeanSerializerModifier() {
			@Override
			public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {

				if (Throwable.class.isAssignableFrom(beanDesc.getBeanClass())) {
					return new ThrowableSerializer(serializer);
				}

				return super.modifySerializer(config, beanDesc, serializer);
			}

			@Override
			public JsonSerializer<?> modifyMapSerializer(SerializationConfig config, MapType valueType, BeanDescription beanDesc,
					JsonSerializer<?> serializer) {

				if (enableComplexMapKeySerialization) {
					return new ComplexKeyMapSerializer((MapSerializer) serializer, null, false);
				}

				return super.modifyMapSerializer(config, valueType, beanDesc, serializer);
			}
		});

		context.addBeanDeserializerModifier(new BeanDeserializerModifier() {
			@Override
			public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
					JsonDeserializer<?> deserializer) {

				if (Date.class.isAssignableFrom(beanDesc.getBeanClass())) {
					return new DateDeserializer();
				}

				return super.modifyDeserializer(config, beanDesc, deserializer);
			}

			@Override
			public JsonDeserializer<?> modifyMapDeserializer(DeserializationConfig config, MapType type, BeanDescription beanDesc,
					JsonDeserializer<?> deserializer) {

				if (enableComplexMapKeySerialization) {
					return new ComplexKeyMapDeserializer((MapDeserializer) deserializer);
				}

				return super.modifyMapDeserializer(config, type, beanDesc, deserializer);
			}
		});
	}
}
