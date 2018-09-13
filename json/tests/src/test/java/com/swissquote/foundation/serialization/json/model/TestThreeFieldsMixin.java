package com.swissquote.foundation.serialization.json.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class TestThreeFieldsMixin {

	@JsonCreator
	public TestThreeFieldsMixin(
			@JsonProperty("stringField") String stringField,
			@JsonProperty("longField") Long longField,
			@JsonProperty("bdField") BigDecimal bdField) {

	}
}