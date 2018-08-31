package com.swissquote.foundation.serialization.json;

import java.time.Instant;

import lombok.Value;

@Value
class TestValueInstant {

	private String name;
	private Instant creationDate;
}