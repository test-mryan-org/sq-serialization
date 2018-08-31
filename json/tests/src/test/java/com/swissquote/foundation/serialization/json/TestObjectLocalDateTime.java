package com.swissquote.foundation.serialization.json;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TestObjectLocalDateTime {

	private String name;
	private LocalDateTime creationDate;
}