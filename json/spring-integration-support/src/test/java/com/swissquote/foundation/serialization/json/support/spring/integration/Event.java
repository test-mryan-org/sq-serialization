package com.swissquote.foundation.serialization.json.support.spring.integration;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Value {
		private String value;
	}

	private List<Value> values;
}
