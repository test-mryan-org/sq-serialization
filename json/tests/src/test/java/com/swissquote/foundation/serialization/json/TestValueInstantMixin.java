package com.swissquote.foundation.serialization.json;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class TestValueInstantMixin {

	@JsonCreator
	public TestValueInstantMixin(
			@JsonProperty("name") String name,
			@JsonProperty("creationDate") Instant creationDate) {

	}
}