package com.swissquote.foundation.serialization.json.model;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestObjectInstant {

	private String name;
	private List<Instant> creationDates;
}