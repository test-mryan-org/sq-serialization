package com.swissquote.foundation.serialization.json;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TestObjectZonedDateTime {

	private String name;
	private ZonedDateTime creationDate;
}