package com.swissquote.foundation.serialization.jackson;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.MapProperty;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

 /*
 	TODO refactor this inheritance by composition pattern like

 	                   MapSerializer
                            ^
                            |
             ------------------------------
             |                            |
 	   MapSerializerDispatcher       ComplexKeyMapSerializer
 	     - MapSerializer
 	     - ComplexKeyMapSerializer

 */

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

	/*
	 * Overrides creator to key the type 'ComplexKeyMapSerializer in the chain
	 */

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
		ensureOverrideForComplex("_withValueTypeSerializer");
		return new ComplexKeyMapSerializer(this, vts, _suppressableValue, _suppressNulls);
	}

	@Override
	public MapSerializer withFilterId(Object filterId) {
		if (_filterId == filterId) {
			return this;
		}
		ensureOverrideForComplex("withFilterId");
		return new ComplexKeyMapSerializer(this, filterId, _sortKeys);
	}

	@Override
	public MapSerializer withContentInclusion(Object suppressableValue, boolean suppressNulls) {
		if ((suppressableValue == _suppressableValue) && (suppressNulls == _suppressNulls)) {
			return this;
		}
		ensureOverrideForComplex("withContentInclusion");
		return new ComplexKeyMapSerializer(this, _valueTypeSerializer, suppressableValue, suppressNulls);
	}

	@Override
	protected void _ensureOverride(String method) {
		// We don't override this method on purpose
		super._ensureOverride(method);
	}

	protected void ensureOverrideForComplex(String method) {
		ClassUtil.verifyMustOverride(ComplexKeyMapSerializer.class, this, method);
	}

	/*
	 * Overrides Serialization mechanism when applicable
	 */
	@Override
	public void serialize(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (shouldSerializedAsListOfTuple(value, provider)) {
			// Launch the Gson Like serialization for complex key map
			serializeMapAsTuple(value, gen, provider);
		} else {
			super.serialize(value, gen, provider);
		}
	}

	@Override
	public void serializeWithType(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
		if (shouldSerializedAsListOfTuple(value, provider)) {
			// Launch the Gson Like serialization for complex key map
			serializeWithTypeAsTuple(value, gen, provider, typeSer);
		} else {
			super.serializeWithType(value, gen, provider, typeSer);
		}
	}

	@Override
	public void serializeFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (shouldSerializedAsListOfTuple(value, provider)) {
			// Launch the Gson Like serialization for complex key map
			serializeFieldsAsTuple(value, gen, provider);
		} else {
			super.serializeFields(value, gen, provider);
		}
	}

	@Override
	public void serializeOptionalFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue)
			throws IOException {

		if (shouldSerializedAsListOfTuple(value, provider)) {
			// Launch the Gson Like serialization for complex key map
			serializeOptionalFieldsAsTuple(value, gen, provider, suppressableValue);
		} else {
			super.serializeOptionalFields(value, gen, provider, suppressableValue);
		}
	}

	@Override
	public void serializeFieldsUsing(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, JsonSerializer<Object> ser)
			throws IOException {
		if (shouldSerializedAsListOfTuple(value, provider)) {
			// Launch the Gson Like serialization for complex key map
			serializeFieldsUsingAsTuple(value, gen, provider, ser);
		} else {
			super.serializeFieldsUsing(value, gen, provider, ser);
		}
	}

	@Override
	public void serializeFilteredFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter,
			Object suppressableValue) throws IOException {

		if (shouldSerializedAsListOfTuple(value, provider)) {
			// Launch the Gson Like serialization for complex key map
			serializeFilteredFieldsAsTuple(value, gen, provider, filter, suppressableValue);
		} else {
			super.serializeFilteredFields(value, gen, provider, filter, suppressableValue);
		}
	}

	@Override
	public void serializeTypedFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue)
			throws IOException {
		if (shouldSerializedAsListOfTuple(value, provider)) {
			// Launch the Gson Like serialization for complex key map
			serializeTypedFieldsAsTuple(value, gen, provider, suppressableValue);
		} else {
			super.serializeTypedFields(value, gen, provider, suppressableValue);
		}
	}

	@Override
	public void serializeFilteredAnyProperties(SerializerProvider provider, JsonGenerator gen, Object bean, Map<?, ?> value,
			PropertyFilter filter, Object suppressableValue) throws IOException {

		if (shouldSerializedAsListOfTuple(value, provider)) {
			// Launch the Gson Like serialization for complex key map
			serializeFilteredAnyPropertiesAsTuple(provider, gen, bean, value, filter, suppressableValue);
		} else {
			super.serializeFilteredAnyProperties(provider, gen, bean, value, filter, suppressableValue);
		}
	}

	@Override
	protected Map<?, ?> _orderEntries(Map<?, ?> input, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (shouldSerializedAsListOfTuple(input, provider)) {
			// Launch the Gson Like serialization for complex key map
			return _orderEntriesAsTuple(input, gen, provider);
		}
		return super._orderEntries(input, gen, provider);
	}

	@Override
	protected void _writeNullKeyedEntry(JsonGenerator gen, SerializerProvider provider, Object value) throws IOException {
		// We cannot override this method as we don't know the key type.
		// as this code is used in _orderEntries, we directly override this method instead
		super._writeNullKeyedEntry(gen, provider, value);
	}

	/*
	 * Utility methods used to dispatch the serialization behavior
	 */
	protected boolean complexKeyMapSerializationEnabled(SerializerProvider provider) {
		return true;
	}



	protected boolean shouldSerializedAsListOfTuple(Map<?, ?> value, SerializerProvider provider) {
		if (!complexKeyMapSerializationEnabled(provider)) {
			return false;
		}

		if (value.isEmpty()) {
			return false;
		}

		// check if the key is simple
		if (TypeUtils.isKeySimpleType(value, provider.getConfig())) {
			return false;
		}

		return true;
	}

	private void serializeTuple(Object keyElem, Object valueElem, JsonGenerator gen, SerializerProvider provider,
			JsonSerializer<Object> keySerializer,
			JsonSerializer<Object> elementSerializer, boolean withType) throws IOException {

		gen.writeStartArray();
		gen.setCurrentValue(Arrays.asList(keyElem, valueElem));
		try {
			Optional.ofNullable(keyElem).map(k -> keySerializer).orElseGet(provider::getDefaultNullValueSerializer)
					.serialize(keyElem, gen, provider);

			JsonSerializer<Object> serializer =
					Optional.ofNullable(keyElem).map(v -> elementSerializer).orElseGet(provider::getDefaultNullValueSerializer);

			if (withType) {
				serializer.serializeWithType(valueElem, gen, provider, _valueTypeSerializer);
			} else {
				serializer.serialize(valueElem, gen, provider);
			}
		}
		finally {
			gen.writeEndArray();
		}
	}

	/* method from upper class is private, copy-paste it here */
	private JsonSerializer<Object> _findSerializer(SerializerProvider provider,
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

	protected void serializeMapAsTuple(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {

		gen.writeStartArray();
		gen.setCurrentValue(value);
		dispatchSerialization(value, gen, provider);
		gen.writeEndArray();

	}

	protected void serializeWithTypeAsTuple(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
			throws IOException {

		gen.setCurrentValue(value);
		WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.START_OBJECT));
		dispatchSerialization(value, gen, provider);
		typeSer.writeTypeSuffix(gen, typeIdDef);

	}

	private void dispatchSerialization(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (!value.isEmpty()) {
			if (_sortKeys || provider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)) {
				value = _orderEntriesAsTuple(value, gen, provider);
			}
			PropertyFilter pf;
			if ((_filterId != null) && (pf = findPropertyFilter(provider, _filterId, value)) != null) {
				serializeFilteredFieldsAsTuple(value, gen, provider, pf, _suppressableValue);
			} else if ((_suppressableValue != null) || _suppressNulls) {
				serializeOptionalFieldsAsTuple(value, gen, provider, _suppressableValue);
			} else if (_valueSerializer != null) {
				serializeFieldsUsingAsTuple(value, gen, provider, _valueSerializer);
			} else {
				serializeFieldsAsTuple(value, gen, provider);
			}
		}
	}

	/*
	 * Custom implementation for serialization of complexKeyMap as a list of tuple (Map entries)
	 * in order to have the same JSON as GSON.enableComplexKeyMapSerialization
	 */
	protected void serializeFieldsAsTuple(Map<?, ?> value, JsonGenerator g, SerializerProvider provider) throws IOException {
		// If value type needs polymorphic type handling, some more work needed:
		if (_valueTypeSerializer != null) {
			serializeTypedFieldsAsTuple(value, g, provider, null);
			return;
		}

		Object lastKeyElem = null;
		try {
			for (Map.Entry<?, ?> entry : value.entrySet()) {
				Object valueElem = entry.getValue();
				Object keyElem = entry.getKey();
				lastKeyElem = keyElem;
				final Set<String> ignored = _ignoredEntries;

				if (keyElem != null && ignored != null && ignored.contains(keyElem)) {
					continue;
				}

				JsonSerializer<Object> serializer = _valueSerializer != null
						? _valueSerializer
						: _findSerializer(provider, valueElem);

				serializeTuple(keyElem, valueElem, g, provider,
						_findSerializer(provider, keyElem),
						serializer, false);
			}
		}
		catch (Exception e) { // Add reference information
			wrapAndThrow(provider, e, value, String.valueOf(lastKeyElem));
		}
	}

	private void serializeTypedFieldsAsTuple(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue)
			throws IOException {

		final Set<String> ignored = _ignoredEntries;
		final boolean checkEmpty = (MARKER_FOR_EMPTY == suppressableValue);

		for (Map.Entry<?, ?> entry : value.entrySet()) {
			final Object keyElem = entry.getKey();
			final Object valueElem = entry.getValue();

			if (keyElem != null && ignored != null && ignored.contains(keyElem)) {
				continue;
			}

			if (valueElem == null && _suppressNulls) {
				continue;
			}

			JsonSerializer<Object> valueSer = _valueSerializer;
			if (valueSer == null) {
				valueSer = _findSerializer(provider, valueElem);
			}
			// also may need to skip non-empty values:
			if (checkEmpty) {
				if (valueSer.isEmpty(provider, valueElem)) {
					continue;
				}
			} else if (suppressableValue != null) {
				if (suppressableValue.equals(valueElem)) {
					continue;
				}
			}

			try {
				serializeTuple(keyElem, valueElem, gen, provider,
						_findSerializer(provider, keyElem),
						valueSer, true);
			}
			catch (Exception e) {
				wrapAndThrow(provider, e, value, String.valueOf(keyElem));
			}
		}
	}

	private void serializeOptionalFieldsAsTuple(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue)
			throws IOException {

		// If value type needs polymorphic type handling, some more work needed:
		if (_valueTypeSerializer != null) {
			serializeTypedFieldsAsTuple(value, gen, provider, suppressableValue);
			return;
		}
		final Set<String> ignored = _ignoredEntries;
		final boolean checkEmpty = (MARKER_FOR_EMPTY == suppressableValue);

		for (Map.Entry<?, ?> entry : value.entrySet()) {
			final Object keyElem = entry.getKey();
			final Object valueElem = entry.getValue();

			if (keyElem != null && ignored != null && ignored.contains(keyElem)) {
				continue;
			}

			if (valueElem == null && _suppressNulls) {
				continue;
			}

			JsonSerializer<Object> valueSer = _valueSerializer;
			if (valueSer == null) {
				valueSer = _findSerializer(provider, valueElem);
			}
			// also may need to skip non-empty values:
			if (checkEmpty) {
				if (valueSer.isEmpty(provider, valueElem)) {
					continue;
				}
			} else if (suppressableValue != null) {
				if (suppressableValue.equals(valueElem)) {
					continue;
				}
			}

			try {
				serializeTuple(keyElem, valueElem, gen, provider,
						_findSerializer(provider, keyElem),
						valueSer, false);
			}
			catch (Exception e) {
				wrapAndThrow(provider, e, value, String.valueOf(keyElem));
			}
		}

	}

	private void serializeFilteredAnyPropertiesAsTuple(SerializerProvider provider, JsonGenerator gen, Object bean, Map<?, ?> value,
			PropertyFilter filter, Object suppressableValue) throws IOException {

		final Set<String> ignored = _ignoredEntries;
		final boolean checkEmpty = (MARKER_FOR_EMPTY == suppressableValue);

		final MapProperty prop = new MapProperty(_valueTypeSerializer, _property) {
			// Special override for writing a tuple[key, value] instead of a "key":value
			@Override
			public void serializeAsField(Object map, JsonGenerator gen, SerializerProvider provider) throws IOException {
				serializeTuple(_key, _value, gen, provider,
						_keySerializer,
						_valueSerializer, _typeSerializer != null);
			}
		};

		for (Map.Entry<?, ?> entry : value.entrySet()) {
			final Object keyElem = entry.getKey();
			final Object valueElem = entry.getValue();

			if (keyElem != null && ignored != null && ignored.contains(keyElem)) {
				continue;
			}

			if (valueElem == null && _suppressNulls) {
				continue;
			}

			JsonSerializer<Object> valueSer = _valueSerializer;
			if (valueSer == null) {
				valueSer = _findSerializer(provider, valueElem);
			}
			// also may need to skip non-empty values:
			if (checkEmpty) {
				if (valueSer.isEmpty(provider, valueElem)) {
					continue;
				}
			} else if (suppressableValue != null) {
				if (suppressableValue.equals(valueElem)) {
					continue;
				}
			}

			try {
				prop.reset(keyElem, valueElem, _findSerializer(provider, keyElem), valueSer);
				filter.serializeAsField(bean != null ? bean : value, gen, provider, prop);
			}
			catch (Exception e) {
				wrapAndThrow(provider, e, value, String.valueOf(keyElem));
			}
		}

	}

	private void serializeFilteredFieldsAsTuple(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter,
			Object suppressableValue) throws IOException {

		serializeFilteredAnyPropertiesAsTuple(provider, gen, null, value, filter, suppressableValue);

	}

	private void serializeFieldsUsingAsTuple(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, JsonSerializer<Object> ser)
			throws IOException {

		final Set<String> ignored = _ignoredEntries;
		final TypeSerializer typeSer = _valueTypeSerializer;

		for (Map.Entry<?, ?> entry : value.entrySet()) {
			final Object keyElem = entry.getKey();
			final Object valueElem = entry.getValue();

			if (keyElem != null && ignored != null && ignored.contains(keyElem)) {
				continue;
			}

			try {
				serializeTuple(keyElem, valueElem, gen, provider,
						_findSerializer(provider, keyElem),
						ser, typeSer != null);
			}
			catch (Exception e) {
				wrapAndThrow(provider, e, value, String.valueOf(keyElem));
			}
		}

	}

	/**
	 * Returns a new Map as a TreeMap.
	 * Notice: There is a small side-effect that if the input map has en null entry, the entry is skipped in the returned map
	 * (TreeMap don't support null keys), thus the entry is directly serialized in the JsonGenerator
	 * /!\ do not call this method twice AND only within the scope of the serializeMapAsTuple(..) method
	 */
	private Map<?, ?> _orderEntriesAsTuple(Map<?, ?> input, JsonGenerator gen, SerializerProvider provider) throws IOException {
		// minor optimization: may already be sorted?
		if (input instanceof SortedMap<?, ?>) {
			return input;
		}
		// [databind#1411]: TreeMap does not like null key... (although note that
		//   check above should prevent this code from being called in that case)
		// [databind#153]: but, apparently, some custom Maps do manage hit this
		//   problem.
		if (_hasNullKey(input)) {
			TreeMap<Object, Object> result = new TreeMap<Object, Object>();
			for (Map.Entry<?, ?> entry : input.entrySet()) {
				Object key = entry.getKey();
				if (key == null) {
					_writeNullKeyedEntryAsTuple(gen, provider, entry.getValue());
					continue;
				}
				result.put(key, entry.getValue());
			}
			return result;
		}
		return new TreeMap<Object, Object>(input);
	}

	private void _writeNullKeyedEntryAsTuple(JsonGenerator gen, SerializerProvider provider, Object value) throws IOException {
		JsonSerializer<Object> keySerializer = provider.getDefaultNullValueSerializer();
		JsonSerializer<Object> valueSer;
		if (value == null) {
			if (_suppressNulls) {
				return;
			}
			valueSer = provider.getDefaultNullValueSerializer();
		} else {
			valueSer = _valueSerializer;
			if (valueSer == null) {
				valueSer = _findSerializer(provider, value);
			}
			if (_suppressableValue == MARKER_FOR_EMPTY) {
				if (valueSer.isEmpty(provider, value)) {
					return;
				}
			} else if ((_suppressableValue != null)
					&& (_suppressableValue.equals(value))) {
				return;
			}
		}

		try {
			keySerializer.serialize(null, gen, provider);
			valueSer.serialize(value, gen, provider);
		}
		catch (Exception e) {
			wrapAndThrow(provider, e, value, "");
		}
	}

}
