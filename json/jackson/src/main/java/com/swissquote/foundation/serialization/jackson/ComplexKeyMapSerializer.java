package com.swissquote.foundation.serialization.jackson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ComplexKeyMapSerializer extends MapSerializer {

	protected ComplexKeyMapSerializer(MapSerializer src, Object filterId, boolean sortKeys) {
		super(src, filterId, sortKeys);
	}

	protected ComplexKeyMapSerializer(MapSerializer src, BeanProperty property,
			JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer,
			Set<String> ignoredEntries) {

		super(src, property, keySerializer, valueSerializer, ignoredEntries);
	}

	public ComplexKeyMapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue, boolean suppressNulls) {
		super(src, vts, suppressableValue, suppressNulls);
	}

	protected boolean isKeySimpleType(Map<?, ?> value, SerializerProvider provider) {
		// this is better not to use the first key found in the map to check the typing because
		// the value could be null
		//		Map.Entry<?, ?> entry = value.entrySet().iterator().next();
		//		JavaType keyType = provider.getConfig().constructType(key.getClass());
		JavaType keyType = provider.getConfig().constructType(value.getClass()).getKeyType();
		Class<?> rawClass = keyType.getRawClass();
		return keyType.isPrimitive()
				|| keyType.isEnumType()
				|| String.class.isAssignableFrom(rawClass)
				|| Boolean.class.isAssignableFrom(rawClass)
				|| Number.class.isAssignableFrom(rawClass);
	}

	@Override
	public void serialize(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {

		if (value.isEmpty()) {
			super.serialize(value, gen, provider);
			return;
		}

		// check if the key is complex or not
		if (isKeySimpleType(value, provider)) {
			super.serialize(value, gen, provider);
			return;
		}

		gen.writeStartArray();
		if (!value.isEmpty()) {
			if (_sortKeys || provider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)) {
				value = _orderEntries(value, gen, provider);
			}
			PropertyFilter pf;
			if ((_filterId != null) && (pf = findPropertyFilter(provider, _filterId, value)) != null) {
				// todo like serializeFieldsAsTuple
				serializeFilteredFields(value, gen, provider, pf, _suppressableValue);
			} else if ((_suppressableValue != null) || _suppressNulls) {
				// todo like serializeFieldsAsTuple
				serializeOptionalFields(value, gen, provider, _suppressableValue);
			} else if (_valueSerializer != null) {
				// todo like serializeFieldsAsTuple
				serializeFieldsUsing(value, gen, provider, _valueSerializer);
			} else {
				serializeFieldsAsTuple(value, gen, provider);
			}
		}
		gen.writeEndArray();
	}

	protected void serializeFieldsAsTuple(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		// If value type needs polymorphic type handling, some more work needed:
		if (_valueTypeSerializer != null) {
			// todo
			serializeTypedFields(value, gen, provider, null);
			return;
		}
		final Set<String> ignored = _ignoredEntries;
		Object keyElem = null;

		try {
			for (Map.Entry<?, ?> entry : value.entrySet()) {
				gen.writeStartArray();
				Object valueElem = entry.getValue();
				keyElem = entry.getKey();
				List<Object> tuple = Arrays.asList(keyElem, valueElem);
				gen.setCurrentValue(tuple);

				if (keyElem == null) {
					provider.defaultSerializeNull(gen);
				} else {
					// One twist: is entry ignorable? If so, skip
					if ((ignored != null) && ignored.contains(keyElem)) {
						continue;
					}
					_findSerializer(provider, keyElem).serialize(keyElem, gen, provider);
				}
				// And then value
				if (valueElem == null) {
					provider.defaultSerializeNull(gen);
					continue;
				}
				JsonSerializer<Object> serializer = _valueSerializer;
				if (serializer == null) {
					serializer = _findSerializer(provider, valueElem);
				}
				serializer.serialize(valueElem, gen, provider);

				gen.writeEndArray();
			}
		}
		catch (Exception e) { // Add reference information
			wrapAndThrow(provider, e, value, String.valueOf(keyElem));
		}
	}

	private final JsonSerializer<Object> _findSerializer(SerializerProvider provider,
			Object value) throws JsonMappingException {
		final Class<?> cc = value.getClass();
		JsonSerializer<Object> valueSer = _dynamicValueSerializers.serializerFor(cc);
		if (valueSer != null) {
			return valueSer;
		}
		if (_valueType.hasGenericTypes()) {
			return _findAndAddDynamic(_dynamicValueSerializers,
					provider.constructSpecializedType(_valueType, cc), provider);
		}
		return _findAndAddDynamic(_dynamicValueSerializers, cc, provider);
	}

	@Override
	public ComplexKeyMapSerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer,
			Set<String> ignored, boolean sortKeys) {
		return new ComplexKeyMapSerializer(this, property, keySerializer, valueSerializer, ignored);
	}

	@Override
	public ComplexKeyMapSerializer _withValueTypeSerializer(TypeSerializer vts) {
		if (_valueTypeSerializer == vts) {
			return this;
		}
		_ensureOverride("_withValueTypeSerializer");
		return new ComplexKeyMapSerializer(this, vts, _suppressableValue, _suppressNulls);
	}

	@Override
	public MapSerializer withFilterId(Object filterId) {
		if (_filterId == filterId) {
			return this;
		}
		_ensureOverride("withFilterId");
		return new ComplexKeyMapSerializer(this, filterId, _sortKeys);
	}

	@Override
	public MapSerializer withContentInclusion(Object suppressableValue, boolean suppressNulls) {
		if ((suppressableValue == _suppressableValue) && (suppressNulls == _suppressNulls)) {
			return this;
		}
		_ensureOverride("withContentInclusion");
		return new ComplexKeyMapSerializer(this, _valueTypeSerializer, suppressableValue, suppressNulls);
	}

	protected void _ensureOverride(String method) {
		ClassUtil.verifyMustOverride(ComplexKeyMapSerializer.class, this, method);
	}

	//    ajouter tous les deprecated creator et les autres method qui sont definies dans la super classe

}
