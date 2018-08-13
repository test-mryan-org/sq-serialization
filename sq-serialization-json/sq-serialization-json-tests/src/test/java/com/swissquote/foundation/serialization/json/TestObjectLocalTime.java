package com.swissquote.foundation.serialization.json;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TestObjectLocalTime {

	private String name;
	private LocalTime creationDate;
}