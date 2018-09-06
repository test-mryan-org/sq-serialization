package com.swissquote.foundation.serialization.json.model;

import java.time.Instant;
import java.util.List;

import lombok.Value;

@Value
public class TestValueInstant {

	private String name;
	private List<Instant> creationDates;
}