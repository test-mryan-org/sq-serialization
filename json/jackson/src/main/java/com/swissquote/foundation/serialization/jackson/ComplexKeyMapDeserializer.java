package com.swissquote.foundation.serialization.jackson;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.AccessPattern;
import com.fasterxml.jackson.databind.util.NameTransformer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComplexKeyMapDeserializer extends MapDeserializer {

	private JsonDeserializer<?> keyDeSerializer;

	public ComplexKeyMapDeserializer(MapDeserializer deserializer) {
		super(deserializer);
		log.info("Constructor  1 args");
	}

	public ComplexKeyMapDeserializer(ComplexKeyMapDeserializer deserializer, JsonDeserializer<?> keyDeser,
			JsonDeserializer<Object> valueDeser,
			TypeDeserializer valueTypeDeser, NullValueProvider nuller, Set<String> ignorable) {
		super(deserializer, null, valueDeser, valueTypeDeser, nuller, ignorable);
		this.keyDeSerializer = keyDeser;
		log.info("Constructor  1");
	}

	public ComplexKeyMapDeserializer(JavaType mapType, ValueInstantiator valueInstantiator, KeyDeserializer keyDeser,
			JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
		super(mapType, valueInstantiator, keyDeser, valueDeser, valueTypeDeser);
		log.info("Constructor  2");
	}

	protected ComplexKeyMapDeserializer(MapDeserializer src, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser,
			TypeDeserializer valueTypeDeser, NullValueProvider nuller, Set<String> ignorable) {
		super(src, keyDeser, valueDeser, valueTypeDeser, nuller, ignorable);
		log.info("Constructor  3");
	}

	@Override
	protected ComplexKeyMapDeserializer withResolved(KeyDeserializer keyDeser,
			TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser,
			NullValueProvider nuller,
			Set<String> ignorable) {
		log.info("override withResolved");

		if ((_keyDeserializer == keyDeser) && (_valueDeserializer == valueDeser)
				&& (_valueTypeDeserializer == valueTypeDeser) && (_nullProvider == nuller)
				&& (_ignorableProperties == ignorable)) {
			return this;
		}

		// maybe we can throw an exception instead of putting a null as keyDeser

		return new ComplexKeyMapDeserializer((MapDeserializer) this, null, (JsonDeserializer<Object>) valueDeser, valueTypeDeser, nuller,
				ignorable);
	}

	protected ComplexKeyMapDeserializer withResolved(JsonDeserializer<?> keyDeser,
			TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser,
			NullValueProvider nuller,
			Set<String> ignorable) {
		log.info("protected withResolved");

		return new ComplexKeyMapDeserializer(this, keyDeser, (JsonDeserializer<Object>) valueDeser, valueTypeDeser, nuller, ignorable);
	}

	// TODO with the same format of the Serializer, to override all the methods

	/**
	 * Method called to finalize setup of this deserializer,
	 * when it is known for which property deserializer is needed for.
	 */
	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
			BeanProperty property) throws JsonMappingException {
		if (TypeUtils.isKeySimpleType(_containerType)) {
			return super.createContextual(ctxt, property);
		}

		// The keyDeser is not anymore a KeyDeserializer object but a std ValueDeserializer
		JsonDeserializer<?> keyDeser = ctxt.findContextualValueDeserializer(_containerType.getKeyType(), property);

		JsonDeserializer<?> valueDeser = _valueDeserializer;
		// [databind#125]: May have a content converter
		if (property != null) {
			valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
		}
		final JavaType vt = _containerType.getContentType();
		if (valueDeser == null) {
			valueDeser = ctxt.findContextualValueDeserializer(vt, property);
		} else { // if directly assigned, probably not yet contextual, so:
			valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, vt);
		}
		TypeDeserializer vtd = _valueTypeDeserializer;
		if (vtd != null) {
			vtd = vtd.forProperty(property);
		}
		Set<String> ignored = _ignorableProperties;
		AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
		if (_neitherNull(intr, property)) {
			AnnotatedMember member = property.getMember();
			if (member != null) {
				JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals(member);
				if (ignorals != null) {
					Set<String> ignoresToAdd = ignorals.findIgnoredForDeserialization();
					if (!ignoresToAdd.isEmpty()) {
						ignored = (ignored == null) ? new HashSet<String>() : new HashSet<String>(ignored);
						for (String str : ignoresToAdd) {
							ignored.add(str);
						}
					}
				}
			}
		}
		return withResolved(keyDeser, vtd, valueDeser,
				findContentNullProvider(ctxt, property, valueDeser), ignored);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override deserialize 1");

		if (TypeUtils.isKeySimpleType(_containerType)) {
			return super.deserialize(p, ctxt);
		}

		if (_propertyBasedCreator != null) {
			return _deserializeUsingCreator(p, ctxt);
		}
		if (_delegateDeserializer != null) {
			return (Map<Object, Object>) _valueInstantiator.createUsingDelegate(ctxt,
					_delegateDeserializer.deserialize(p, ctxt));
		}
		if (!_hasDefaultCreator) {
			return (Map<Object, Object>) ctxt.handleMissingInstantiator(getMapClass(),
					getValueInstantiator(), p,
					"no default constructor found");
		}
		// Ok: must point to START_OBJECT, FIELD_NAME or END_OBJECT
		JsonToken t = p.getCurrentToken();
		if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME && t != JsonToken.END_OBJECT
				&& t != JsonToken.START_ARRAY //THIS is the new condition
		) {
			// (empty) String may be ok however; or single-String-arg ctor
			if (t == JsonToken.VALUE_STRING) {
				return (Map<Object, Object>) _valueInstantiator.createFromString(ctxt, p.getText());
			}
			// slightly redundant (since String was passed above), but also handles empty array case:
			return _deserializeFromEmpty(p, ctxt);
		}

		final Map<Object, Object> result = (Map<Object, Object>) _valueInstantiator.createUsingDefault(ctxt);
		if (t == JsonToken.START_ARRAY) {

			t = p.nextToken();
			while (t != JsonToken.END_ARRAY && p.nextToken() != JsonToken.END_ARRAY) {

				//t = p.nextToken(); // start of entity array, already consumed in the condition
				JsonToken jsonTokenStartObject = p.nextToken(); //Start of key object
				Object key = keyDeSerializer.deserialize(p, ctxt); // read object
				JsonToken jsonTokenStartValue = p.nextToken(); // start of value object
				Object value = _valueDeserializer.deserialize(p, ctxt); // read object
				JsonToken jsonTokenEndArrayEntity = p.nextToken(); // end of entity array
				result.put(key, value);

				t = p.nextToken(); // next
			}
		} else {
			//TODO something?
			// throw MismatchedInputException.from(p, ctxt.getContextualType(), t.asString());
			//			ctxt.handleUnexpectedToken(keyDeSerializer.handledType(), t, p,
			//					"Unexpected token, we want a list of entity for our ComplexKeyMap [[");
			//leave at is and return empty map
		}

		return result;
	}

	@Override
	public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result) throws IOException {
		log.info("override deserialize 2");
		return super.deserialize(p, ctxt, result);
	}

	@Override
	public void setIgnorableProperties(Set<String> ignorable) {
		log.info("override setIgnorableProperties");
		super.setIgnorableProperties(ignorable);
	}

	@Override
	public void resolve(DeserializationContext ctxt) throws JsonMappingException {
		log.info("override resolve");
		super.resolve(ctxt);
	}

	@Override
	public JsonDeserializer<Object> getContentDeserializer() {
		log.info("override getContentDeserializer");
		return super.getContentDeserializer();
	}

	@Override
	public ValueInstantiator getValueInstantiator() {
		log.info("override getValueInstantiator");
		return super.getValueInstantiator();
	}

	@Override
	public boolean isCachable() {
		log.info("override isCachable");
		return super.isCachable();
	}

	@Override
	public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
		log.info("override deserializeWithType");

		return super.deserializeWithType(p, ctxt, typeDeserializer);
	}

	@Override
	public JavaType getValueType() {
		log.info("override getValueType");
		return super.getValueType();
	}

	@Override
	public Map<Object, Object> _deserializeUsingCreator(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override _deserializeUsingCreator");
		return super._deserializeUsingCreator(p, ctxt);
	}

	@Override
	public Boolean supportsUpdate(DeserializationConfig config) {
		log.info("override supportsUpdate");
		return super.supportsUpdate(config);
	}

	@Override
	public SettableBeanProperty findBackReference(String refName) {
		log.info("override findBackReference");
		return super.findBackReference(refName);
	}

	@Override
	public JavaType getContentType() {
		log.info("override getContentType");
		return super.getContentType();
	}

	@Override
	public AccessPattern getEmptyAccessPattern() {
		log.info("override getEmptyAccessPattern");
		return super.getEmptyAccessPattern();
	}

	@Override
	public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
		log.info("override getEmptyValue");
		return super.getEmptyValue(ctxt);
	}

	@Override
	protected <BOGUS> BOGUS wrapAndThrow(Throwable t, Object ref, String key) throws IOException {
		log.info("override wrapAndThrow");
		return super.wrapAndThrow(t, ref, key);
	}

	@Override
	public Class<?> handledType() {
		log.info("override handledType");
		return super.handledType();
	}

	@Override
	protected boolean isDefaultDeserializer(JsonDeserializer<?> deserializer) {
		log.info("override isDefaultDeserializer");
		return super.isDefaultDeserializer(deserializer);
	}

	@Override
	protected boolean isDefaultKeyDeserializer(KeyDeserializer keyDeser) {
		log.info("override isDefaultKeyDeserializer");
		return super.isDefaultKeyDeserializer(keyDeser);
	}

	@Override
	protected boolean _parseBooleanFromInt(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override _parseBooleanFromInt");
		return super._parseBooleanFromInt(p, ctxt);
	}

	@Override
	protected Date _parseDate(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override _parseDate");
		return super._parseDate(p, ctxt);
	}

	@Override
	protected Date _parseDateFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override _parseDateFromArray");
		return super._parseDateFromArray(p, ctxt);
	}

	@Override
	protected Date _parseDate(String value, DeserializationContext ctxt) throws IOException {
		log.info("override _parseDate");
		return super._parseDate(value, ctxt);
	}

	@Override
	protected Map<Object, Object> _deserializeFromEmpty(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override _deserializeFromEmpty");
		return super._deserializeFromEmpty(p, ctxt);
	}

	@Override
	protected boolean _hasTextualNull(String value) {
		log.info("override _hasTextualNull");
		return super._hasTextualNull(value);
	}

	@Override
	protected boolean _isEmptyOrTextualNull(String value) {
		log.info("override _isEmptyOrTextualNull");
		return super._isEmptyOrTextualNull(value);
	}

	@Override
	protected Map<Object, Object> _deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override _deserializeFromArray");
		return super._deserializeFromArray(p, ctxt);
	}

	@Override
	protected Map<Object, Object> _deserializeWrappedValue(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override _deserializeWrappedValue");
		return super._deserializeWrappedValue(p, ctxt);
	}

	@Override
	protected void _failDoubleToIntCoercion(JsonParser p, DeserializationContext ctxt, String type) throws IOException {
		log.info("override _failDoubleToIntCoercion");
		super._failDoubleToIntCoercion(p, ctxt, type);
	}

	@Override
	protected Object _coerceIntegral(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override _coerceIntegral");
		return super._coerceIntegral(p, ctxt);
	}

	@Override
	protected Object _coerceNullToken(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
		log.info("override _coerceNullToken");
		return super._coerceNullToken(ctxt, isPrimitive);
	}

	@Override
	protected Object _coerceTextualNull(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
		log.info("override _coerceTextualNull");
		return super._coerceTextualNull(ctxt, isPrimitive);
	}

	@Override
	protected Object _coerceEmptyString(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
		log.info("override _coerceEmptyString");
		return super._coerceEmptyString(ctxt, isPrimitive);
	}

	@Override
	protected void _verifyStringForScalarCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
		log.info("override _verifyStringForScalarCoercion");
		super._verifyStringForScalarCoercion(ctxt, str);
	}

	@Override
	protected void _verifyNumberForScalarCoercion(DeserializationContext ctxt, JsonParser p) throws IOException {
		log.info("override _verifyNumberForScalarCoercion");
		super._verifyNumberForScalarCoercion(ctxt, p);
	}

	@Override
	protected void _reportFailedNullCoerce(DeserializationContext ctxt, boolean state, Enum<?> feature, String inputDesc)
			throws JsonMappingException {
		log.info("override _reportFailedNullCoerce");
		super._reportFailedNullCoerce(ctxt, state, feature, inputDesc);
	}

	@Override
	protected String _coercedTypeDesc() {
		log.info("override _coercedTypeDesc");
		return super._coercedTypeDesc();
	}

	@Override
	protected JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, JavaType type, BeanProperty property)
			throws JsonMappingException {
		log.info("override findDeserializer");
		return super.findDeserializer(ctxt, type, property);
	}

	@Override
	protected JsonDeserializer<?> findConvertingContentDeserializer(DeserializationContext ctxt, BeanProperty prop,
			JsonDeserializer<?> existingDeserializer) throws JsonMappingException {
		log.info("override findConvertingContentDeserializer");
		return super.findConvertingContentDeserializer(ctxt, prop, existingDeserializer);
	}

	@Override
	protected JsonFormat.Value findFormatOverrides(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults) {
		log.info("override findFormatOverrides");
		return super.findFormatOverrides(ctxt, prop, typeForDefaults);
	}

	@Override
	protected Boolean findFormatFeature(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults, JsonFormat.Feature feat) {
		log.info("override findFormatFeature");
		return super.findFormatFeature(ctxt, prop, typeForDefaults, feat);
	}

	@Override
	protected NullValueProvider findContentNullProvider(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> valueDeser)
			throws JsonMappingException {
		log.info("override findContentNullProvider");
		return super.findContentNullProvider(ctxt, prop, valueDeser);
	}

	@Override
	protected Nulls findContentNullStyle(DeserializationContext ctxt, BeanProperty prop) throws JsonMappingException {
		log.info("override findContentNullStyle");
		return super.findContentNullStyle(ctxt, prop);
	}

	@Override
	protected void handleUnknownProperty(JsonParser p, DeserializationContext ctxt, Object instanceOrClass, String propName) throws IOException {
		log.info("override handleUnknownProperty");
		super.handleUnknownProperty(p, ctxt, instanceOrClass, propName);
	}

	@Override
	protected void handleMissingEndArrayForSingle(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override handleMissingEndArrayForSingle");
		super.handleMissingEndArrayForSingle(p, ctxt);
	}

	@Override
	protected void _verifyEndArrayForSingle(JsonParser p, DeserializationContext ctxt) throws IOException {
		log.info("override _verifyEndArrayForSingle");
		super._verifyEndArrayForSingle(p, ctxt);
	}

	@Override
	protected Number _nonNullNumber(Number n) {
		log.info("override _nonNullNumber");
		return super._nonNullNumber(n);
	}

	@Override
	public JsonDeserializer<Map<Object, Object>> unwrappingDeserializer(NameTransformer unwrapper) {
		log.info("override unwrappingDeserializer");
		return super.unwrappingDeserializer(unwrapper);
	}

	@Override
	public JsonDeserializer<?> replaceDelegatee(JsonDeserializer<?> delegatee) {
		log.info("override replaceDelegatee");
		return super.replaceDelegatee(delegatee);
	}

	@Override
	public JsonDeserializer<?> getDelegatee() {
		log.info("override getDelegatee");
		return super.getDelegatee();
	}

	@Override
	public Collection<Object> getKnownPropertyNames() {
		log.info("override getKnownPropertyNames");
		return super.getKnownPropertyNames();
	}

	@Override
	public Map<Object, Object> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
		log.info("override getNullValue");
		return super.getNullValue(ctxt);
	}

	@Override
	public AccessPattern getNullAccessPattern() {
		log.info("override getNullAccessPattern");
		return super.getNullAccessPattern();
	}

	@Override
	public ObjectIdReader getObjectIdReader() {
		log.info("override getObjectIdReader");
		return super.getObjectIdReader();
	}

	@Override
	public Map<Object, Object> getNullValue() {
		log.info("override getNullValue");
		return super.getNullValue();
	}

	@Override
	public Object getEmptyValue() {
		log.info("override getEmptyValue");
		return super.getEmptyValue();
	}
}
