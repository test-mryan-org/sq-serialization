package com.swissquote.foundation.serialization.api.v1.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ComplexValueMixin {

	@JsonCreator
	public ComplexValueMixin(
			@JsonProperty("name") String name
	) {

	}
}
