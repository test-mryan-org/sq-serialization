package com.swissquote.foundation.serialization.json;

import java.time.Instant;
import java.util.List;

import lombok.Value;

@Value
class TestValueInstant {

	private String name;
	private List<Instant> creationDates;
}