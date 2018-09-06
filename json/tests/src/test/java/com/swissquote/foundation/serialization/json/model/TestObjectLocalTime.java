package com.swissquote.foundation.serialization.json.model;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestObjectLocalTime {

	private String name;
	private List<LocalTime> creationDates;
}