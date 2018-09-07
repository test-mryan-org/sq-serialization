package com.swissquote.foundation.serialization.jackson;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ComplexKeyMapDeserializer extends MapDeserializer {

	public ComplexKeyMapDeserializer(MapDeserializer deserializer) {
		super(deserializer);
	}

	public ComplexKeyMapDeserializer(ComplexKeyMapDeserializer deserializer, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser,
			TypeDeserializer valueTypeDeser, NullValueProvider nuller, Set<String> ignorable) {
		super(deserializer, keyDeser, valueDeser, valueTypeDeser, nuller, ignorable);
	}

	protected ComplexKeyMapDeserializer withResolved(KeyDeserializer keyDeser,
			TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser,
			NullValueProvider nuller,
			Set<String> ignorable) {

		if ((_keyDeserializer == keyDeser) && (_valueDeserializer == valueDeser)
				&& (_valueTypeDeserializer == valueTypeDeser) && (_nullProvider == nuller)
				&& (_ignorableProperties == ignorable)) {
			return this;
		}
		return new ComplexKeyMapDeserializer(this, keyDeser, (JsonDeserializer<Object>) valueDeser, valueTypeDeser, nuller, ignorable);
	}

	// TODO with the same format of the Serializer, to override all the methods
}
