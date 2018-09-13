package com.swissquote.foundation.serialization.json.model;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class TestContainsComplicatedMap {
	private Map<TestThreeFields, String> complicatedMap;
}
