package com.swissquote.foundation.serialization.json;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TestObjectJavaUtilDate {

	private String name;
	private List<Date> creationDates;
}