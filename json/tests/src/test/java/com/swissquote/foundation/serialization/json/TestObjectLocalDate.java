package com.swissquote.foundation.serialization.json;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TestObjectLocalDate {

	private String name;
	private List<LocalDate> creationDates;
}