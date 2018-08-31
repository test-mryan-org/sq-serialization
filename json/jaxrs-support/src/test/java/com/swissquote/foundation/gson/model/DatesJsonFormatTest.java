package com.swissquote.foundation.gson.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import com.google.gson.Gson;
import com.swissquote.foundation.soa.gson.GsonConfig;

/**
 * @author Denis Larka
 * at 31.Aug.2017
 */
public class DatesJsonFormatTest {


	@Test
	public void test_local_date_format() {
		Gson gson = GsonConfig.buildSpecificConfig();
		final LocalDate localDate = LocalDate.parse("2017-08-31");
		final String goodJson = "\"2017-08-31\"";
		assertEquals(goodJson, gson.toJson(localDate));
		assertEquals(gson.fromJson(goodJson, LocalDate.class), localDate);
	}

	@Test
	public void test_local_date_time_format() {
		Gson gson = GsonConfig.buildSpecificConfig();
		final LocalDateTime localDateTime = LocalDateTime.parse("2017-08-31T15:42:11");
		final String goodJson = "\"2017-08-31T15:42:11\"";
		assertEquals(goodJson, gson.toJson(localDateTime));
		assertEquals(gson.fromJson(goodJson, LocalDateTime.class), localDateTime);
	}
}
