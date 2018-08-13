package com.swissquote.foundation.serialization.json;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TestObjectInstant {

	private String name;
	private Instant creationDate;
}