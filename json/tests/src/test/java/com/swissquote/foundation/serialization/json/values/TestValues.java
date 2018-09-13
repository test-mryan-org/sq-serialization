package com.swissquote.foundation.serialization.json.values;

import java.math.BigDecimal;

import com.swissquote.foundation.serialization.json.model.TestThreeFields;

public class TestValues {
	private TestValues() {
	}

	public static final String STRING_HELLO = "Hello";
	public static final String STRING_WORLD = "World";
	public static final String STRING_KEY_1 = "key1";
	public static final String STRING_KEY_2 = "key2";
	public static final BigDecimal BD_VALUE_1 = new BigDecimal("123.4");
	public static final BigDecimal BD_VALUE_2 = new BigDecimal("4.321");

	public static final TestThreeFields TF_HELLO = new TestThreeFields(STRING_HELLO, 123L, BD_VALUE_1);
	public static final TestThreeFields TF_WORLD = new TestThreeFields(STRING_WORLD, 321L, BD_VALUE_2);
}
