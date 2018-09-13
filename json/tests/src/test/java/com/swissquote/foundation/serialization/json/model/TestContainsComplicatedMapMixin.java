package com.swissquote.foundation.serialization.json.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class TestContainsComplicatedMapMixin {

	@JsonCreator
	public TestContainsComplicatedMapMixin(
			@JsonProperty("complicatedMap") Map<TestThreeFields, String> complicatedMap) {

	}
}