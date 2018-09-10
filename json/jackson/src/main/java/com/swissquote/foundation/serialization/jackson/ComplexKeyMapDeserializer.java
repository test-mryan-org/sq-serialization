package com.swissquote.foundation.serialization.jackson;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ComplexKeyMapDeserializer extends MapDeserializer {

	private JsonDeserializer<?> keyDeserAsSerializer;

	public ComplexKeyMapDeserializer(MapDeserializer deserializer) {
		super(deserializer);
	}

	public ComplexKeyMapDeserializer(ComplexKeyMapDeserializer deserializer, JsonDeserializer<?> keyDeser, JsonDeserializer<Object> valueDeser,
			TypeDeserializer valueTypeDeser, NullValueProvider nuller, Set<String> ignorable) {
		super(deserializer, null, valueDeser, valueTypeDeser, nuller, ignorable);
		this.keyDeserAsSerializer = keyDeser;
	}

	@Override
	protected ComplexKeyMapDeserializer withResolved(KeyDeserializer keyDeser,
			TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser,
			NullValueProvider nuller,
			Set<String> ignorable) {

		if ((_keyDeserializer == keyDeser) && (_valueDeserializer == valueDeser)
				&& (_valueTypeDeserializer == valueTypeDeser) && (_nullProvider == nuller)
				&& (_ignorableProperties == ignorable)) {
			return this;
		}

		// maybe we can throw an exception instead of putting a null as keyDeser

		return new ComplexKeyMapDeserializer(this, null, (JsonDeserializer<Object>) valueDeser, valueTypeDeser, nuller, ignorable);
	}

	protected ComplexKeyMapDeserializer withResolved(JsonDeserializer<?> keyDeser,
			TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser,
			NullValueProvider nuller,
			Set<String> ignorable) {

		return new ComplexKeyMapDeserializer(this, keyDeser, (JsonDeserializer<Object>) valueDeser, valueTypeDeser, nuller, ignorable);
	}

	// TODO with the same format of the Serializer, to override all the methods

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
			BeanProperty property) throws JsonMappingException {
		if (TypeUtils.isKeySimpleType(_containerType)) {
			return super.createContextual(ctxt, property);
		}

		// The keyDeser is not anly more a KeyDeserializer object but a std ValueDeserializer
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
		if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME && t != JsonToken.END_OBJECT) {
			// (empty) String may be ok however; or single-String-arg ctor
			if (t == JsonToken.VALUE_STRING) {
				return (Map<Object, Object>) _valueInstantiator.createFromString(ctxt, p.getText());
			}
			// slightly redundant (since String was passed above), but also handles empty array case:
			return _deserializeFromEmpty(p, ctxt);
		}
		final Map<Object, Object> result = (Map<Object, Object>) _valueInstantiator.createUsingDefault(ctxt);
		if (_standardStringKey) {
			_readAndBindStringKeyMap(p, ctxt, result);
			return result;
		}
		_readAndBind(p, ctxt, result);
		return result;
	}
}
