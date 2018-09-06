package com.swissquote.foundation.serialization.json.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestObjectJavaUtilDate {

	private String name;
	private List<Date> creationDates;
}