package com.swissquote.foundation.serialization.json;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TestObjectLocalTime {

	private String name;
	private List<LocalTime> creationDates;
}